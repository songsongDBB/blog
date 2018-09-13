package com.dss.springboot.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dss.springboot.blog.domain.Comment;
import com.dss.springboot.blog.repository.CommentRepository;
import com.dss.springboot.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public Comment getCommentById(Long id) {
		return commentRepository.getOne(id);
	}

	@Override
	public void removeCommentById(Long id) {
		commentRepository.deleteById(id);
	}

}
