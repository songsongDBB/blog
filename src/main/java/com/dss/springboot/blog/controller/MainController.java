package com.dss.springboot.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主页控制器
 * @author duan ss
 *
 */
@Controller
public class MainController {

	/**
	 * 访问根目录时，重定向到index页面
	 * @return
	 */
	@GetMapping("/")
	public String root() {
		return "redirect:/index";
	}
	
	/**
	 * 访问index页面
	 * @return
	 */
	@GetMapping("/index")
	public String index() {
		return "redirect:/blogs";		//访问首页时，重定向到blogs请求
	}
	
	/**
	 * 访问登录页面
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	/**
	 * 登录失败，返回错误信息，并且去login页面
	 * @param model
	 * @return
	 */
	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		model.addAttribute("errorMsg", "登录失败，用户名或密码错误...");
		return "login";
	}
	
	/**
	 * 访问注册页面
	 * @return
	 */
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	/**
	 * 访问search页面
	 * @return
	 */
	@GetMapping("/search")
	public String search() {
		return "search";
	}
	
	/**
	 * 注册用户
	 * 这个方法写在这里，不写在UserController里面是因为 UserController里用的RestController,这个方法需要重定向到login页面
	 * @param user
	 * @return
	 */
	/*@PostMapping("/register")
	public String registerUser(User user) {
		userService.registerUser(user);
		return "redirect:/login";
	}*/
	
}
