package com.dss.springboot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dss.springboot.blog.domain.Authority;

/**
 * 权限管理的dao层
 * @author duan ss
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
