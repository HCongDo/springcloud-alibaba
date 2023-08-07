package com.study.authentication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Oauth2.0 资源服务配置
 * @version 1.0
 * @author hecong
 * @date 2023-08-04 17:55:23
 */
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  private static final String RESOURCE_ID = "user-resource";

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources
        .resourceId(RESOURCE_ID)
        .stateless(true)
    ;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .anyRequest()
        .authenticated()
    ;
  }

}
