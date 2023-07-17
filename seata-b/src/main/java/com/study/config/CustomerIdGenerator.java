package com.study.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

public class CustomerIdGenerator implements IdentifierGenerator {

    @Override
    public Long nextId(Object entity) {
        // 填充自己的Id生成器，
        return IdGenerator.generateId();
    }

}
