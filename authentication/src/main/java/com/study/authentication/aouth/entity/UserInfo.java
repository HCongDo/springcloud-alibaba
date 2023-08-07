package com.study.authentication.aouth.entity;

import java.util.Date;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 12:59
 */
@Data
public class UserInfo implements Serializable{

    private String id;
    private String phone;
    private String password;
    private String username;
    private String smsCode;
    private Date smsCodeCreateTime;
    private String roles;


}
