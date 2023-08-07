package com.study.authentication.granter;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.study.authentication.aouth.entity.UserInfo;
import com.study.authentication.aouth.mapper.UserInfoMapper;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 *  短信验证码登录鉴权
 * @version 1.0
 * @author hecong
 * @date 2023-08-04 17:55:23
 */
public class SmsCodeTokenGranter extends AbstractTokenGranter {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final String SMS_GRANT_TYPE = "captcha";

  private UserInfoMapper userInfoMapper;

  public SmsCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
      ClientDetailsService clientDetailsService,
      OAuth2RequestFactory requestFactory,
      UserInfoMapper userInfoMapper) {
    super(tokenServices, clientDetailsService, requestFactory, SMS_GRANT_TYPE);
    this.userInfoMapper = userInfoMapper;
  }


  @Override
  protected OAuth2Authentication getOAuth2Authentication(ClientDetails client,
      TokenRequest tokenRequest) {
    Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
    System.out.println(parameters.toString());
    String phoneNumber = parameters.get("phone");
    String smsCode = parameters.get("captcha");
    try{
      Assert.notBlank(phoneNumber, "手机号为空");
      Assert.notBlank(smsCode, "验证码为空");
      UserInfo user = userInfoMapper.getUserByUserPhone(phoneNumber);
      Assert.notNull(user, "为找到用户信息");
      Assert.isTrue(DateUtil.isExpired(user.getSmsCodeCreateTime(), DateField.MINUTE,5,new Date()), "验证码已过期");
      Assert.isTrue(smsCode.equals(user.getSmsCode()), "验证码错误");
      AbstractAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(user, null, AuthorityUtils
          .createAuthorityList(user.getRoles()));
      userAuth.setDetails(parameters);
      OAuth2Request oAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
      return new OAuth2Authentication(oAuth2Request, userAuth);
    }catch (Exception e) {
      logger.error("短信登录验证失败，错误信息为：{}",e.getMessage());
      throw new AccessDeniedException(e.getMessage());
    }
  }
}
