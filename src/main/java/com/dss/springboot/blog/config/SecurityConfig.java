package com.dss.springboot.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security权限安全配置类(要实现Security功能必须要有这个类，来进行自定义配置)
 * 使用 Spring Security后，会开启csrf防护，因此从页面来的请求都必须携带 csrf token，否则就会被拒绝
 * 				//添加 Thymeleaf Spring Security的依赖，使用这个后，form表单提交时会自带scrf token
				compile('org.thymeleaf.extras:thymeleaf-extras-springsecurity4:3.0.2.RELEASE')
 * @author duan ss
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) 			// 启用方法安全设置
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String KEY = "duanss.com";
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean  
    public PasswordEncoder passwordEncoder() {  
        return new BCryptPasswordEncoder();   // 使用 BCrypt 加密密码
    }  

	/**
	 * 这个是相当于在这里new一个AuthenticationProvider的实例
	 * @return
	 */
	@Bean  	//这个注解是把这个对象放入spring容器中，以便可以使用 @Autowired 来自动装配
    public AuthenticationProvider authenticationProvider() {  
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder); 		// 设置密码加密方式
        return authenticationProvider;  
    }  

	
	/**
	 * 重写这个方法，实现自定义配置
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//这里先不设置拦截，都可以访问
		//http.authorizeRequests().antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll();
		
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll()	//表示静态资源，以及主页/index页面不设置拦截
				.antMatchers("/h2-console/**").permitAll()		//h2数据库控制台相关的请求都可以访问
				.antMatchers("/admins/**").hasRole("管理员") 	//后台管理的页面则需要管理员角色才能访问，因此在权限表里面，管理员角色的值必须是 ROLE_管理员
				.and().formLogin()			//Form表单的登录验证
				.loginPage("/login").failureUrl("/login-error") //登录的请求是/login, 登录失败后到/login-error ，配置这个之后，
																//不需要写登录方法，会自动去查询user表, 这里登录成功后，会自动访问项目的根目录，这里就是来到了后台管理的主页面
				.and().rememberMe().key(KEY) 		//启用记住密码功能,这里记住密码是保存用户认证信息到浏览器的cookie，需要页面有一个checkbox ，name="remember-me"
				.and().exceptionHandling().accessDeniedPage("/403"); 	//处理异常，拒绝访问就重定向到403页面
		
		http.csrf().ignoringAntMatchers("/h2-console/**");		//关闭H2数据库控制台的csrf防护
		http.headers().frameOptions().sameOrigin();			//允许来自同一来源的H2控制台的请求
	}
	
	/**
	 * 认证信息管理
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		/**
		 * 使用userDetailsService来从数据库中，把认证信息取出来，这是一个接口，因此UserServiceImpl要实现这个接口
		 */
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}


}
