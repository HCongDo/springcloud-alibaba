package com.study.common.entity;

import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import lombok.Data;

/**
 * JWT对象
 *
 * @Description: TODO
 * @Author hecong
 * @Date 2023/8/4 20:02
 * @Version 1.0
 */
@Data
public class JwtInfo {

  private String aud;
  private String user_id;
  private String phone;
  private String user_name;
  private String scope;
  private Integer exp;
  private String[] authorities;
  private String jti;
  private String client_id;

  @Override
  public String toString() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("aud",aud);
    jsonObject.put("user_id",user_id);
    jsonObject.put("phone",phone);
    jsonObject.put("user_name",user_name);
    jsonObject.put("scope",scope);
    jsonObject.put("exp",exp);
    jsonObject.put("authorities",Arrays.toString(authorities));
    jsonObject.put("client_id",client_id);
    jsonObject.put("jti",jti);
    return jsonObject.toJSONString();
  }
}
