package com.tulingxueyuan.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tulingxueyuan.product.entity.Person;

import java.util.List;

//@Mapper   启动类用@MapperScan注解说明了扫描路径便可注释此注解
public interface PersonMapper extends BaseMapper<Person> {

     public List<Person> selectPersonList();
}
