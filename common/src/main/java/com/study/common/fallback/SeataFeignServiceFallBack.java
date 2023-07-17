package com.study.common.fallback;

import com.study.common.entity.ResultDto;
import com.study.common.entity.User;
import com.study.common.feign.SeaFeignService;
import com.study.common.feign.StockFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SeataFeignServiceFallBack implements SeaFeignService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResultDto test() {
        logger.info(" 执行 SeataFeignServiceFallBack 服务降级逻辑");
        return ResultDto.success("seata服务降级处理");
    }
}
