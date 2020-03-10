package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {

    @Resource
    ICartService cartService;


    @RequestMapping("list.do")
    public ServerResponse list(Integer productId, HttpSession session) {

        User user = (User) session.getAttribute(Consts.USER);
        if (user == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(), StatusEnum.NO_LOGIN.getDesc());
        }
        return cartService.list(user.getId());
    }

    @RequestMapping("add.do")
    public ServerResponse add(HttpSession session,Integer userId, Integer productId, Integer count){
        User user = (User) session.getAttribute(Consts.USER);
        if (user == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(), StatusEnum.NO_LOGIN.getDesc());
        }
        return cartService.add(userId,productId,count);
    }
}
