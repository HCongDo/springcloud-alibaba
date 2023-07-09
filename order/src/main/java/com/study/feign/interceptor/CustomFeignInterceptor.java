package com.study.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
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
