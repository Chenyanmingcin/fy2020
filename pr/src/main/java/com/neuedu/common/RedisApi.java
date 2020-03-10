package com.neuedu.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RedisApi {

    @Resource
     JedisPool jedisPool;

    public String set(String key,String value){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.set(key,value);
        jedis.close();
        return result;
    }

    public  String   get(String key){
        Jedis jedis=jedisPool.getResource();
        String result=   jedis.get(key);
        jedis.close();

        return result;
    }


    //key存在，设置不成功，反之，成功
    public Long setNx(String key,String value){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.setnx(key,value);
        return result;
    }



    //原子性，先get后set
    public String getSet(String key,String value){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.getSet(key,value);
        jedis.close();
        return result;
    }

    //为key设置过期时间
    public Long expire(String key,int timeout){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.expire(key,timeout);
        return result;
    }

    //查看key剩余时间
    public Long ttl(String key){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.ttl(key);
        return result;
    }

    //设置key，value，为key设置过期时间
    public String setEx(String key,int timeout,String value){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.setex(key,timeout,value);
        return result;
    }

    //设置key，field,value
    public Long hset(String key,String field,String value){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.hset(key,field,value);
        jedis.close();
        return result;
    }
//批量插入
    public String hset(String key, Map<String,String> map){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.hmset(key,map);
        jedis.close();
        return result;
    }
//根据key，filed查看value
    public Map<String,String> hgetAll(String key ){
        Jedis jedis=jedisPool.getResource();
        Map<String,String> result=jedis.hgetAll(key);
        jedis.close();
        return result;
    }
    //根据key，查看filed
    public Set<String> hgetAllField(String key ){
        Jedis jedis=jedisPool.getResource();
        Set<String> result=jedis.hkeys(key);
        jedis.close();
        return result;
    }
    //根据key，查看value
    public List<String> hgetAllVals(String key){
        Jedis jedis=jedisPool.getResource();
        List<String> result=jedis.hvals(key);
        jedis.close();
        return result;
    }
    //计数器
    public Long hgetAllVals(String key, String field, Long incr){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.hincrBy(key,field,incr);
        jedis.close();
        return result;
    }

}
