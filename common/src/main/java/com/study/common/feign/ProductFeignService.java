package com.study.common.feign;

import com.study.common.entity.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "product-service",path = "/product")
public interface ProductFeignService {

    @RequestMapping("/seata")
    public ResultDto seata();

    @RequestMapping("/err")
    public ResultDto err();

}
