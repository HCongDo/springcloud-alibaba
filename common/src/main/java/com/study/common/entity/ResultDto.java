package com.study.common.entity;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.net.InetAddress;

import com.study.common.utils.CommonUtil;
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
    //信息来源IP
    private String hostIp;
    //实例名称
    private String instanceName;
    //日志ID
    private String logId;


    public ResultDto(String applicationName,Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.applicationName = applicationName;
        this.hostIp = CommonUtil.getHostIp();
        this.hostIp = CommonUtil.getHostIp();
        this.instanceName = CommonUtil.getInstanceName();

    }


    public ResultDto(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.hostIp = CommonUtil.getHostIp();
        this.instanceName = CommonUtil.getInstanceName();
    }

    public ResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.hostIp = CommonUtil.getHostIp();
        this.instanceName = CommonUtil.getInstanceName();
    }

    public ResultDto() {}

    public static <T> ResultDto error(Integer status,String msg){
        return new ResultDto(status,msg);
    }

    public static <T> ResultDto error(String msg){
        return new ResultDto(500,msg);
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

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("applicationName",applicationName);
        jsonObject.put("status",status);
        jsonObject.put("msg",msg);
        jsonObject.put("data",data);
        jsonObject.put("hostIp",hostIp);
        jsonObject.put("instanceName",instanceName);
        jsonObject.put("logId",logId);
        return jsonObject.toString();
    }

}
