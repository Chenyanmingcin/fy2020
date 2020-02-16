package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CartServiceImpl implements ICartService {

    @Resource
    CartMapper cartMapper;


    @Override
    public ServerResponse selectByUserId(Integer userId) {

        List<Cart> carts=cartMapper.selectByUserId(userId);
        return ServerResponse.serverResponseBySuccess(null,carts);
    }
}
