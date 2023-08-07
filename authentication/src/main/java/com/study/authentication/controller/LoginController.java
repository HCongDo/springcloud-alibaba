package com.study.authentication.controller;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.study.authentication.entity.QrContent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author hecong
 * @Date 2023/8/4 21:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

  static String authUtl = "http://localhost:7080/auth/oauth/authorize?client_id=order-service&scope=all&response_type=code"
      + "&redirect_uri=http://www.baidu.com";


  /**
   * 获取二维码
   * @return
   */
  @RequestMapping("/qrCode")
  private String  qrCode(){
    QrContent qrContent = new QrContent();
    qrContent.setQrId(UUID.randomUUID().toString());
    qrContent.setAuthUrl(authUtl);
    qrContent.setCreatetime(new Date());
    BufferedImage generate = QrCodeUtil.generate(qrContent.toString(), 300, 300);
    FastByteArrayOutputStream os = new FastByteArrayOutputStream();
    try {
      ImageIO.write(generate, "jpg", os);
    } catch (IOException e) {
      return e.getMessage();
    }
    //如果二维码要在前端显示需要转成Base64
    return Base64.getEncoder().encodeToString(os.toByteArray());
  }

}
