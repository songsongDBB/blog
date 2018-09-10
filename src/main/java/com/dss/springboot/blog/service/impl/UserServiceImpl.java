package com.dss.springboot.blog.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dss.springboot.blog.domain.User;
import com.dss.springboot.blog.repository.UserRepository;
import com.dss.springboot.blog.service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public User saveOrUpdateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void removeUser(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	@Transactional
	public User getUserById(Long id) {
		return userRepository.getOne(id);
	}

	@Override
	@Transactional
	public Page<User> listUserByNameList(String name, Pageable pageable) {
		name = "%" + name + "%";
		return userRepository.findByNameLike(name, pageable);
	}

	@Override
	@Transactional
	public User registerUser(User user) {
		return userRepository.save(user);
	}

	/**
	 * UserDetailsService的方法：根据用户逇username来获取用户的认证信息
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

}
