package com.study.authentication.config;


import com.study.authentication.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Securiry 安全配置
 *
 * @author hecong
 * @version 1.0
 * @date 2023-08-04 17:55:23
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserServiceImpl userService;


  /**
   * 注入用户信息服务
   *
   * @return 用户信息服务对象
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return userService;
  }


  /**
   * 全局用户信息
   *
   * @param auth 认证管理
   * @throws Exception 用户认证异常信息
   */
  @Autowired
  public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService())
        .passwordEncoder(passwordEncoder());
  }

  /**
   * 认证管理
   *
   * @return 认证管理对象
   * @throws Exception 认证异常信息
   */
  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * http安全配置
   *
   * @param http http安全对象
   * @throws Exception http安全异常信息
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS) .permitAll()
        .anyRequest().authenticated()
        .and()
//        .formLogin().loginPage("/login").permitAll() // 自定义登录，下面httpBasic便会失效。会由LoginUrlAuthenticationEntryPoint 直接返回浏览器无权限访问
//        .and()
        .httpBasic()// BasicAuthenticationEntryPoint 返回浏览器WWW-Authenticate: Basic realm ,浏览器便会自己弹出登录页面
        .and()
        .exceptionHandling()
        .and()
        .csrf().disable()
    ;
  }



  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}