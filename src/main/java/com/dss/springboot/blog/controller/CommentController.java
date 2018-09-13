package com.dss.springboot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dss.springboot.blog.domain.Blog;
import com.dss.springboot.blog.domain.Comment;
import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.service.BlogService;
import com.dss.springboot.blog.service.CommentService;
import com.dss.springboot.blog.utils.ConstraintViolationExceptionHandler;
import com.dss.springboot.blog.vo.Response;

/**
 * 评论管理控制器
 * @author duan ss
 */
@Controller
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CommentService commentService;
	
	/**
	 * 通过blog的id获取这个blog的所有评论
	 * @param blogId
	 * @param model
	 * @return
	 */
	@GetMapping
	public String listCommentsByBlogId(@RequestParam(value="blogId", required=true) Long blogId, Model model) {
		
		Blog blog = blogService.getBlogById(blogId);
		List<Comment> comments = blog.getComments();
		
		//得到当前用户的username
		String commentgOwner = "";
		//这里就是获取当前登录的用户
		if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal != null ) {
				commentgOwner = principal.getUsername();
			}
		}
		
		model.addAttribute("commentgOwner", commentgOwner);
		model.addAttribute("comments", comments);
		
		return "/userspace/blog :: #mainContainerRepleace";
	}
	
	/**
	 * 发表评论
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_管理员','ROLE_博主')") 			//要求用户必须分配了角色才能发表评论
	public ResponseEntity<Response> createComment(Long blogId, String commentContent){
		
		try {
			blogService.createComment(blogId, commentContent);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "操作成功", null));
		
	}
	
	/**
	 * 删除评论，通过评论id
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_管理员','ROLE_博主')") 			//要求用户必须分配了角色才能删除评论
	public ResponseEntity<Response> createComment(@PathVariable("id") Long id, Long blogId){
		
		boolean isOwner = false;
		User user = commentService.getCommentById(id).getUser();			//找到这个评论是那个user发布的
		
		//判断这个操作用户是否这个评论的所有者
		if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal != null ) {
				isOwner = true;
			}
		}
		
		if (!isOwner) {
			return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
		}
		
		try {
			
			blogService.removeComment(blogId, id);
			commentService.removeCommentById(id);
			
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "操作成功", null));

		
	}
}
