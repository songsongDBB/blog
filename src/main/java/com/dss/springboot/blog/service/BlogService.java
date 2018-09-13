package com.dss.springboot.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dss.springboot.blog.domain.Blog;
import com.dss.springboot.blog.domain.User;

public interface BlogService {

	/**
	 * 保存Blog
	 * 
	 * @param Blog
	 * @return
	 */
	Blog saveBlog(Blog blog);

	/**
	 * 删除Blog
	 * 
	 * @param id
	 * @return
	 */
	void removeBlog(Long id);

	/**
	 * 更新Blog
	 * 
	 * @param Blog
	 * @return
	 */
	Blog updateBlog(Blog blog);

	/**
	 * 根据id获取Blog
	 * 
	 * @param id
	 * @return
	 */
	Blog getBlogById(Long id);

	/**
	 * 根据用户名进行分页模糊查询（最新）
	 * 
	 * @param user
	 * @return
	 */
	Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable);

	/**
	 * 根据用户名进行分页模糊查询（最热）
	 * 
	 * @param user
	 * @return
	 */
	Page<Blog> listBlogsByTitleVoteAndSort(User suser, String title, Pageable pageable);

	/**
	 * 阅读量递增
	 * 
	 * @param id
	 */
	void readingIncrease(Long id);
	
	/**
	 * 发表评论，返回这个blog的内容，这里面就包括所有的评论
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	Blog createComment(Long blogId, String commentContent);

	/**
	 * 删除一个博客的某一条评论
	 * @param blogId
	 * @param commentId
	 */
	Blog removeComment(Long blogId, Long commentId);
}
