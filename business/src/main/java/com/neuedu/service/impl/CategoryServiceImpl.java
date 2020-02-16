package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(Integer parentId, String categoryName) {

        if(categoryName==null||categoryName.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_NAME_NOT_EMPTY.getStatus(),StatusEnum.CATEGORY_NAME_NOT_EMPTY.getDesc());
        }

        Category category =new Category();

        category.setParentId(parentId);
        category.setName(categoryName);

        int result= categoryMapper.insert(category);
        if (result<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_INSERT_FAIL.getStatus(),StatusEnum.CATEGORY_INSERT_FAIL.getDesc());
        }


        return null;
    }

    @Override
    public ServerResponse setCategoryName(Integer categoryId, String categoryName) {

        if(categoryId==null||categoryId.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORYID_NOT_EMPTY.getStatus(),StatusEnum.CATEGORYID_NOT_EMPTY.getDesc());
        }

        if(categoryName==null||categoryName.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_NAME_NOT_EMPTY.getStatus(),StatusEnum.CATEGORY_NAME_NOT_EMPTY.getDesc());
        }

        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category==null){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_NOT_EXISTS.getStatus(),StatusEnum.CATEGORY_NOT_EXISTS.getDesc());
        }

        category.setName(categoryName);
        int result=categoryMapper.updateByPrimaryKey(category);
        if(result<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_UPDATE_FAIL.getStatus(),StatusEnum.CATEGORY_UPDATE_FAIL.getDesc());
        }

        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse getCategory(Integer categoryId) {

        if(categoryId==null||categoryId.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORYID_NOT_EMPTY.getStatus(),StatusEnum.CATEGORYID_NOT_EMPTY.getDesc());
        }

        List<Category> categoryList=categoryMapper.getCategoryById(categoryId);

        return ServerResponse.serverResponseBySuccess(null,categoryList);
    }

    @Override
    public ServerResponse getDeepCategory(Integer categoryId) {
        if(categoryId==null||categoryId.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORYID_NOT_EMPTY.getStatus(),StatusEnum.CATEGORYID_NOT_EMPTY.getDesc());
        }
        Set<Integer> set=new HashSet<>();
        Set<Integer> resultSet=findAllSubCategory(set,categoryId);


        return ServerResponse.serverResponseBySuccess(null,resultSet);
    }

    private Set<Integer> findAllSubCategory(Set<Integer> categoryIds,Integer categoryId) {
        //step1: 先根据categoryId查询类别
        Category category= categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categoryIds.add(category.getId());
        }

        //step2:查询category下所有的一级子节点
        ServerResponse<List<Category>> serverResponse=getCategory(categoryId);
        if(!serverResponse.isSuccess()){
            return categoryIds;
        }
        List<Category> categoryList=serverResponse.getData();
        for(Category c:categoryList){
            findAllSubCategory(categoryIds,c.getId());
        }

        return  categoryIds;
    }
}
