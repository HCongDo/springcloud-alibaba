package com.study.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultDto<T> implements Serializable {

    private Integer status;
    private String msg;
    private T data;

    public ResultDto(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResultDto() {}

    public static <T> ResultDto error(Integer status,String msg){
        return new ResultDto(status,msg);
    }

    public static <T> ResultDto success(T data){
        return new ResultDto(200,"请求成功",data);
    }

    public static ResultDto success(){
        return new ResultDto(200,"请求成功");
    }


}
