package com.study.common.feign;

import com.study.common.entity.ResultDto;
import com.study.common.fallback.SeataFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "seata-b",path = "/seata-b" , fallback = SeataFeignServiceFallBack.class)
public interface SeaFeignService {

    @RequestMapping("/test")
    @ResponseBody
    public ResultDto test();


}
