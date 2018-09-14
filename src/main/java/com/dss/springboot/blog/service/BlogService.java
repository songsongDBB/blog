package com.dss.springboot.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dss.springboot.blog.domain.Blog;
import com.dss.springboot.blog.domain.Catalog;
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
	
	/**
	 * 点赞，插入点赞信息和点赞数量
	 * @param blogId
	 * @return
	 */
	Blog createVote(Long blogId);
	
	/**
	 * 取消点赞，删除点赞信息和修改点赞数量
	 * @param blogId
	 * @param voteId
	 * @return
	 */
	Blog removeVote(Long blogId, Long voteId);
	
	/**
	 * 根据分类查询博客列表
	 * @param catalog
	 * @param pageable
	 * @return
	 */
	Page<Blog> listBlogByCatalog(Catalog catalog, Pageable pageable);
	
	/**
	 * 判断当前catalogId先，这个user有没有博客
	 * @param catalogId
	 * @return
	 */
	boolean isExsitBlogByCatalogId(Long catalogId, User user);
}
