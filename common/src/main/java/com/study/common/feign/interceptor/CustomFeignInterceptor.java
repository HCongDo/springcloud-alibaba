package com.study.common.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * nacos order-service 配置文件中将此文件配置为了 feign 拦截器
 * @author hecong
 * @version 1.0
 * @date 2023/7/11 13:23
 */
public class CustomFeignInterceptor implements RequestInterceptor {

    Logger logger= LoggerFactory.getLogger(this.getClass());

    public void apply(RequestTemplate requestTemplate) {
        // Todo 请求发生前做处理
        requestTemplate.header("xxx","xxx");
        requestTemplate.query("id","111");
        requestTemplate.uri("/9");
        logger.info("feign拦截器！");
    }
}