package com.dss.springboot.blog.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dss.springboot.blog.domain.Blog;
import com.dss.springboot.blog.domain.Comment;
import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.repository.BlogRepository;
import com.dss.springboot.blog.service.BlogService;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;
	
	@Transactional
	@Override
	public Blog saveBlog(Blog blog) {
		Blog returnBlog = blogRepository.save(blog);
		return returnBlog;
	}

	@Transactional
	@Override
	public void removeBlog(Long id) {
		blogRepository.deleteById(id);
	}

	@Override
	public Blog updateBlog(Blog blog) {
		return blogRepository.save(blog);
	}

	@Override
	public Blog getBlogById(Long id) {
		return blogRepository.getOne(id);
	}

	@Override
	public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {

		title = "%" + title + "%";
		Page<Blog> blogs = blogRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
		return blogs;
	}

	@Override
	public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {

		title = "%" + title + "%";
		Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
		return blogs;
	}

	@Override
	public void readingIncrease(Long id) {
		
		Blog blog = blogRepository.getOne(id);
		blog.setReadSize(blog.getReadSize()+1);
		this.saveBlog(blog);
	}

	@Override
	public Blog createComment(Long blogId, String commentContent) {
		
		Blog blog = blogRepository.getOne(blogId);			//先找到这个blog
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			//从Security上下文获取当前认证的user
		Comment comment = new Comment(user, commentContent);
		blog.addComment(comment);			//这个评论添加到这个blog中
		
		return this.saveBlog(blog);
	}

	@Override
	public Blog removeComment(Long blogId, Long commentId) {

		Blog blog = blogRepository.getOne(blogId);			//先找到这个blog
		blog.removeComment(commentId);
		
		return this.saveBlog(blog);
	}

}
