package com.dss.springboot.blog.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dss.springboot.blog.domain.Blog;
import com.dss.springboot.blog.domain.Catalog;
import com.dss.springboot.blog.domain.Comment;
import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.domain.Vote;
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
		String tags = title;
		Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, user, tags, user, pageable);

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

	@Override
	public Blog createVote(Long blogId) {
		
		Blog blog = blogRepository.getOne(blogId);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			//从Security上下文获取当前认证的user
		Vote vote = new Vote(user);			//创建一个Vote，里面有点赞的user，为了后面jpa插入vote表数据

		boolean isExist = blog.addVote(vote);		//看这个用户是否给这个blog点赞过，如果没有，则会把该vote插入到blog中
		if(isExist) {
			throw new IllegalArgumentException("您已经为该博客点过赞了！！！");
		}
		
		return this.saveBlog(blog);		//再次保存这个blog，这里会是update操作
	}

	@Override
	public Blog removeVote(Long blogId, Long voteId) {

		Blog blog = blogRepository.getOne(blogId);
		blog.removeVote(voteId);
		return this.saveBlog(blog);
	}

	@Override
	public Page<Blog> listBlogByCatalog(Catalog catalog, Pageable pageable) {
		return blogRepository.findByCatalog(catalog, pageable);
	}

	@Override
	public boolean isExsitBlogByCatalogId(Long catalogId, User user) {
		
		Catalog catalog = new Catalog(catalogId);
		catalog.setUser(user);
		
		Page<Blog> page = blogRepository.findByCatalog(catalog, null);
		List<Blog> blogs = page.getContent();
		if(blogs != null && blogs.size() > 0) {
			return true;
		}
		
		return false;
	}

}
