package com.study.authentication.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.List;

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
    // 多个逗号隔开
    private String roles;


}
