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
import com.dss.springboot.blog.service.VoteService;
import com.dss.springboot.blog.utils.ConstraintViolationExceptionHandler;
import com.dss.springboot.blog.vo.Response;

/**
 * 评论管理控制器
 * @author duan ss
 */
@Controller
@RequestMapping("/votes")
public class VoteController {

	@Autowired
	private BlogService blogService;
	
	@Autowired
	private VoteService voteService;
	
	
	/**
	 * 发表点赞
	 * @param blogId
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_管理员','ROLE_博主')") 			//要求用户必须分配了角色才能点赞
	public ResponseEntity<Response> createVote(Long blogId){
		
		try {
			blogService.createVote(blogId);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
		
	}
	
	/**
	 * 取消点赞
	 * @param id
	 * @param blogId
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_管理员','ROLE_博主')") 			//要求用户必须分配了角色才能取消点赞
	public ResponseEntity<Response> createComment(@PathVariable("id") Long id, Long blogId){
		
		boolean isOwner = false;
		User user = voteService.getVoteById(id).getUser();			//找到这个点赞是那个user发布的
		
		//判断这个操作用户是否这个点赞的所有者
		if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal != null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			}
		}
		
		if (!isOwner) {
			return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
		}
		
		try {
			
			blogService.removeVote(blogId, id);
			voteService.removeVoteById(id);
			
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));

		
	}
}
