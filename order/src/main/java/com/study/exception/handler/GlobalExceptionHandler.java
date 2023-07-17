package com.study.exception.handler;

import com.study.common.entity.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger=   LoggerFactory.getLogger(this.getClass());

    /**
     * 全局异常捕捉处理
     * @param e
     * @return
     */
//    @ExceptionHandler(value = Exception.class)
    public ResultDto errorHandler(Exception e) {
        logger.error("程序异常, 详细信息为:{}", e.getLocalizedMessage() , e);
        return ResultDto.error(500,"不好意思，程序出错了，请稍后再试",e.getMessage());
    }


}
