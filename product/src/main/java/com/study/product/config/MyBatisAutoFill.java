package com.study.product.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis plus 自动填充扩展接口实现
 * 对数据库每条记录的创建时间和更新时间自动进行填充
 */
@Component
public class MyBatisAutoFill  implements MetaObjectHandler {

    /**
     * 插入时的填充策略
     * @param metaObject
     */
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
    }

    /**
     * 更新时的填充策略
     * @param metaObject
     */
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
