package com.neuedu.service.impl;

import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public ServerResponse registerLogic(User user) {

        //1.参数非空判断
        if(user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }

        //用户名非空判断
        if(user.getUsername()==null||user.getUsername().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }

        //密码非空判断
        if(user.getPassword()==null||user.getPassword().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),StatusEnum.PASSWORD_NOT_EMPTY.getDesc());

        }

        //邮箱非空判断
        if(user.getEmail()==null||user.getEmail().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.EMAIL_NOT_EMPTY.getStatus(),StatusEnum.EMAIL_NOT_EMPTY.getDesc());
        }

        //联系方式非空判断
        if(user.getPhone()==null||user.getPhone().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.PHONE_NOT_EMPTY.getStatus(), StatusEnum.PHONE_NOT_EMPTY.getDesc());
        }

        //密保问题非空判断
        if(user.getQuestion()==null||user.getQuestion().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.QUESTION_NOT_EMPTY.getStatus(),StatusEnum.QUESTION_NOT_EMPTY.getDesc());
        }

        //答案非空判断
        if(user.getAnswer()==null||user.getAnswer().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.ANSWER_NOT_EMPTY.getStatus(),StatusEnum.ANSWER_NOT_EMPTY.getDesc());
        }

        int countUsername=userMapper.countUsername(user.getUsername());
        if(countUsername>0){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_EXISTS.getStatus(),StatusEnum.USERNAME_EXISTS.getDesc());
        }

        int countEmail=userMapper.countEmail(user.getEmail());
        if(countEmail>0){
            return ServerResponse.serverResponseByFail(StatusEnum.EMAIL_EXISTS.getStatus(),StatusEnum.EMAIL_EXISTS.getDesc());
        }

        String password= MD5Utils.getMD5Code(user.getPassword());
        user.setPassword(password);
        user.setRole(RoleEnum.USER.getRole());
        int insertCount=userMapper.insert(user);
        if(insertCount==0){
            return ServerResponse.serverResponseByFail(StatusEnum.REGISTER_FAIL.getStatus(),StatusEnum.REGISTER_FAIL.getDesc());
        }

        return ServerResponse.serverResponseBySuccess("校验成功",null);

    }

    @Override
    public ServerResponse loginLogic(String username, String password) {

        //用户名为空
        if(username==null||username.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }

        //密码非空判断
        if(password==null||password.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),StatusEnum.PASSWORD_NOT_EMPTY.getDesc());

        }

        int countUsername=userMapper.countUsername(username);
        if(countUsername==0){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EXISTS.getStatus(),StatusEnum.USERNAME_NOT_EXISTS.getDesc());
        }

       String _password= MD5Utils.getMD5Code(password);
        User user=userMapper.findUserByUsernameAndPassword(username,_password);
        if (user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_INCORRENT.getStatus(),StatusEnum.PASSWORD_INCORRENT.getDesc());
        }

        return ServerResponse.serverResponseBySuccess(null,user);
    }

    @Override
    public ServerResponse forgetGetQuestionLogic(String username) {

        //用户名非空判断
        if(username==null||username.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }

        String question=userMapper.forgetGetQuestion(username);
        if(question==null||question.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_QUESTION_AND_ANSWER.getStatus(),StatusEnum.NO_QUESTION_AND_ANSWER.getDesc());
        }

        return ServerResponse.serverResponseBySuccess(null,question);
    }

    @Override
    public ServerResponse forgetCheckAnswer(String username, String question, String answer) {

        String answer_=userMapper.forgetCheckAnswer(username, question);
        if(!answer.equals(answer_)){
            return ServerResponse.serverResponseByFail(StatusEnum.ANSWER_IS_WRONG.getStatus(),StatusEnum.ANSWER_IS_WRONG.getDesc());
        }
        return ServerResponse.serverResponseBySuccess();
    }

    public ServerResponse updateInformation(User user){

        //邮箱非空判断
        if(user.getEmail()==null||user.getEmail().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.EMAIL_NOT_EMPTY.getStatus(),StatusEnum.EMAIL_NOT_EMPTY.getDesc());
        }

        //联系方式非空判断
        if(user.getPhone()==null||user.getPhone().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.PHONE_NOT_EMPTY.getStatus(), StatusEnum.PHONE_NOT_EMPTY.getDesc());
        }

        //密保问题非空判断
        if(user.getQuestion()==null||user.getQuestion().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.QUESTION_NOT_EMPTY.getStatus(),StatusEnum.QUESTION_NOT_EMPTY.getDesc());
        }

        //答案非空判断
        if(user.getAnswer()==null||user.getAnswer().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.ANSWER_NOT_EMPTY.getStatus(),StatusEnum.ANSWER_NOT_EMPTY.getDesc());
        }

        int count =userMapper.updateByPrimaryKey(user);
        if(count<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
        }
        return ServerResponse.serverResponseBySuccess("更新个人信息成功",null);
    }

    @Override
    public ServerResponse getInformation(User user) {

        User user_=userMapper.selectByPrimaryKey(user.getId());
        if(user_==null){
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN_AND_QUIT.getStatus(),StatusEnum.NO_LOGIN_AND_QUIT.getDesc());

        }
        return null;
    }

    public ServerResponse AdminLoginLogic(String username, String password) {

        //用户名为空
        if(username==null||username.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }

        //密码非空判断
        if(password==null||password.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),StatusEnum.PASSWORD_NOT_EMPTY.getDesc());

        }

        int countRole=userMapper.adminLogin(username,password);
        if(countRole==0){
            return ServerResponse.serverResponseByFail(StatusEnum.ROLE_NOT_ADMIN.getStatus(),StatusEnum.ROLE_NOT_ADMIN.getDesc());
        }

        String _password= MD5Utils.getMD5Code(password);
        User user=userMapper.findUserByUsernameAndPassword(username,_password);
        if (user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_INCORRENT.getStatus(),StatusEnum.PASSWORD_INCORRENT.getDesc());
        }

        return ServerResponse.serverResponseBySuccess(null,user);
    }

    @Override
    public ServerResponse list(String pageSize, String pageNum) {
        return null;
    }


}
