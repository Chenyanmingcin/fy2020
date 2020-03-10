package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface IPayService {

    ServerResponse pay(Long orderNO);

    String callbackLogic(Map<String, String> signParam);

}
