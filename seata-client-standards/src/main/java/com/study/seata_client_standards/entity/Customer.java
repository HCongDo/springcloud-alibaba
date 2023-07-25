package com.study.seata_client_standards.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("customer")
public class Customer {
    @TableId(type = IdType.AUTO)
    private int id;
    private String name;
    private int age;
    private String phone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
}