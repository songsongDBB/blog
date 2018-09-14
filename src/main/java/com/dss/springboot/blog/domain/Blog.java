package com.dss.springboot.blog.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.github.rjeschke.txtmark.Processor;

/**
 * Blog实体
 * 
 * @author duanss
 */
@Entity // 这个注释，springdatajap会自动创建表
public class Blog implements Serializable {

	private static final long serialVersionUID = -2720656051996198928L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
	private Long id;

	@NotEmpty(message = "标题不能为空")
	@Size(min = 2, max = 50)
	@Column(nullable = false, length = 50) // 映射为字段，值不能为空
	private String title;

	@NotEmpty(message = "摘要不能为空")
	@Size(min = 2, max = 300)
	@Column(nullable = false) // 映射为字段，值不能为空
	private String summary;

	@Lob // 大对象，映射 MySQL 的 Long Text 类型
	@Basic(fetch = FetchType.LAZY) // 懒加载
	@NotEmpty(message = "内容不能为空")
	@Size(min = 2)
	@Column(nullable = false) // 映射为字段，值不能为空
	private String content;

	@Lob // 大对象，映射 MySQL 的 Long Text 类型
	@Basic(fetch = FetchType.LAZY) // 懒加载
	@NotEmpty(message = "内容不能为空")
	@Size(min = 2)
	@Column(nullable = false) // 映射为字段，值不能为空
	private String htmlContent; // 这个是用于在页面显示的内容，因为用户的博客可能包含一些html代码，将内容转成html

	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false) // 映射为字段，值不能为空
	@org.hibernate.annotations.CreationTimestamp // 由数据库自动创建时间
	private Timestamp createTime;

	@Column(name = "readSize")
	private Long readSize = 0L; // 访问量

	@Column(name = "commentSize")
	private Long commentSize = 0L; // 评论量

	@Column(name = "voteSize")
	private Long voteSize = 0L; // 点赞量

	// 这里表示会建立一个中间变，博客和评论的中间表，关系是一对多
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
	private List<Comment> comments;

	// 这里表示会建立一个中间变，博客和点赞的中间表，关系是一对多
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "blog_vote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"))
	private List<Vote> votes;

	// 这里会在blog表中建立一个catalog_id的字段，外键
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "catalog_id")
	private Catalog catalog;

	protected Blog() {

	}

	public Blog(Long id, String title, String summary, String content) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.htmlContent = Processor.process(content); // 把content转化成html, 使用compile('es.nitaur.markdown:txtmark:0.16')
	}

	public User getUser() {
		return user;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
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

	public Long getReadSize() {
		return readSize;
	}

	public void setReadSize(Long readSize) {
		this.readSize = readSize;
	}

	public Long getCommentSize() {
		return commentSize;
	}

	public void setCommentSize(Long commentSize) {
		this.commentSize = commentSize;
	}

	public Long getVoteSize() {
		return voteSize;
	}

	public void setVoteSize(Long voteSize) {
		this.voteSize = voteSize;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
		this.commentSize = (long) this.comments.size();
	}

	/**
	 * 添加评论 这里jpa会自动修改博客评论中间表，并且重新这是评论量
	 * 
	 * @param comment
	 */
	public void addComment(Comment comment) {
		this.comments.add(comment);
		this.commentSize = (long) this.comments.size();
	}

	/**
	 * 删除评论 这里jpa会自动修改博客评论中间表，并且重新这是评论量
	 * 
	 * @param comment
	 */
	public void removeComment(Long commentId) {
		for (int index = 0; index < this.comments.size(); index++) {
			if (comments.get(index).getId() == commentId) {
				this.comments.remove(index);
				break;
			}
		}

		this.commentSize = (long) this.comments.size();
	}

	public List<Vote> getVotes() {
		return votes;
	}

	// 意思同评论量
	public void setVotes(List<Vote> votes) {
		this.votes = votes;
		this.voteSize = (long) this.votes.size();
	}

	/**
	 * 点赞
	 * 
	 * @param vote
	 * @return
	 */
	public boolean addVote(Vote vote) {
		boolean isExist = false;

		// 判断当前点赞的这个user是否对这个博客进行过点赞操作了
		for (int index = 0; index < this.votes.size(); index++) {
			if (this.votes.get(index).getUser().getId() == vote.getUser().getId()) {
				isExist = true;
				break;
			}
		}

		if (!isExist) {
			this.votes.add(vote); // 这里add之后，jpa会往blog_vote插入一条数据
			this.voteSize = (long) this.votes.size();
		}

		return isExist;
	}

	/**
	 * 取消点赞
	 * 
	 * @param voteId
	 */
	public void removeVote(Long voteId) {
		for (int index = 0; index < this.votes.size(); index++) {
			if (this.votes.get(index).getId() == voteId) {
				this.votes.remove(index); // 这里remove之后，jpa会在blog_vote删除voteId的这个记录
				break;
			}
		}
		this.voteSize = (long) this.votes.size();
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", summary=" + summary + ", content=" + content
				+ ", htmlContent=" + htmlContent + ", user=" + user + ", createTime=" + createTime + ", readSize="
				+ readSize + ", commentSize=" + commentSize + ", voteSize=" + voteSize + ", comments=" + comments
				+ ", votes=" + votes + ", catalog=" + catalog + "]";
	}

}
