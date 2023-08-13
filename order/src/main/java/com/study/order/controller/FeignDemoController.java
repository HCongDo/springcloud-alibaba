package com.study.order.controller;

import com.study.common.entity.ResultDto;
import com.study.common.entity.User;
import com.study.common.feign.ProductFeignService;
import com.study.common.feign.SeaFeignService;
import com.study.common.feign.StockFeignService;
import com.study.common.utils.TraceIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign 内部服务调用 Demo
 */
@RestController
@RequestMapping("/feign")
public class FeignDemoController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StockFeignService stockFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    SeaFeignService seaFeignService;


    /**
     * 案例： openFeign seata
     * @return
     */
        @RequestMapping("/seata")
    @ResponseBody
    public ResultDto seata() throws Exception{
        ResultDto reduct = stockFeignService.reduct();
        System.out.println(reduct.toString());
        ResultDto info = productFeignService.seata();
        logger.info(info.getData().toString());
        return ResultDto.success();
    }

    /**
     * 案例： 全流程查询
     * @return
     */
    @RequestMapping("/all")
    @ResponseBody
    public ResultDto all(){
        logger.info("traceId:{}", TraceIdUtil.getTraceId());
        ResultDto reduct = stockFeignService.reduct();
        System.out.println(reduct.toString());
        ResultDto info = productFeignService.list();
        logger.info(info.getData().toString());
        return ResultDto.success(info);
    }


    /**
     * 案例： openFeign err 错误链测试
     * @return
     */
    @RequestMapping("/err")
    @ResponseBody
    public ResultDto err() throws Exception{
        return productFeignService.err();
    }



}
