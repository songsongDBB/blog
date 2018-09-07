package com.dss.springboot.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Blog 控制器
 * @author duan ss
 */
@RestController
@RequestMapping("/blogs")
public class BlogController {

	/**
	 * 查询blog列表
	 * @param order	排序方式，默认值是new,表示按最新排序
	 * @param keyword	关键字搜索
	 * @return
	 */
	@GetMapping
	public String listBlogs(@RequestParam(value="order", required=false, defaultValue="new") String order,
			@RequestParam(value="keyword", required=false, defaultValue="") String keyword) {
		System.out.println("order: "+order+"; keyword: "+keyword);
		return "redirect:/index?order="+order+"&keyword="+keyword;
	}
	
}
