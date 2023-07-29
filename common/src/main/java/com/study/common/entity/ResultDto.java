package com.study.common.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResultDto<T> implements Serializable {

    //模块名
    private String  applicationName;
    //状态码
    private Integer status;
    //错误提示信息
    private String msg;
    //业务数据
    private T data;


    public ResultDto(String applicationName,Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.applicationName = applicationName;
    }


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


    /**
     * 此构造函数给全局异常处理使用
     * @param applicationName 模块名
     * @param status 错误码
     * @param msg 友好错误提示
     * @param errorMsg 具体错误原因
     * @param <T>
     * @return
     */
    public static <T> ResultDto error(String applicationName,Integer status,String msg,T errorMsg){
        return new ResultDto(applicationName,status,msg,errorMsg);
    }

    public static <T> ResultDto success(T data){
        return new ResultDto(200,"请求成功",data);
    }

    public static ResultDto success(){
        return new ResultDto(200,"请求成功");
    }


}
