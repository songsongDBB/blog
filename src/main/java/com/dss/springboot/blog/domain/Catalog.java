package com.dss.springboot.blog.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 博客分类实体类
 * 
 * @author duanss
 */
@Entity
public class Catalog implements Serializable {

	private static final long serialVersionUID = 4725305485170283562L;

	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
	private Long id; // 用户的唯一标识

	@NotEmpty(message = "名称不能为空")
	@Size(min = 2, max = 30)
	@Column(nullable = false) // 映射为字段，值不能为空
	private String name;

	/**
	 * 这个表示在catalog表中有一个user_id字段，是一对一关系
	 */
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	protected Catalog() {
	}

	public Catalog(Long id) {
		this.id = id;
	}
	
	public Catalog(User user, String name) {
		this.user = user;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Catalog [id=" + id + ", name=" + name + ", user=" + user + "]";
	}

}
