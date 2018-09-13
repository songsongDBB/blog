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

/**
 * 点赞管理类
 * 
 * @author duanss
 */
@Entity
public class Vote implements Serializable {

	private static final long serialVersionUID = 4725305485170283562L;

	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
	private Long id; // 用户的唯一标识

	/**
	 * 这个表示在vote表中有一个user_id字段，是一对一关系
	 */
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false) // 映射为字段，值不能为空
	@org.hibernate.annotations.CreationTimestamp // 由数据库自动创建时间
	private Timestamp createTime;

	protected Vote() {
	}

	public Vote(User user) {
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Vote [id=" + id + ", user=" + user + ", createTime=" + createTime + "]";
	}

}
