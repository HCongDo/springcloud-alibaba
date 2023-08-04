package com.study.product.filter;

import com.alibaba.fastjson.JSON;
import com.study.common.entity.JwtInfo;
import com.study.common.entity.UserContextHolder;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *  用户上下文信息
 * @Description:  处理gateway 解析后的用户信息
 * @Author hecong
 * @Date 2023/8/4 19:29
 * @Version 1.0
 */

@Order(-1000)
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  protected void doFilterInternal(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, FilterChain filterChain)
      throws ServletException, IOException {
    String userToken = httpServletRequest.getHeader("x-client-token-user");
    if (userToken != null) {
      UserContextHolder.getInstance().setContext(JSON.parseObject(userToken, JwtInfo.class));
    } else {
      UserContextHolder.getInstance().setContext(null);
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}

