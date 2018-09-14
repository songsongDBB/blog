package com.dss.springboot.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dss.springboot.blog.domain.Blog;
import com.dss.springboot.blog.domain.Catalog;
import com.dss.springboot.blog.domain.User;

/**
 * Blog的资源库
 * @author duan ss
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {

	/**
	 * 根据博客标题，博客标签，用户查询，可模糊查询，并且通过创建时间进行降序排序
	 * @param user
	 * @param title
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title, User user, String tags, User user1, Pageable pageable);
	
	/**
	 * 根据用户名，博客标题分页查询博客列表，可模糊查询
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

	/**
	 * 根据分类查询博客列表
	 * @param catalog
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
}
