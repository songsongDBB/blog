package com.dss.springboot.blog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dss.springboot.blog.vo.Menu;

/**
 * 后台用户管理控制器
 * @author duan ss
 */
@Controller
@RequestMapping("/admins")
public class AdminController {

	/**
	 * 去后台管理主页面
	 * 从这里进入后台管理员的页面，会从这里查出后台管理员页面的所有菜单
	 * @param model
	 * @return
	 */
	@GetMapping
	public ModelAndView listUsers(Model model) {
		
		List<Menu> list = new ArrayList<Menu>();
		list.add(new Menu("用户管理", "/users"));
		model.addAttribute("list", list);
		
		return new ModelAndView("admin/index", "model", model);
	}
}
