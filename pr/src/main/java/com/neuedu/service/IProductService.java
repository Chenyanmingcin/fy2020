package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.stereotype.Component;

@Component
public interface IProductService {

     ServerResponse addOrUpdate(Product product);

     ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

     ServerResponse detail(Integer productId);
     ServerResponse updateStock(Integer productId,Integer quantity,int type);

     //type:0 -;1 +
     ServerResponse setSaleStatus(Product product);
}
