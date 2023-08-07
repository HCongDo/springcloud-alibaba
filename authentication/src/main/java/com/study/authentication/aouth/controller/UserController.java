package com.study.authentication.aouth.controller;

import com.study.common.entity.ResultDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/29 20:30
 */
@RestController
public class UserController {

   @RequestMapping("/user")
   private ResultDto getUserInfo(){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       if(authentication!=null & authentication.getPrincipal()!=null){
           return ResultDto.success(authentication);
       }
       return ResultDto.error("获取用户信息失败");
   }



}
