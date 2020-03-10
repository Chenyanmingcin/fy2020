package com.neuedu.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order/")
public class PayController {

    @Autowired
    IPayService payService;

    @Value("${alipay.public.key}")
    String alipayPublicKey;

    @RequestMapping("pay.do")
    public ServerResponse pay(Long orderNO){

        return payService.pay(orderNO);
    }

    @RequestMapping("alipay_callback.do")
    public  String   alipay_callback(HttpServletRequest request){


        System.out.println("====");

        Map<String,String[]> callbackParam=  request.getParameterMap();

        Iterator<Map.Entry<String,String[]>> iterator=callbackParam.entrySet().iterator();

        Map<String,String> signMap=new HashMap<>();

        while(iterator.hasNext()){
            Map.Entry<String,String[]> entry=iterator.next();
            String key= entry.getKey();
            String[] values=entry.getValue();
            StringBuffer buffer=new StringBuffer();
            for(int i=0;i<values.length;i++){
                buffer.append(values[i]+",");
            }
            String value=buffer.toString();
            value=value.substring(0,value.length()-1);
            signMap.put(key,value);

            System.out.println("=key="+key+"  value="+value);
        }

        //1.验签、
        signMap.remove("sign_type");
        try {
            boolean result= AlipaySignature.rsaCheckV2(signMap,alipayPublicKey,"utf-8","RSA2");
            if(!result){
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        };


        //2.处理业务逻辑
        return payService.callbackLogic(signMap);

    }


}
