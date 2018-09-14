package com.dss.springboot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dss.springboot.blog.domain.Blog;
import com.dss.springboot.blog.domain.Catalog;
import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.service.BlogService;
import com.dss.springboot.blog.service.CatalogService;
import com.dss.springboot.blog.utils.ConstraintViolationExceptionHandler;
import com.dss.springboot.blog.vo.CatalogVO;
import com.dss.springboot.blog.vo.Response;

/**
 * 博客分类管理控制器
 * @author duan ss
 */
@Controller
@RequestMapping("/catalogs")
public class CatalogController {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private CatalogService catalogService;
	
	@Autowired
	private BlogService blogService;
	
	/**
	 * 通过username获取分类列表
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping
	public String listCatalogs(@RequestParam(value="username", required=true) String username, Model model) {
		
		//使用security中的接口获取已认证的用户user信息
		User user = (User) userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.getCatalogsByUser(user);
		
		//判断操作用户是否为catalog的所有者，这里使用的是Security
		boolean isOwner = false;
		//这里就是获取当前登录的用户
		if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal != null  && username.equals(principal.getUsername())) {
				isOwner = true;
			}
		}
		
		model.addAttribute("isCatalogsOwner", isOwner);
		model.addAttribute("catalogs", catalogs);
		
		return "/userspace/u :: #catalogRepleace";
	}
	
	/**
	 * 创建分类，会从页面传来一个catalogVO的对象，里面有一个user和catalog
	 * @param catalogVO
	 * @return
	 */
	@PostMapping
	@PreAuthorize("authentication.name.equals(#catalogVO.username)")		// catalogVO中的username和当前登录的username一样，才能创建
	public ResponseEntity<Response> create(@RequestBody CatalogVO catalogVO) {
		
		String username = catalogVO.getUsername();
		Catalog catalog = catalogVO.getCatalog();
		
		//使用security中的接口获取已认证的用户user信息
		User user = (User) userDetailsService.loadUserByUsername(username);
		
		try {
			catalog.setUser(user);
			catalogService.saveCatalog(catalog);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}

		return ResponseEntity.ok().body(new Response(true, "操作成功", null));
	}
	
	/**
	 * 删除分类
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("authentication.name.equals(#username)")  // 要求username和当前登录的username一样
	public ResponseEntity<Response> delete(String username, @PathVariable("id") Long id) {
		
		try {
			
			//这里应该要先判断该分类下面有没有博客，如果有的话，要让用户先把这些博客修改分类
			User user = (User) userDetailsService.loadUserByUsername(username);
			boolean result = blogService.isExsitBlogByCatalogId(id, user);
			if(result) {		//存在博客，不能直接删除
				return ResponseEntity.ok().body(new Response(false, "该分类下面有博客，请你先修改这些博客的分类信息！！！"));
			}
			
			catalogService.removeCatalog(id);			
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "操作成功", null));
	}
	
	/**
	 * 前往新增分类页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit")
	public String getCatalogEdit(Model model) {
		Catalog catalog = new Catalog(null, null);
		model.addAttribute("catalog",catalog);
		return "/userspace/catalogedit";
	}

	/**
	 * 根据 Id 获取分类信息，前往分类修改页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String getCatalogById(@PathVariable("id") Long id, Model model) {
		Catalog catalog = catalogService.getCatalogById(id);
		model.addAttribute("catalog",catalog);
		return "/userspace/catalogedit";
	}
	
}
