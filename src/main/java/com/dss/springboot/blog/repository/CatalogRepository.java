package com.dss.springboot.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dss.springboot.blog.domain.Catalog;
import com.dss.springboot.blog.domain.User;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

	/**
	 * 根据用户查询分类列表
	 * @param user
	 * @return
	 */
	List<Catalog> findByUser(User user);
	
	/**
	 * 根据用户和分类名称查询分类列表
	 * @param user
	 * @param name
	 * @return
	 */
	List<Catalog> findByUserAndName(User user, String name);
}
