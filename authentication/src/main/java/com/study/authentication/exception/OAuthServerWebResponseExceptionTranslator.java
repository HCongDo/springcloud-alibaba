package com.study.authentication.exception;

import com.study.common.entity.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 *  oauth2 全局异常处理器
 * @Description: TODO
 * @Author hecong
 * @Date 2023/8/4 15:02
 * @Version 1.0
 */
public class OAuthServerWebResponseExceptionTranslator implements
    WebResponseExceptionTranslator {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public ResponseEntity translate(Exception e) throws Exception {
    logger.error("oauth server 原始错误信息为：{}", e.getMessage());
    return new ResponseEntity<>(ResultDto.error(401,e.getMessage()), HttpStatus.UNAUTHORIZED);
  }
}
