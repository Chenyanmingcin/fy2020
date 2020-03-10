package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Resource
    IOrderService orderService;

    @RequestMapping("create.do")
    public ServerResponse create(HttpSession session,Integer shippingId){

        User user= (User) session.getAttribute(Consts.USER);
        if(user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(), StatusEnum.NO_LOGIN.getDesc());
        }

        return orderService.createOrder(user.getId(),shippingId);
    }

    @RequestMapping("cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNO){


        return orderService.cancelOrder(orderNO);
    }

}
