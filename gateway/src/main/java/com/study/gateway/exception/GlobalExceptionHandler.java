package com.study.gateway.exception;

import com.alibaba.csp.sentinel.Tracer;
import com.study.common.entity.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 网关全局异常处理类
 * 只能处理本模块代码异常，不包含路由到子服务的异常
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    Logger logger=   LoggerFactory.getLogger(this.getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 全局异常捕捉处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResultDto errorHandler(Exception e) {
        logger.error("程序异常, 详细信息为:{}", e.getLocalizedMessage() , e);
        return ResultDto.error(applicationName,500,"不好意思，程序出错了，请稍后再试",e.getMessage());
    }


}
