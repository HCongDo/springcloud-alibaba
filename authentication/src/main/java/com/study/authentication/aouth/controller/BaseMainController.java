package com.study.authentication.aouth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description: TODO
 * @Author hecong
 * @Date 2023/8/4 22:20
 * @Version 1.0
 */
@Controller
public class BaseMainController {

  @GetMapping("/auth/login")
  public String loginPage(){
    return "login";
  }
}
