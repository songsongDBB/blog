package com.dss.springboot.blog.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dss.springboot.blog.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

	/**
	 * 根据用户姓名分页查询用户列表
	 * @param name
	 * @param pageable
	 * @return
	 */
	Page<User> findByNameLike(String name, Pageable pageable);

	/**
	 * 根据用户账号查询用户
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

	/**
	 * 通过username的list获取user对象的list
	 * @param usernamelist
	 * @return
	 */
	List<User> findByUsernameIn(Collection<String> usernamelist);
}
