package com.study.common.fallback;

import com.study.common.entity.ResultDto;
import com.study.common.entity.User;
import com.study.common.feign.StockFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StockFeignServiceFallBack implements StockFeignService {

    Logger logger =LoggerFactory.getLogger(this.getClass());

    public ResultDto reduct() {
        logger.info(" 执行 StockFeignServiceFallBack 服务降级逻辑");
        return ResultDto.error("服务降级逻辑");
    }

}
