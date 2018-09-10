package com.dss.springboot.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dss.springboot.blog.domain.Authority;
import com.dss.springboot.blog.repository.AuthorityRepository;
import com.dss.springboot.blog.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;
	
	/**
	 * 通过权限id获取Authority对象
	 */
	@Override
	public Authority getAuthorityById(Long id) {
		return authorityRepository.getOne(id);
	}

}
