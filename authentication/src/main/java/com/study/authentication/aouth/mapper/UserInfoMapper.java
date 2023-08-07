package com.study.authentication.aouth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.authentication.aouth.entity.UserInfo;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 13:04
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     *  根据用户名查询用户
     * @param username
     * @return
     */
    UserInfo getUserByUsername(String username);

    /**
     *  根据手机号查找用户
     * @param phone
     * @return
     */
    UserInfo getUserByUserPhone(String phone);
}
