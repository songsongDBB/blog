package com.dss.springboot.blog.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.persistence.JoinColumn;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



/**
 * User实体
 * 
 * @author duan ss
 *
 */
@Entity // 实体
public class User implements UserDetails {			// security用户权限要求必须实现UserDetails接口

	private static final long serialVersionUID = 1L;

	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
	private Long id; // 用户的唯一标识

	//Bean验证
	@NotEmpty(message = "姓名不能为空")
	@Size(min=2, max=20)
	@Column(nullable = false, length = 20) // 映射为字段，值不能为空
	private String name;
	
	@NotEmpty(message = "邮箱不能为空")
	@Size(max=50)
	@Email(message= "邮箱格式不对" ) 
	@Column(nullable = false, length = 50, unique = true)
	private String email;
	
	@NotEmpty(message = "账号不能为空")
	@Size(min=3, max=20)
	@Column(nullable = false, length = 20, unique = true)
	private String username;
	
	@NotEmpty(message = "密码不能为空")
	@Size(max=100)
	@Column(length = 100)
	private String password;
	
	@Column(length = 200)
	private String avatar; // 用户头像信息

	/**
	 * 定义一个权限list，因为用户和权限角色是多对多的关系型
	 * 这里是表示在项目启动时，要建立一个user和authority的中间表，表名是user_authority，列名user_id对应user表中的id，并且有外键，列名authority_id对应authority表中的id，并且有外键
	 */
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities;
	
	protected User() { // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
	}

	public User(String name, String email, String username, String avatar) {
		super();
		this.name = name;
		this.email = email;
		this.username = username;
		this.avatar = avatar;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", username=" + username + ", password="
				+ password + ", avatar=" + avatar + "]";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		//  需将 List<Authority> 转成 List<SimpleGrantedAuthority>，否则前端拿不到角色列表名称
		List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
		for(GrantedAuthority authority : this.authorities){
			simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		}
		
		return simpleAuthorities;
	}
	
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * 这个方法设置返回值为true
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 这个方法设置返回值为true
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 这个方法设置返回值为true
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 这个方法设置返回值为true
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}
