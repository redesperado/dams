package com.jqf.dams.controller;

import com.jqf.dams.service.AdminService;
import com.jqf.dams.util.MD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Resource
    private AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @ResponseBody
    @RequestMapping(value = "/loginIn",method = RequestMethod.POST)
    public Map<String,Object> loginIn(String userName, String userPwd){
        Subject subject = SecurityUtils.getSubject();
        String encodingPwd = MD5Util.encodeByMd5(userPwd);
        UsernamePasswordToken token = new UsernamePasswordToken(userName,encodingPwd);
        subject.login(token);
        String role = adminService.getUserRole(userName);
        Map<String,Object> map = new HashMap<>();
        map.put("msg","success");
        map.put("role",role);
        map.put("username",userName);

        if(role != "" && role != null){
            logger.info("----身份认证通过----");
            map.put("resultCode",1);
        }else{
            logger.info("----身份认证失败----");
            map.put("resultCode",2);
        }

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/registerUser",method = RequestMethod.POST)
    public Map<String,Object> registerUser(@RequestBody Map<String,Object> paramMap){
        logger.info("注册用户入参"+paramMap.toString());
        int flag = adminService.addUser(paramMap);
        Map<String,Object> returenMap = new HashMap<>();
        if(flag > 0){
            returenMap.put("resultCode","1");
            returenMap.put("msg","注册成功！");
        }else {
            returenMap.put("resultCode","0");
            returenMap.put("msg","注册失败！");
        }
        return returenMap;
    }

}
