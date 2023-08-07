package com.study.authentication.aouth.entity;

import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import lombok.Data;

/**
 * 返回前端二维码对象
 * @Description: TODO
 * @Author hecong
 * @Date 2023/8/4 21:49
 * @Version 1.0
 */
@Data
public class QrContent {

  private String qrId;
  private String authUrl;
  private Date createtime;

  @Override
  public String toString() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("qrId",qrId);
    jsonObject.put("authUrl",authUrl);
    jsonObject.put("createtime",createtime);
    return jsonObject.toString();
  }
}
