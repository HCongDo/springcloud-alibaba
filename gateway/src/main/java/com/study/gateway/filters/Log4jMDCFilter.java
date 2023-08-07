package com.study.gateway.filters;

import com.study.common.utils.TraceIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;


/**
 * 网关MDC日志拦截器
 *
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 11:43
 */
@Component
public class Log4jMDCFilter implements GlobalFilter, Ordered {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        try {
            String traceId = UUID.randomUUID().toString();
            TraceIdUtil.setTraceId(traceId);
            // TODO 定义新的消息头,子模块可自行转换
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove(TraceIdUtil.TRACE_ID);
            headers.set(TraceIdUtil.TRACE_ID, traceId);
            request = new ServerHttpRequestDecorator(request) {
                @Override
                public HttpHeaders getHeaders() {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(headers);
                    return httpHeaders;
                }
            };
            logger.info("traceId:{}", traceId);
            return chain.filter(exchange.mutate().request(request).build());
        } catch (Exception e) {
            logger.error("网关MDC日志拦截器失败");
        }
        return chain.filter(exchange.mutate().request(exchange.getRequest()).build());
    }


    @Override
    public int getOrder() {
        return 100;
    }

}
