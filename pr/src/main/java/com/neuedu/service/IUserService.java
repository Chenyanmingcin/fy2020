package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;

public interface IUserService {

    ServerResponse registerLogic(User user);

    ServerResponse loginLogic(String username,String password);

    ServerResponse forgetGetQuestionLogic(String username);

    ServerResponse forgetCheckAnswer(String username,
                                     String question,
                                     String answer);
    ServerResponse updateInformation(User user);

    ServerResponse getInformation(User user);

    ServerResponse AdminLoginLogic(String username,String password);

    ServerResponse list(String pageSize,String pageNum);

}
