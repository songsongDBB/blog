package com.dss.springboot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.service.UserService;
import com.dss.springboot.blog.vo.Response;

/**
 * 用户个人主页空间控制器
 * @author duan ss
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Value("${file.service.url}") 		//这个表示从配置文件中读取file.service.url的值
	private String fileServerUrl;
	
	/**
	 * 通过用户名访问用户个人主页
	 * @param username
	 * @return
	 */
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username) {
		System.out.println("username: " + username);
		return "/userspace/u";
	}
	
	/**
	 * 获取用户个人设置的主页面
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")			//这个是spring security的校验，看传过来的这个username是不是当前在认证信息中的username
	public ModelAndView profile(@PathVariable("username") String username, Model model) {
		
		//使用security中的接口获取已认证的用户user信息
		User user = (User) userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("fileServerUrl", fileServerUrl);		//文件服务器的地址，这里带到前台，方便头像的上传
		
		return new ModelAndView("/userspace/profile", "userModel", model);
	}
	
	/**
	 * 保存用户的个人设置
	 * @param username
	 * @param user
	 * @return
	 */
	@PostMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")
	public String saveProfile(@PathVariable("username") String username, User user) {
		
		//通过页面传来的userid获取user信息
		User originalUser = userService.getUserById(user.getId());
		if(originalUser != null ) {
			originalUser.setEmail(user.getEmail());
			originalUser.setName(user.getName());
			
			//判断密码是否做了变更
			String rawPassword = originalUser.getPassword();
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(user.getPassword());		//将页面穿来的密码进行BCrypt加密
			boolean isMathc = encoder.matches(rawPassword, encodedPassword);
			if(!isMathc) {
				//如果密码发生了变更，把前台传入的密码设置到user对象，在setEncodePassword这个方法中会对密码进行加密
				originalUser.setEncodePassword(user.getPassword());
			}
			
			userService.saveOrUpdateUser(originalUser);
			return "redirect:/u/" + username + "/profile";
		}
		
		return "";
	}
	
	/**
	 * 获取编辑头像的页面
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView avatar(@PathVariable("username") String username, Model model) {
		
		//使用security中的接口获取已认证的用户user信息
		User user = (User) userDetailsService.loadUserByUsername(username);
		
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/avatar", "userModel", model);
	}

	/**
	 * 保存头像
	 * @param username
	 * @param user
	 * @return
	 */
	@PostMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user){
		
		//当图片上传服务器成功后，服务器会返回一个图片地址，需要把这个地址存入数据库
		String avatarUrl = user.getAvatar();
		
		User originalUser = userService.getUserById(user.getId());
		originalUser.setAvatar(avatarUrl);
		
		userService.saveOrUpdateUser(originalUser);
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));

	}
	
	/**
	 * 查询用户的blog信息
	 * @param username		用户名
	 * order 排序规则
	 * category blog类别
	 * keyword 关键字
	 * @return
	 */
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order", required=false, defaultValue="new") String order,
			@RequestParam(value="category", required=false) Long category,
			@RequestParam(value="keyword", required=false) String keyword) {
		
		if(category != null) {
			
			System.out.print("category:" +category );
			System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?category="+category);
			return "/userspace/u";
		
		}else if(keyword != null && keyword.isEmpty() == false) {
			
			System.out.print("keyword:" +keyword );
			System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?keyword="+keyword);
			return "/u";
			
		}
		
		System.out.print("order:" +order);
		System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?order="+order);
		return "/u";
	}
	
	/**
	 * 查询用户的blog，通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping("/{username}/blogs/{id}")
	public String listBlogsByOrder(@PathVariable("id") Long id) {
		 
		System.out.print("blogId:" + id);
		return "/userspace/blog";
	}
	
	/**
	 * 修改用户blog
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit")
	public String editBlog() {
 
		return "/userspace/blogedit";
	}
}
