package com.dss.springboot.blog.vo;

import java.io.Serializable;

import com.dss.springboot.blog.domain.Catalog;

/**
 * 用户传递页面和后台的参数类
 * 
 * @author duan ss
 *
 */
public class CatalogVO implements Serializable {

	private static final long serialVersionUID = -2013051054074043027L;

	private String username; // 用户名
	private Catalog catalog; // 分类实体

	public String getUsername() {
		return username;
	}

	public CatalogVO() {
	}

	public CatalogVO(String username, Catalog catalog) {
		super();
		this.username = username;
		this.catalog = catalog;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

}
