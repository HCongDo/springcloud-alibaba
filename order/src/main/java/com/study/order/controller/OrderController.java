package com.study.order.controller;

import com.study.common.entity.User;
import com.study.feign.ProductFeignService;
import com.study.feign.StockFeignService;
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

    @Value("${extension-name}")
    private String extensionName;


    @RequestMapping("/add")
    public String add(){
        System.out.println("下单成功-extensionName= "+extensionName);
        User reduct = stockFeignService.reduct();
        System.out.println(reduct.toString());
        String info = productFeignService.getInfo(1);
        System.out.println(info);
        return "Hello World";
    }
}
