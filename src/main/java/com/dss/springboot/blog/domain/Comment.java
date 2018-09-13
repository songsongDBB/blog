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

@Entity
public class Comment implements Serializable {

	private static final long serialVersionUID = -5525447532209025584L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
	private Long id;

	@NotEmpty(message = "评论内容不能为空")
	@Size(min = 2, max = 500)
	@Column(nullable = false) // 映射为字段，值不能为空
	private String content;

	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY) // 这个表示在comment表中有一个user_id字段，是一对一关系
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false) // 映射为字段，值不能为空
	@org.hibernate.annotations.CreationTimestamp // 由数据库自动创建时间
	private Timestamp createTime;

	protected Comment() {

	}

	public Comment(User user, String content) {
		this.user = user;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		return "Comment [id=" + id + ", content=" + content + ", user=" + user + ", createTime=" + createTime + "]";
	}

}
