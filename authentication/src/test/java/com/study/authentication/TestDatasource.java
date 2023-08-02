package com.study.authentication;

import com.study.common.utils.RSAUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDatasource {


    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    public void test() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidXNlci1yZXNvdXJjZSJdLCJwaG9uZSI6IjE3MzAwMDAwMDAwIiwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJhbGwiXSwiaWQiOiIxMDcyODA2Mzc3NjYxMDA5OTIwIiwiZXhwIjoxNjkwOTAwNjk1LCJhdXRob3JpdGllcyI6WyJhZG1pbiJdLCJqdGkiOiJjNDA2NzJmNy1kNzRlLTRkNDgtOGI2Mi02OGUyYzRlYTMwNDIiLCJjbGllbnRfaWQiOiJvcmRlci1zZXJ2aWNlIiwidXNlcm5hbWUiOiJhZG1pbiJ9.ah33BO31WqkPFvWSxuwrqDdPYXECrPKVzpbhe4i93bEsFWmbWzTSPUXm-5JH1ZVZjN7lxyTXbppgX5ePcieifw";
        Claims body = Jwts.parser()
                .setSigningKey(RSAUtil.getPublicKey())
                .parseClaimsJws(token)
                .getBody();
        System.out.println(body);
    }

}
