package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.ICategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category")
public class CategoryController {

    @Resource
    ICategoryService categoryService;


    @RequestMapping("add_category.do")
    public ServerResponse addCategory(@RequestParam(value = "parentId", required=false,defaultValue ="0") Integer parentId,
                                      @RequestParam(value = "categoryName") String categoryName,
                                      HttpSession session){

        User user= (User) session.getAttribute(Consts.USER);
        if(user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(), StatusEnum.NO_LOGIN.getDesc());
        }

        if(user.getRole()== RoleEnum.ADMIN.getRole()){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_AUTHORITY.getStatus(), StatusEnum.NO_AUTHORITY.getDesc());
        }

        return categoryService.addCategory(parentId,categoryName);

    }

    @RequestMapping("/set_category_name.do")
    public ServerResponse setCategoryName(@RequestParam(value = "categoryId") Integer categoryId,
                                          @RequestParam(value = "categoryName") String categoryName,
                                          HttpSession session){

        User user= (User) session.getAttribute(Consts.USER);
        if(user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(), StatusEnum.NO_LOGIN.getDesc());
        }

        if(user.getRole()== RoleEnum.ADMIN.getRole()){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_AUTHORITY.getStatus(), StatusEnum.NO_AUTHORITY.getDesc());
        }

        return categoryService.setCategoryName(categoryId, categoryName);
    }

    @RequestMapping("/get_category.do")
    public ServerResponse getCategory(@Param("categoryId")Integer categoryId){

        return categoryService.getCategory(categoryId);
    }

    @RequestMapping("/get_deep_category.do")
    public ServerResponse getDeepCategory(@Param("categoryId") Integer categoryId){
        return categoryService.getDeepCategory(categoryId);
    }
}
