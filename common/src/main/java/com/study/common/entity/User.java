package com.study.common.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class User implements Serializable {

    public String id;
    public String name;
    public Integer age;


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }
}
