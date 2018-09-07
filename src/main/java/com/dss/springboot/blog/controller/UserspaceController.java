package com.dss.springboot.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户个人主页空间控制器
 * @author duan ss
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

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
