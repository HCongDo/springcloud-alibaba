package com.study.common.config;

import com.alibaba.fastjson.JSONObject;
import com.study.common.entity.JwtInfo;
import com.study.common.entity.UserContextHolder;
import com.study.common.utils.TraceIdUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * 远程调用透传令牌信息
 *
 * @author hecong
 * @version 1.0
 * @date 2023/8/5 17:34
 */
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        JwtInfo jwtInfo = UserContextHolder.getInstance().getContext();
        if (jwtInfo != null) {
            requestTemplate.header("x-client-token-user", JSONObject.toJSONString(jwtInfo));
            requestTemplate.header(TraceIdUtil.TRACE_ID, TraceIdUtil.getTraceId());
        }
    }
}


