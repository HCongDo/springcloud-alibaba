package com.study.seata_datasource_proxy.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/18 22:47
 */
public class CustomerIdGenerator implements IdentifierGenerator {

    @Override
    public Long nextId(Object entity) {
        // 填充自己的Id生成器，
        return IdGenerator.generateId();
    }
}
