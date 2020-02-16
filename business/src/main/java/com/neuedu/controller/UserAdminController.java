package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager/user")
public class UserAdminController {

    @Autowired
    IUserService userService;

    @RequestMapping("/login.do")
    public ServerResponse adminLogin(String username,String password){
        return null;
    }

    @RequestMapping("/list.do")
    public ServerResponse list(
            @RequestParam(required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(required = false,defaultValue = "10") Integer   pageSize){

        //userService.list(pageNum,pageSize)

        return null;
    }

}
