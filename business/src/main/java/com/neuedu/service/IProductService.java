package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;

public interface IProductService {

     ServerResponse addOrUpdate(Product product);

     ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

     ServerResponse detail(Integer productId);

     ServerResponse setSaleStatus(Product product);
}
