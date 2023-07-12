package com.study.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.study.common.entity.User;
import com.study.common.feign.ProductFeignService;
import com.study.common.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:25
 */
@RefreshScope
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    StockFeignService stockFeignService;

    @Autowired
    ProductFeignService productFeignService;


    @RequestMapping("/add")
    @SentinelResource(value = "order-add",blockHandler = "addBlockHandler",fallback = "addfallback")
    public String add(){
        System.out.println("下单成功-extensionName= ");
//        int i = 1/0;
        User reduct = stockFeignService.reduct();
        System.out.println(reduct.toString());
        String info = productFeignService.getInfo(1);
        System.out.println(info);
        return "Hello World";
    }

    public String addfallback(Throwable e){
      return "异常了";
    }


    public String addBlockHandler(BlockException blockException){
         return "限流了";
    }
}
