package com.dss.springboot.blog.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security权限安全配置类(要实现Security功能必须要有这个类，来进行自定义配置)
 * @author duan ss
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 重写这个方法，实现自定义配置
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//这里先不设置拦截，都可以访问
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll();
	}

}
