package com.study.gateway.config;

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
import java.util.HashMap;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Configuration
public class GatewayConfig {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init(){
         BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
             @Override
             public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable e) {
                 ResultDto resultDto = new ResultDto();
                 if (e instanceof FlowException) {
                     resultDto = ResultDto.error(100,"接口限流了");
                 } else if (e instanceof DegradeException) {
                     resultDto = ResultDto.error(101,"服务降级了");
                 } else if (e instanceof ParamFlowException) {
                     resultDto = ResultDto.error(102,"热点参数限流了");
                 } else if (e instanceof SystemBlockException) {
                     resultDto = ResultDto.error(103,"触发系统保护规则了");
                 } else if (e instanceof AuthorityException) {
                     resultDto = ResultDto.error(104,"授权规则不通过");
                 }
                 logger.error(e.getMessage());
//                 resultDto.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                 // 自定义异常处理
                 return ServerResponse.status(HttpStatus.OK)
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(BodyInserters.fromValue(resultDto));
             }
         };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
    
}
