package com.dss.springboot.blog.vo;

/**
 * 后台管理员管理的菜单
 * 
 * @author duan ss
 */
public class Menu {

	private String name;
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Menu(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public Menu() {
		super();
	}

}
