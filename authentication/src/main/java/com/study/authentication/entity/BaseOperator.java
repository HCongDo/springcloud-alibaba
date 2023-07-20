package com.study.authentication.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.User;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 12:59
 */
@Data
public class BaseOperator{

    private String userid;
    private String telphonenum;
    private String password;
    private final String username;


}
