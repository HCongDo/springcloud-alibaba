package com.study.authentication.service;

import com.study.authentication.entity.UserInfo;
import com.study.authentication.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 12:54
 */
@Service("userService")
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        UserInfo userinfo = userInfoMapper.getUserByUsername(username);
        //需要构造org.springframework.security.core.userdetails.User 对象包含账号密码还有用户的角色
        if (userinfo != null) {
            User user = new User(userinfo.getUsername(), userinfo.getPassword(), AuthorityUtils.createAuthorityList("admin"));
            return new OauthUser(userinfo, user);
        } else {
            throw new UsernameNotFoundException("用户[" + username + "]不存在");
        }
    }
}
