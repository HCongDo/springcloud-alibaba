package com.study.common.feign;

import com.study.common.entity.ResultDto;
import com.study.common.fallback.SeataFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@FeignClient(name = "seata-client-standards",path = "/seata" , fallback = SeataFeignServiceFallBack.class)
@FeignClient(name = "seata-datasource-proxy",path = "/seata" , fallback = SeataFeignServiceFallBack.class)
public interface SeaFeignService {

    @RequestMapping("/test")
    @ResponseBody
    public ResultDto test();


}
