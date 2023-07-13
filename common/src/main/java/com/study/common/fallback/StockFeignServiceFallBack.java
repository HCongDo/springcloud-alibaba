package com.study.common.fallback;

import com.study.common.entity.User;
import com.study.common.feign.StockFeignService;
import org.springframework.stereotype.Service;

@Service
public class StockFeignServiceFallBack implements StockFeignService {

    public User reduct() {
        System.out.println(" 执行 StockFeignServiceFallBack 服务降级逻辑");
        return new User();
    }

}
