package com.neuedu.ininterceptor;

import com.google.gson.Gson;
import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * 请求到达controller之前通过preHandle
     * true:
     * false:
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {

        HttpSession session=request.getSession();
        User user= (User) session.getAttribute(Consts.USER);

        if(user!=null){
            return true;
        }



        PrintWriter printWriter= null;
        try {
            response.reset();
            response.addHeader("Content-Type","application/json;charset=utf-8");
            printWriter = response.getWriter();
            ServerResponse serverResponse=ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
            Gson gson=new Gson();
            String json=gson.toJson(serverResponse);

            printWriter.write(json);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }   finally {
            if(printWriter!=null){
                printWriter.close();
            }
        }



        return false;
    }

    /**
     *
     *当cotroller往前端响应时，会通过该拦截器postHandle方法
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 一次Http请求以后
     * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
