package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @RequestMapping("/register.do")
    public ServerResponse register(User user){
        return userService.registerLogic(user);
    }

    @RequestMapping("login.do")
    public ServerResponse login(HttpSession session,String username, String password){
        ServerResponse response= userService.loginLogic(username,password);
        if(response.isSuccess()){
            session.setAttribute(Consts.USER,response.getData());
        }
        return response;
    }

    @RequestMapping("/logout.do")
    public ServerResponse loginOut(HttpSession session){
        session.removeAttribute(Consts.USER);
        System.out.println(session);
        return ServerResponse.serverResponseBySuccess();
    }

    @RequestMapping("/check_valid.do")
    public ServerResponse checkValid(String str,String username){
        return null;
    }

    @RequestMapping("/get_user_info.do")
    public ServerResponse getUserInfo(HttpSession session){
        User user= (User) session.getAttribute(Consts.USER);
        if(user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
        }
        return ServerResponse.serverResponseBySuccess(null,user);
    }

    @RequestMapping("/forget_get_question.do")
    public ServerResponse forgetGetQuestion(String username){
        return userService.forgetGetQuestionLogic(username);
    }

    @RequestMapping("forget_check_answer.do")
    public ServerResponse forgetCheckAnswer(String username,
                                            String question,
                                            String answer){
        return userService.forgetCheckAnswer(username, question, answer);
    }
    @RequestMapping("/update_information.do")
    public ServerResponse updateInformation(User user){
        return userService.updateInformation(user);
    }

    @RequestMapping("/getInforamtion.do")
    public ServerResponse getInforamtion(HttpSession session){

        User user= (User) session.getAttribute(Consts.USER);
        userService.getInformation(user);

        return null;
    }

    @RequestMapping("/list.do")
    public ServerResponse userList(){
        return null;
    }

}
