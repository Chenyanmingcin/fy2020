package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ICartService {

    ServerResponse list(Integer userId);
    ServerResponse add(Integer userId, Integer productId, Integer count);

    ServerResponse findCartByIdAndChecked(Integer userId);

    ServerResponse deleteBatchByIds(List<Cart> cartList);

}
