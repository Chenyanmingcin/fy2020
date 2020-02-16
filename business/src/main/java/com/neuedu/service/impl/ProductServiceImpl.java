package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ICategoryService categoryService;

    @Override
    public ServerResponse addOrUpdate(Product product) {

        if(product==null){

            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        Integer productId=product.getId();



        String subImages=product.getSubImages();// 1.png,2.png,3.png
        if(subImages!=null&&subImages.length()>0){
            String mainImage=subImages.split(",")[0];
            product.setMainImage(mainImage);
        }


        if(productId==null){//添加商品

            int insertCount=   productMapper.insert(product);
            if(insertCount<=0){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_ADD_FAIL.getStatus(),StatusEnum.PRODUCT_ADD_FAIL.getDesc());
            }else{
                return  ServerResponse.serverResponseBySuccess("商品添加成功",null);
            }

        }else{//商品更新

            //step1:查询商品
            Product product1= productMapper.selectByPrimaryKey(product.getId());
            if(product1==null){
                //更新的商品不存在
                return ServerResponse.serverResponseByFail(StatusEnum.UPDATE_PRODUCT_NOT_EXISTS.getStatus(),StatusEnum.UPDATE_PRODUCT_NOT_EXISTS.getDesc());
            }

            //step2:更新商品

            int updateCount= productMapper.updateProductByActivate(product);
            if(updateCount<=0){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_UPDATE_FAIL.getStatus(),StatusEnum.PRODUCT_UPDATE_FAIL.getDesc());
            }else{
                return  ServerResponse.serverResponseBySuccess("商品更新成功",null);
            }


        }



    }

    @Override
    public ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //step1:判断是否传递了categoryId和keyword
        if(categoryId==-1&& (keyword==null||keyword.equals(""))){
            //前端没有传递categoryId和Keyword,往前端返回空的数据

            //PageHelper

            PageHelper.startPage(pageNum,pageSize);
            List<Product> productList=new ArrayList<>();
            PageInfo pageInfo=new PageInfo(productList);

            return ServerResponse.serverResponseBySuccess(null,pageInfo);


        }

        //step2:判断CategoryId是否传递
        List<Integer> categoryList=new ArrayList<>();

        if(categoryId!=-1){//传递categoryId

            //查询categoryId下的所有子类
            ServerResponse<Set<Integer>> response=categoryService.getDeepCategory(categoryId);

            if(response.isSuccess()){
                Set<Integer> categoryIds=response.getData();

                Iterator<Integer> iterator= categoryIds.iterator();
                while(iterator.hasNext()){
                    categoryList.add(iterator.next());
                }

            }
        }

        //step3:判断keyword是否为空

        if(keyword!=null && !keyword.equals("")){
            keyword="%"+keyword+"%";
        }


        //step4:执行查询


        //一定写在查询前面
        PageHelper.startPage(pageNum,pageSize);
        //

        List<Product> productList=productMapper.findProductByCategoryIdsAndkeyword(categoryList,keyword);



        //构建分页模型
        PageInfo pageInfo=new PageInfo(productList);



        //step5:返回结果

        return ServerResponse.serverResponseBySuccess("",pageInfo);
    }

    public ServerResponse detail(Integer productId){

        if(productId==null||productId.equals("")){

            return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_EXISTS.getStatus(),StatusEnum.PRODUCT_NOT_EXISTS.getDesc());
        }

        Product product= productMapper.selectByPrimaryKey(productId);


        return ServerResponse.serverResponseBySuccess(null,product);
    }

    public ServerResponse setSaleStatus(Product product){
        int count=productMapper.setSaleStatus(product.getId());
        if(count==0){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_UPDATE_STATUS_FAIL.getStatus(),StatusEnum.CATEGORY_UPDATE_STATUS_FAIL.getDesc());
        }
        return ServerResponse.serverResponseBySuccess("修改产品状态成功",null);
    }

}
