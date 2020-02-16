package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICategoryService {

    ServerResponse addCategory(Integer parentId,String categoryName);

    ServerResponse setCategoryName(Integer categoryId,String categoryName);

    ServerResponse getCategory(Integer categoryId);

    ServerResponse getDeepCategory(Integer categoryId);

}
