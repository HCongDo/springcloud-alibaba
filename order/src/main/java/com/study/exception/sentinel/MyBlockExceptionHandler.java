package com.study.exception.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.common.entity.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

//@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {

    Logger logger= LoggerFactory.getLogger(this.getClass());

    public void handle(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, BlockException e) throws Exception {
         // getRule() 资源  规则的详细信息
        logger.info("BlockExceptionHandler BlockException================"+e.getRule());
        ResultDto resultDto = null;
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
        //返回json数据
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), resultDto);
    }
}
