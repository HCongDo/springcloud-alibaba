package com.study.authentication.service;

import com.study.authentication.entity.UserInfo;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/20 12:56
 */
public class OauthUser implements UserDetails, CredentialsContainer {


    private final UserInfo userInfo;
    private final User user;

    public OauthUser(UserInfo userInfo, User user) {
        this.userInfo = userInfo;
        this.user = user;
    }


    @Override
    public void eraseCredentials() {
        user.eraseCredentials();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public User getUser() {
        return user;
    }
}