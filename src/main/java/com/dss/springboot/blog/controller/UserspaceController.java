package com.dss.springboot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dss.springboot.blog.domain.Blog;
import com.dss.springboot.blog.domain.Catalog;
import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.domain.Vote;
import com.dss.springboot.blog.service.BlogService;
import com.dss.springboot.blog.service.CatalogService;
import com.dss.springboot.blog.service.UserService;
import com.dss.springboot.blog.utils.ConstraintViolationExceptionHandler;
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
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CatalogService catalogService;
	
	@Value("${file.service.url}") 		//这个表示从配置文件中读取file.service.url的值
	private String fileServerUrl;
	
	/**
	 * 通过用户名访问用户个人主页，在header中点击个人中心，来到这里
	 * @param username
	 * @return
	 */
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username, Model model) {
		
		//使用security中的接口获取已认证的用户user信息
		User user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return "redirect:/u/" + username + "/blogs";			//这里是重定向到 listBlogsByOrder 这个方法
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
		
		return ResponseEntity.ok().body(new Response(true, "上传成功", avatarUrl));

	}
	
	/**
	 * 获取登录用户的博客列表，也就是个人主页页面
	 * @param username
	 * @param order		排序方式 默认值是按最新排序
	 * @param catalogId		类别
	 * @param keyword	关键字
	 * @param async		是否异步请求
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/{username}/blogs")
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order", required=false, defaultValue="new") String order,
			@RequestParam(value="catalog", required=false) Long catalogId,
			@RequestParam(value="keyword", required=false, defaultValue="") String keyword,
			@RequestParam(value="async", required=false) boolean async,
			@RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize", required=false, defaultValue="10") int pageSize,
			Model model) {
		
		
		//使用security中的接口获取已认证的用户user信息
		User user = (User) userDetailsService.loadUserByUsername(username);
		
		Page<Blog> page = null;
		
		if(catalogId != null && catalogId > 0) {

			Catalog catalog = catalogService.getCatalogById(catalogId);
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogByCatalog(catalog, pageable);
			order = "";		//这里因为order有默认值 new ，所以这里重置成空
			
		}else if("hot".equals(order)) {			//按最热排序，最热也就是访问量最多，评论量，点赞量来排序

			Sort sort = new Sort(Direction.DESC, "readSize", "commentSize", "voteSize");
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
			
		}else if("new".equals(order)) {
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByTitleVote(user, keyword, pageable);
		}
		
		List<Blog> list = page.getContent();
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("catalogId", catalogId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		
		return (async == true ? "/userspace/u :: #mainContainerRepleace" : "/userspace/u");
	}
	
	/**
	 * 查询用户的blog，通过id查询，去往显示一个blog详情的页面
	 * @param id
	 * @return
	 */
	@GetMapping("/{username}/blogs/{id}")
	public ModelAndView listBlogsByOrder(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		
		User principal = null;
		Blog blog = blogService.getBlogById(id);	//获取这个blog	
		
		blogService.readingIncrease(id);		//blog访问量加1
		
		//判断操作用户是否为这个blog的所有者，这里使用的是Security
		boolean isBlogOwner = false;
		//这里就是获取当前登录的用户
		if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal != null  && username.equals(principal.getUsername())) {
				isBlogOwner = true;
			}
		}
		
		/**
		 * 看当前登录的用户，是否对这个blog进行过点赞操作
		 */
		List<Vote> votes = blog.getVotes();			//看这个blog的所有点赞信息
		Vote currentVote = null;
		
		if(principal != null) {
			//如果点过赞了，就在前台显示取消点赞按钮，反之就显示点赞按钮
			for(Vote vote : votes) {
				if(vote.getUser().getUsername().equals(principal.getUsername())) {
					currentVote = vote;
					break;
				}
			}
		}
		
		model.addAttribute("isBlogOwner", isBlogOwner);		//如果是当前登录用户的blog，则允许在页面出现修改按钮
		model.addAttribute("blogModel", blog);
		model.addAttribute("currentVote", currentVote);
	
		return new ModelAndView("userspace/blog");
	}
	
	/**
	 *获取新增blog的页面
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit")
	public ModelAndView createBolg(@PathVariable("username") String username, Model model) {
 
		//把分类列表返回到页面
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.getCatalogsByUser(user);
		
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("blog", new Blog(null, null, null, null));
		model.addAttribute("fileServerUrl", fileServerUrl);
		
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * 获取编辑blog的界面，需要传入blog id
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit/{id}")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ModelAndView createBolg(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
 
		//把分类列表返回到页面
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.getCatalogsByUser(user);
		
		model.addAttribute("catalogs", catalogs);
		
		model.addAttribute("blog", blogService.getBlogById(id));
		model.addAttribute("fileServerUrl", fileServerUrl);
		
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * 保存博客
	 * @param username
	 * @param blog
	 * @return
	 */
	@PostMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog){
		
		//对catalog进行空的判断
		if(blog.getCatalog() == null) {
			return ResponseEntity.ok().body(new Response(false, "未选择分类！！！")); 
		}
		
		try {
			
			//判断是修改还是新增
			if(blog.getId() != null && blog.getId() != 0) {
				//修改
				Blog orinalBlog = blogService.getBlogById(blog.getId());
				orinalBlog.setTitle(blog.getTitle());
				orinalBlog.setContent(blog.getContent());
				orinalBlog.setSummary(blog.getSummary());
				orinalBlog.setCatalog(blog.getCatalog());  //这里要重新设置分类，因为可能改了分类
				
				blog = blogService.saveBlog(orinalBlog);
			}else {
				//新增
				//使用security中的接口获取已认证的用户user信息
				User user = (User) userDetailsService.loadUserByUsername(username);
				blog.setUser(user);
				
				blog = blogService.saveBlog(blog);
			}
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
		return ResponseEntity.ok().body(new Response(true, "操作成功", redirectUrl));
	}
	
	/**
	 * 删除blog
	 * @param username
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{username}/blogs/{id}")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id){
	
		try {
			blogService.removeBlog(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs";
		return ResponseEntity.ok().body(new Response(true, "操作成功", redirectUrl));
	}
}
