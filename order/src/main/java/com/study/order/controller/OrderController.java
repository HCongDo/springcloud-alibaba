package com.study.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:25
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RestTemplate restTemplate;


    @RequestMapping("/add")
    public String add(){
        System.out.println("下单成功");
        String result = restTemplate.getForObject("http://stock-service/stock/reduct", String.class);
        System.out.println(result);
        return "Hello World";
    }
}
