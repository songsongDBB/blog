package com.dss.springboot.blog.service;

import java.util.List;

import com.dss.springboot.blog.domain.Catalog;
import com.dss.springboot.blog.domain.User;

public interface CatalogService {

	/**
	 * 保存分类
	 * @param catalog
	 * @return
	 */
	Catalog saveCatalog(Catalog catalog);
	
	/**
	 * 根据id删除分类
	 * @param id
	 */
	void removeCatalog(Long id);
	
	/**
	 * 根据id查找分类
	 * @param id
	 * @return
	 */
	Catalog getCatalogById(Long id);
	
	/**
	 * 根据用户查询分类列表
	 * @param user
	 * @return
	 */
	List<Catalog> getCatalogsByUser(User user);
}
