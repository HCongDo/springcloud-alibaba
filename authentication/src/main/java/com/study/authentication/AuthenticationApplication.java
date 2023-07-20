package com.study.authentication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:15
 */
@SpringBootApplication
@EnableAuthorizationServer
@MapperScan("com.study.authentication.mapper")
public class AuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class,args);
    }
}
