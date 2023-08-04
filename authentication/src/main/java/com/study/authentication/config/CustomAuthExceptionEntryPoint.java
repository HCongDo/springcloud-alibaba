package com.study.authentication.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: TODO
 * @Author hecong
 * @Date 2023/8/4 22:01
 * @Version 1.0
 */
//@Component
public class CustomAuthExceptionEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    // 重定向到登录页面
    response.sendRedirect("/login");
  }
}
