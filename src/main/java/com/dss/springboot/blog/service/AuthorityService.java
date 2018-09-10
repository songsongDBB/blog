package com.dss.springboot.blog.service;

import com.dss.springboot.blog.domain.Authority;

public interface AuthorityService {

	/**
	 * 通过权限id获取Authority对象
	 * @param id
	 * @return
	 */
	Authority getAuthorityById(Long id);
}
