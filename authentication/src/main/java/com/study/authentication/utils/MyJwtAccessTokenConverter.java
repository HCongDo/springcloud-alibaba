package com.study.authentication.utils;

import com.study.authentication.aouth.service.OauthUser;
import java.util.HashMap;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 *  JWT token 转化
 * @version 1.0
 * @author hecong
 * @date 2023-08-04 17:55:23
 */
public class MyJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            //((DefaultOAuth2AccessToken) accessToken).setRefreshToken();
            Object principal = authentication.getPrincipal();
            if (principal instanceof OauthUser) {
                OauthUser user = (OauthUser) principal;
                HashMap<String, Object> map = new HashMap<>();
                map.put("user_id", user.getBaseOperator().getId());
                map.put("phone", user.getBaseOperator().getPhone());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            }
        }
        return super.enhance(accessToken, authentication);
    }


}
