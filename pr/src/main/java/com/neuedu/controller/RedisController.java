package com.neuedu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@RestController
public class RedisController {


    @Resource
    JedisPool jedisPool;

    @RequestMapping("/redis")
    public  String  set(){

        Jedis jedis= jedisPool.getResource();
        jedis.set("neuedu","fy2020");

        String value= jedis.get("neuedu");

        jedisPool.close();

        return  value;
    }

}