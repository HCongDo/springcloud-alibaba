package com.study.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.study.common.entity.ResultDto;
import com.study.common.entity.User;
import com.study.common.feign.ProductFeignService;
import com.study.common.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:25
 */
@RefreshScope //动态刷新配置 - nacos config
@RestController
@RequestMapping("/order")
public class OrderController {

    @Qualifier("com.study.common.feign.StockFeignService")
    @Autowired
    StockFeignService stockFeignService;

    @Autowired
    ProductFeignService productFeignService;


    /**
     * 案例： openFeign
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResultDto add() throws Exception{
        System.out.println("下单成功");
        User reduct = stockFeignService.reduct();
        System.out.println(reduct.toString());
        String info = productFeignService.getInfo(1);
        System.out.println(info);
        return ResultDto.success();
    }

}
