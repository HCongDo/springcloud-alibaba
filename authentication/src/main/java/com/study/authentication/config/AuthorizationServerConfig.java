package com.study.authentication.config;

import com.study.authentication.utils.MyJwtAccessTokenConverter;
import com.study.common.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Oauth2.0 授权服务器配置
 *
 * @author He PanFu
 * @date 2021-12-19 17:55:23
 */
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;

    /**
     *  JWT Token 增强器
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
     * 定义存储和检索访问令牌的方法
     * 用于管理访问令牌和刷新令牌的持久化存储
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        //  return new InMemoryTokenStore();
        //  return new JdbcTokenStore(dataSource)
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 定义了用于处理访问令牌的方法
     * 从TokenStore中获取令牌，并进行令牌的验证和处理
     * 如loadAuthentication(String accessToken)用于加载与访问令牌相关联的认证信息
     * 以及refreshAccessToken(String refreshToken,TokenRequest tokenRequest)用于刷新访问令牌等
     * 具体的实现类如DefaultTokenServices，提供了对这些方法的具体实现
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
//        return new InMemoryAuthorizationCodeServices();
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
        //  暂时使用in‐memory存储
//        clients.inMemory()
//                // 客户端标识
//                .withClient("c1")
//                // 客户端秘钥
//                .secret(passwordEncoder.encode("secret"))
//                // 资源列表
//                .resourceIds("user-resource")
//                // 允许授权的五种类型
//                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
//                // 允许的授权范围
//                .scopes("all")
//                // false=跳转到授权页面，true=直接方法令牌
//                .autoApprove(false)
//                // 加上验证回调地址
//                .redirectUris("http://www.baidu.com");;
        clients.jdbc(dataSource);
    }


    /**
     * 管理令牌：用来配置令牌（token）的访问端点和令牌服务(token services)。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 密码模式
        endpoints.authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices())     // 授权码模式code相关信息
                .tokenStore(tokenStore()) // 令牌存储于搜索
                .tokenServices(tokenService()) // token的创建
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST); // 端点支持的请求类型
    }

    /**
     * 用来配置令牌端点的安全约束
     * 拦截控制
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
        // token_key公开
        security.tokenKeyAccess("permitAll()") // 允许公开访问令牌端点
                .checkTokenAccess("permitAll()")// 需要身份验证来检查令牌
                // 允许表单身份验证客户端 - 新增ClientCredentialsTokenEndpointFilter拦截器获取参数客户端信息创建Auth信息，这样就不会被Security 拦截器拦截了
                .allowFormAuthenticationForClients()
        ;
//         clientCredentialsTokenEndpointFilter(security);
//         security.addTokenEndpointAuthenticationFilter(clientCredentialsTokenEndpointFilter(null));
    }
}
