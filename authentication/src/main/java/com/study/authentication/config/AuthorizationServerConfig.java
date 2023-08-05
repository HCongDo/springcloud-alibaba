package com.study.authentication.config;

import com.study.authentication.exception.OAuthServerWebResponseExceptionTranslator;
import com.study.authentication.mapper.UserInfoMapper;
import com.study.authentication.granter.SmsCodeTokenGranter;
import com.study.authentication.utils.MyJwtAccessTokenConverter;
import com.study.common.utils.RSAUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Oauth2.0 授权服务器配置
 *
 * @author hecong
 * @version 1.0
 * @date 2023-08-04 17:55:23
 */
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private DataSource dataSource;
  @Autowired
  private UserInfoMapper userInfoMapper;


  /**
   * JWT Token 增强器
   *
   * @return JwtAccessTokenConverter
   */
  @Bean
  protected JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter converter = new MyJwtAccessTokenConverter();
    converter.setKeyPair(RSAUtil.GetKeyPair());
    return converter;
  }

  /**
   * 定义存储和检索访问令牌的方法 用于管理访问令牌和刷新令牌的持久化存储
   *
   * @return TokenStore
   */
  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(jwtAccessTokenConverter());
  }


  /**
   * 定义了用于处理访问令牌的方法 从TokenStore中获取令牌，并进行令牌的验证和处理 如loadAuthentication(String
   * accessToken)用于加载与访问令牌相关联的认证信息 以及refreshAccessToken(String refreshToken,TokenRequest
   * tokenRequest)用于刷新访问令牌等 具体的实现类如DefaultTokenServices，提供了对这些方法的具体实现
   *
   * @return AuthorizationServerTokenServices
   */
  @Bean
  public AuthorizationServerTokenServices tokenService() {
    DefaultTokenServices service = new DefaultTokenServices();
    // 是否刷新令牌
    service.setSupportRefreshToken(true);
    // 令牌存储策略
    service.setTokenStore(tokenStore());
    // 令牌默认有效期2小时
    service.setAccessTokenValiditySeconds(7200);
    // 刷新令牌默认有效期3天
    service.setRefreshTokenValiditySeconds(259200);
    // token 增强器
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter()));
    service.setTokenEnhancer(tokenEnhancerChain);
    return service;
  }

  /**
   * 用于管理授权码（Authorization Code）的生成、存储和删除等操作
   *
   * @return AuthorizationCodeServices
   */
  @Bean
  public AuthorizationCodeServices authorizationCodeServices() {
    //设置授权码模式的授权码如何存取，暂时采用内存方式
    return new JdbcAuthorizationCodeServices(dataSource);
  }

  /**
   * 配置客户端详细信息
   *
   * @param clients
   * @throws Exception
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource);
  }


  /**
   * 添加自定义授权类型
   *
   * @return List<TokenGranter>
   */
  private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
    //获取SpringSecurity OAuth2.0 现有的授权类型
    List<TokenGranter> granters = new ArrayList<TokenGranter>(
        Collections.singletonList(endpoints.getTokenGranter()));
    //短信登录
    SmsCodeTokenGranter smsCodeTokenGranter = new SmsCodeTokenGranter(endpoints.getTokenServices(),
        endpoints.getClientDetailsService(),
        endpoints.getOAuth2RequestFactory(), userInfoMapper);
    granters.add(smsCodeTokenGranter);
    return new CompositeTokenGranter(granters);
  }

  @Bean
  public OAuthServerWebResponseExceptionTranslator webResponseExceptionTranslator(){
    return  new OAuthServerWebResponseExceptionTranslator();
  }

  /**
   * 管理令牌：用来配置令牌（token）的访问端点和令牌服务(token services)。
   *
   * @param endpoints
   * @throws Exception
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager)
        .authorizationCodeServices(authorizationCodeServices()) // 授权码模式code相关信息
        .tokenStore(tokenStore()) // 令牌存储于搜索
        .tokenServices(tokenService()) // token的创建
        .tokenGranter(tokenGranter(endpoints))// 新加短信登录与二维码登录
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
        .exceptionTranslator(webResponseExceptionTranslator())
    ;

  }

  /**
   * 用来配置令牌端点的安全约束 拦截控制
   *
   * @param security
   * @throws Exception
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    /*
     * /oauth/authorize：授权端点。
     * /oauth/token：令牌端点。
     * /oauth/confirm_access：用户确认授权提交端点。
     * /oauth/error：授权服务错误信息端点。
     * /oauth/check_token：用于资源服务访问的令牌解析端点。
     * /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
     */
    security.tokenKeyAccess("permitAll()") // 允许公开访问令牌端点
        .checkTokenAccess("permitAll()")// 需要身份验证来检查令牌
        // 允许表单身份验证客户端 - 新增ClientCredentialsTokenEndpointFilter拦截器获取参数客户端信息创建Auth信息，这样就不会被Security 拦截器拦截了
        .allowFormAuthenticationForClients()
    ;
 }
}