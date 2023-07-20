package com.study.order.controller;

import com.study.common.entity.ResultDto;
import com.study.common.entity.User;
import com.study.common.feign.ProductFeignService;
import com.study.common.feign.SeaFeignService;
import com.study.common.feign.StockFeignService;
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

    @Autowired
    StockFeignService stockFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    SeaFeignService seaFeignService;


    /**
     * 案例： openFeign
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public ResultDto test() throws Exception{
        System.out.println("进入feign-demo逻辑");
        User reduct = stockFeignService.reduct();
        System.out.println(reduct.toString());
        ResultDto info = productFeignService.seata();
        System.out.println(info.getData());
        return ResultDto.success();
    }
}
