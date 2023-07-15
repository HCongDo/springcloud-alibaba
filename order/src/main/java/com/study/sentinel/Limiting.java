package com.study.sentinel;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.study.common.entity.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentinel")
public class Limiting {

    Logger logger= LoggerFactory.getLogger(this.getClass());

    // sentinel dashboard 直接 - QPS - 限流 2
    @RequestMapping("/directQps")
    @SentinelResource(value = "directQps",blockHandler = "directBlockHandler",fallback = "directQpsfallback")
    @ResponseBody
    public ResultDto directQps(){
        logger.info("info-直接限流测试");
        logger.warn("warn-直接限流测试");
        logger.error("error-直接限流测试");
        int i = 1/0;
        return ResultDto.success();
    }
    public ResultDto directBlockHandler(BlockException blockException){
        return ResultDto.error(501,"QPS限流 2");
    }
    public ResultDto directQpsfallback(Throwable e){
        return  ResultDto.error(500,"QPS限流 2 异常了");
    }

    // sentinel dashboard 关联 - QPS - 限流 2
    @RequestMapping("/relatedQps")
    @SentinelResource(value = "relatedQps",blockHandler = "relatedQpsBlockHandler",fallback = "relatedQpsfallback")
    @ResponseBody
    public ResultDto relatedQps(){
        return ResultDto.success();
    }
    public ResultDto relatedQpsBlockHandler(BlockException blockException){
        return ResultDto.error(501,"QPS限流 2");
    }
    public ResultDto relatedQpsfallback(Throwable e){
        return  ResultDto.error(500,"QPS限流 2 异常了");
    }

    // sentinel dashboard 熔断降级
    @RequestMapping("/fusing")
    @SentinelResource(value = "fusing",blockHandler = "fusingBlockHandler",fallback = "fusingfallback")
    @ResponseBody
    public ResultDto fusing() throws Exception{
        logger.info("into fusing");
        Thread.sleep(3000);
        return ResultDto.success();
    }
    public ResultDto fusingBlockHandler(BlockException blockException){
        logger.info("fusingBlockHandler-----");
        return ResultDto.error(501,"fusingBlockHandler ");
    }
    public ResultDto fusingfallback(Throwable e){
        logger.info("fusingfallback异常了");
        return  ResultDto.error(500,"fusingfallback异常了");
    }


    @RequestMapping("/hotParameter/{id}")
    @SentinelResource(value = "hotParameter",blockHandler = "hotParameterBlockHandler")
    @ResponseBody
    public ResultDto hotParameter(@PathVariable("id") String id) throws Exception{
        logger.info(StrUtil.format("热点参数id: {}",id));
        return ResultDto.success();
    }
    public ResultDto hotParameterBlockHandler(@PathVariable("id") String id,BlockException blockException){
        return ResultDto.error(501,"热点参数限流了");
    }





}
