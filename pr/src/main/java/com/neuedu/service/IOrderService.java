package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface IOrderService {

    //创建订单
    ServerResponse createOrder(Integer userId,Integer shippingId);

    ServerResponse cancelOrder(Long orderNO);





    ServerResponse findOrderByOrderNO(Long oderNO);

    ServerResponse updateOrder(Long orderNo, String payTime,Integer orderStatus);

}
