package com.study.gateway.filters;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.study.common.entity.ResultDto;
import com.study.common.utils.RSAUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.sun.scenario.Settings.set;
import static java.security.KeyRep.Type.SECRET;
import static org.bouncycastle.crypto.tls.SignatureAlgorithm.rsa;


/**
 * 网关全局安全校验
 *
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 11:43
 */
@Component
public class JwtTokenFilter implements GlobalFilter, Ordered {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  /*
   * /oauth/authorize：授权端点。
   * /oauth/token：令牌端点。
   * /oauth/confirm_access：用户确认授权提交端点。
   * /oauth/error：授权服务错误信息端点。
   * /oauth/check_token：用于资源服务访问的令牌解析端点。
   * /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
   */
  private String[] skipAuthUrls = {
      "/oauth/authorize",
      "/oauth/token",
      "/oauth/confirm_access",
      "/oauth/error",
      "/oauth/check_token",
      "/oauth/token_key"
  };

  /**
   * 鉴权实现
   * 1.首先网关检查token是否有效，无效直接返回401，不调用签权服务
   * 2.调用签权服务器看是否对该请求有权限，有权限进入下一个filter，没有权限返回401
   * @param exchange
   * @param chain
   * @return
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String url = exchange.getRequest().getURI().getPath();
    logger.info("请求路径为：{}",url);
    try {
      // TODO 跳过不需要验证的路径
      if (null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(url)) {
        return chain.filter(exchange);
      }
      String token =  exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      if(StrUtil.isBlank(token)){
        List<String> accessTokens = exchange.getRequest().getQueryParams().get("access_token");
        Assert.isTrue(CollUtil.isNotEmpty(accessTokens), "权限验证失败，请先登录");
        token = accessTokens.get(0);
      }
      Claims jwt = getTokenBody(token);
      logger.info("token解密为：{}",jwt.toString());
      ServerHttpRequest oldRequest = exchange.getRequest();
      URI uri = oldRequest.getURI();
      ServerHttpRequest newRequest = oldRequest.mutate().uri(uri).build();
      // TODO 定义新的消息头,子模块可自行转换
      HttpHeaders headers = new HttpHeaders();
      headers.putAll(exchange.getRequest().getHeaders());
      headers.remove("Authorization");
      headers.set("x-client-token-user", JSONObject.toJSONString(jwt));
      newRequest = new ServerHttpRequestDecorator(newRequest) {
        @Override
        public HttpHeaders getHeaders() {
          HttpHeaders httpHeaders = new HttpHeaders();
          httpHeaders.putAll(headers);
          return httpHeaders;
        }
      };
      return chain.filter(exchange.mutate().request(newRequest).build());
    }catch (Exception e){
      logger.error("Token验签失败，具体原因为：{}",e.getMessage());
      return returnAuthFail(exchange, "验签失败,请重新登录再试");
    }
  }

  /**
   * 返回校验失败
   *
   * @param exchange
   * @return
   */
  private Mono<Void> returnAuthFail(ServerWebExchange exchange, String message) {
    ServerHttpResponse serverHttpResponse = exchange.getResponse();
    serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
    byte[] bytes = ResultDto.error(401,message).toString().getBytes(StandardCharsets.UTF_8);
    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
    return exchange.getResponse().writeWith(Flux.just(buffer));
  }

  /**
   *  jwt 解析
   * @param token
   * @return
   */
  private static Claims getTokenBody(String token) {
    return Jwts.parser()
        .setSigningKey(RSAUtil.getPublicKey())
        .parseClaimsJws(token)
        .getBody();
  }

  @Override
  public int getOrder() {
    return 100;
  }

}
