package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Cart;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ICartService {

    ServerResponse selectByUserId(Integer userId);

}
