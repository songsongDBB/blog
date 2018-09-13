package com.dss.springboot.blog.service;

import com.dss.springboot.blog.domain.Comment;

public interface CommentService {

	/**
	 * 根据id获取评论
	 * @param id
	 * @return
	 */
	Comment getCommentById(Long id);
	
	/**
	 * 根据id删除评论
	 * @param id
	 */
	void removeCommentById(Long id);
}
