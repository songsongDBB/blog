package com.dss.springboot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.service.UserService;
import com.dss.springboot.blog.utils.ConstraintViolationExceptionHandler;
import com.dss.springboot.blog.vo.Response;

/**
 * 用户管理控制器
 * @author duan ss
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired 
	private UserService userService;

	/**
	 * 查询所有的用户
	 * @param async 是否是异步，第一次进页面，非异步，后面点击查询的时候就是异步请求
	 * @param pageIndex
	 * @param pageSize
	 * @param name
	 * @param model
	 * @return
	 */
	@GetMapping
	public ModelAndView list(@RequestParam(value="async", required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			@RequestParam(value="name",required=false,defaultValue="") String name,
			Model model) {
		
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<User> page = userService.listUserByNameList(name, pageable);
		List<User> list = page.getContent();
		
		model.addAttribute("page", page);
		model.addAttribute("userList", list);
		
		// users/list :: #mainContainerRepleace 表示如果是异步请求，将数据返回到user/list页面中的 id为mainContainerRepleace的div中
		ModelAndView mv = new ModelAndView(async==true ? "users/list :: #mainContainerRepleace" : "users/list", "userModel", model);
		return mv;
	}

 
	/**
	 * 获取 创建用户 form 表单页面
	 * @param user
	 * @return
	 */
	@GetMapping("/add")
	public ModelAndView createForm(Model model) {
		model.addAttribute("user", new User(null, null, null, null));
		return new ModelAndView("users/add", "userModel", model);
	}
	
	
	/**
	 * 保存或者修改用户
	 * @param user
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Response> saveOrUpdateUser(User user){
		
		try {
			userService.saveOrUpdateUser(user);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		}
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", user));
	}
	

	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {
		
		try {
			userService.removeUser(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}

		return ResponseEntity.ok().body(new Response(false, "处理成功"));
	}

	/**
	 * 通过userid获取用户信息，用于修改用户
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "edit/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		return new ModelAndView("users/edit", "userModel", model);
	}

}
