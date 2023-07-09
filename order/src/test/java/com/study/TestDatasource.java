package com.study;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDatasource {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        String sql = "SELECT username FROM roles ";
        // 通过jdbcTemplate查询数据库
        String userName = (String) jdbcTemplate.queryForObject(sql, new Object[]{0}, String.class);
        System.out.println(userName);
    }

}
