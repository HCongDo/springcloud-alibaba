package com.study.stock.controller;

import com.study.common.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {


    @RequestMapping("/reduct")
    @ResponseBody
    public User reduct(){
        User user = new User();
        user.setId("1");
        user.setName("张三");
        user.setAge(18);
        System.out.println("扣减库存");
        return user;
    }
}
