package com.study.common.feign;

import com.study.common.entity.User;
import com.study.common.fallback.StockFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "stock-service",path = "/stock" ,fallback = StockFeignServiceFallBack.class)
public interface StockFeignService {

    @RequestMapping("/reduct")
    @ResponseBody
    public User reduct();


}
