package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.User;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/manage/product")
public class ProductController {

    @Autowired
    IProductService productService;
    @Value("${upload.path}")
    private String uploadPath;

    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String upload(){
        return "upload";
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ServerResponse upload(@RequestParam("pic") MultipartFile file){

        if(file==null){
            return null;
        }

        String filename=file.getOriginalFilename();

        if(filename==null){
            return ServerResponse.serverResponseByFail(StatusEnum.UPLOAD_FILENAME_NOT_EMPTY.getStatus(),StatusEnum.UPLOAD_FILENAME_NOT_EMPTY.getDesc());
        }
        String ext=filename.substring(filename.lastIndexOf("."));
        String name= UUID.randomUUID().toString();
        String newfilename=name+ext;
        File target=new File(uploadPath);
        if(!target.exists()){
            target.mkdir();
        }

        File newfile=new File(uploadPath,newfilename);
        try {
            file.transferTo(newfile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.serverResponseBySuccess(null,newfilename);
    }

    @RequestMapping("/save.do")
    public  ServerResponse addOrUpdate(Product product, HttpSession session){

        User user= (User) session.getAttribute(Consts.USER);
        if(user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
        }

        if(user.getRole()== RoleEnum.ADMIN.getRole()){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_AUTHORITY.getStatus(),StatusEnum.NO_AUTHORITY.getDesc());
        }

        return productService.addOrUpdate(product);
    }

    @RequestMapping("detail.do")
    public ServerResponse detail(HttpSession session,Integer productId){
        User user= (User) session.getAttribute(Consts.USER);
        if(user.getRole()==1){
            return ServerResponse.serverResponseByFail(StatusEnum.ROLE_NOT_ADMIN.getStatus(),StatusEnum.ROLE_NOT_ADMIN.getDesc());
        }
        return productService.detail(productId);
    }

    @RequestMapping("set_sale_status.do")
    public ServerResponse setSaleStatus(Product product){
        productService.setSaleStatus(product);
        return null;
    }
}
