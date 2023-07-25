package com.study.gateway.exception;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.study.common.entity.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 *  sentinel 限流、熔断降级相关异常处理器
 *  此处有个坑：sentinel dashboard 中配置降级选择异常数，子服务500是不会触发，只有子服务宕机才有效
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:25
 */
@Configuration
public class GatewaySentinelExceptionConfig {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init(){
         BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
             @Override
             public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable e) {
                 ResultDto resultDto = new ResultDto();
              // 标准错误编码可参考 resultDto.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                 if (e instanceof FlowException || e instanceof ParamFlowException) {
                     resultDto = ResultDto.error(100,"接口限流了");
                 } else if (e instanceof DegradeException) {
                     resultDto = ResultDto.error(101,"服务降级了");
                 } else if (e instanceof SystemBlockException) {
                     resultDto = ResultDto.error(103,"触发系统保护规则了");
                 } else if (e instanceof AuthorityException) {
                     resultDto = ResultDto.error(104,"授权规则不通过");
                 }
                 logger.error("sentinel 报错信息: ");
                 e.printStackTrace();
                 return ServerResponse.status(HttpStatus.OK)
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(BodyInserters.fromValue(resultDto));
             }
         };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
    
}
