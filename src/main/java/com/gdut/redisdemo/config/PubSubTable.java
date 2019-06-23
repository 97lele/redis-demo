package com.gdut.redisdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author lulu
 * @Date 2019/6/22 17:39
 */

public class PubSubTable {
    //Map的key为主题，Set里面的元素为队列里面的消费者的队列key，该队列用来检测消息是否被正确接受
    private Map<String, Set<String>> pubSubMap = new HashMap();

    @Autowired
    private StringRedisTemplate redisTemplate;
//添加关系
    public Boolean addComsumer(String topic, String comsumer) {
        Set<String> comsumerList = pubSubMap.get(topic);

        if (comsumerList == null) {
            comsumerList = new HashSet<>();
        }
        Boolean b = comsumerList.add(comsumer);
        pubSubMap.put(topic, comsumerList);
        return b;
    }
//删除关系
    public Boolean removeComsumer(String topic, String comsumer) {
        System.out.println(comsumer+"取消订阅主题"+topic);
        Set<String> comsumerList = pubSubMap.get(topic);
        Boolean b =false;
        if(comsumerList!=null){
           b= comsumerList.remove(comsumer);
            pubSubMap.put(topic, comsumerList);
        }
        return b;
    }


    //广播消息
    public void boradCast(String topic, String messageId) {
        if(pubSubMap.get(topic)!=null){
            for (String comsumer : pubSubMap.get(topic)) {
                //这里不再次进行入队和设监听键的原因是已经有了（对应滞后消费的情况）
                if(!redisTemplate.hasKey("fail_"+topic+"_"+comsumer+"_"+messageId)){
                    //设置监听键
                    redisTemplate.opsForValue().set(topic+"_"+comsumer + "_" + messageId, messageId);
                    //为该队列传入消息id，为后面校验使用
                    redisTemplate.opsForList().leftPush(topic+"_"+comsumer, topic + "_" + messageId);
                }
            }
        }

    }

}
