package com.dss.springboot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dss.springboot.blog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
