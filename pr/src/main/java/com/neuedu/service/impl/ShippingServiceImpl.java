package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.exception.BusinessException;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;

    @Transactional(isolation = Isolation.DEFAULT,timeout = 10,readOnly = false,rollbackFor = {BusinessException.class},
    propagation = Propagation.MANDATORY)
    @Override
    public ServerResponse add(Shipping shipping) {

        int count=shippingMapper.insert(shipping);
        if(count<=0){
            throw new BusinessException(StatusEnum.ADDRESS_ADD_FAIL.getStatus(),StatusEnum.ADDRESS_ADD_FAIL.getDesc());
        }



        return ServerResponse.serverResponseBySuccess(null,shipping.getId());
    }
}
