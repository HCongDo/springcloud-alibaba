package com.study.gateway.filters;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.study.common.entity.ResultDto;
import com.study.gateway.utils.GZIPUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.val;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 响应体转换处理
 *
 * @author heguitang
 */
@Component
public class GlobalPostFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("global filter HttpResponseBody，processing response results");
        // 这里可以增加一些业务判断条件，进行跳过处理
        String path = exchange.getRequest().getURI().getPath();
        logger.info("请求路径为：{}", path);
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        // 响应装饰
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                logger.info("global filter HttpResponseBody，Response processing，getStatusCode={}",
                        getStatusCode());
                if (getStatusCode() != null && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        // 如果响应过大，会进行截断，出现乱码，看api DefaultDataBufferFactory
                        // 有个join方法可以合并所有的流，乱码的问题解决
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer dataBuffer = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        // 释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        List<String> encodingList = exchange.getResponse().getHeaders()
                                .get(HttpHeaders.CONTENT_ENCODING);
                        boolean zip = encodingList != null && encodingList.contains("gzip");
                        // responseData就是response的值，就可查看修改了
                        String responseData = getResponseData(zip, content);
                        // 服务降级处理 重置返回参数
                        String result = responseData;
                        if (JSONUtil.isTypeJSON(responseData)) {
                            result = responseConversion(exchange, responseData);
                        }
                        byte[] uppedContent = getUppedContent(zip, result);
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private String responseConversion(ServerWebExchange exchange, String result) {
        try {
            logger.info("响应结果为：{}", result);
            val resultDto = JSONUtil.parse(result).toBean(ResultDto.class);
            if (resultDto.getStatus() != 200) {
                // 获取此次请求命中的路由，与控制台配置资源名要一致
                Route route = (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                String id = route.getId();
                Entry entry = null;
                try {
                    // 第三个参数为0，这样返回的时候不会再次累加QPS数
                    entry = SphU.entry(id, EntryType.OUT, 0);
                    Tracer.trace(new Exception("error"));
                } finally {
                    if (entry != null) {
                        entry.close();
                    }
                }
            }
            return JSONUtil.toJsonStr(resultDto);
        } catch (Exception e) {
            logger.error("响应包装转换失败，异常信息为：", e);
            return result;
        }
    }

    private String getResponseData(boolean zip, byte[] content) {
        String responseData;
        if (zip) {
            responseData = GZIPUtils.uncompressToString(content);
        } else {
            responseData = new String(content, StandardCharsets.UTF_8);
        }
        return responseData;
    }

    private byte[] getUppedContent(boolean zip, String result) {
        byte[] uppedContent;
        if (zip) {
            uppedContent = GZIPUtils.compress(result);
        } else {
            uppedContent = result.getBytes(StandardCharsets.UTF_8);
        }
        return uppedContent;
    }

}

