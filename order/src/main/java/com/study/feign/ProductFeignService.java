package com.study.feign;

import com.study.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "product-service",path = "/product")
public interface ProductFeignService {

    @RequestMapping("/{id}")
    public String getInfo(@PathVariable("id") Integer id);


}
