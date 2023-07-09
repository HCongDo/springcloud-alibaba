package com.study.feign;

import com.study.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "stock-service",path = "/stock")
public interface StockFeignService {

    @RequestMapping("/reduct")
    @ResponseBody
    public User reduct();


}
