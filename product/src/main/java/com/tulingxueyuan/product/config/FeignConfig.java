package com.tulingxueyuan.product.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

//@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }


    //切换为Feign原生的注解
//    @Bean
//    public Contract feignContract() {
//        return new Contract.Default();
//    }


    /**
     * 超时时间配置
     @Bean
     public Request.Options options() {
     return new Request.Options(5000, 10000);
     }
     */

    /**
     * 自定义拦截器
     * @return
     @Bean
     public FeignAuthRequestInterceptor feignAuthRequestInterceptor(){
     return new FeignAuthRequestInterceptor();
     }
     */

}
