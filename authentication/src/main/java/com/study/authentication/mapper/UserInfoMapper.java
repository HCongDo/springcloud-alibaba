package com.study.authentication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.authentication.entity.UserInfo;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 13:04
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo getUserByUsername(String username);
}
