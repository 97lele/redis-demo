package com.gdut.redisdemo.config;

import com.gdut.redisdemo.comsumer.Comsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lulu
 * @Date 2019/6/22 17:39
 */

public class PubSubTable {
    //Map的key为主题，Set里面的元素为队列里面的消费者的队列key，该队列用来检测消息是否被正确接受
    private Map<String, Set<Integer>> pubSubMap = new HashMap();
    @Autowired
    private StringRedisTemplate redisTemplate;

    //添加关系
    public synchronized Boolean addComsumer(String topic, Comsumer comsumer) {

        Set<Integer> comsumerList = pubSubMap.get(topic);
        if (comsumerList == null) {
            comsumerList = new HashSet<>();
        }
        Boolean b = comsumerList.add(comsumer.getId());
        pubSubMap.put(topic, comsumerList);
        return b;
    }








    //广播消息
    public synchronized void boradCast(String topic,String content,String messageId) {
        if (pubSubMap.get(topic) != null) {
            for (Integer comsumer : pubSubMap.get(topic)) {
                    //设置监听键,用于消息被消费后过期，如果没有被消费，则作为重传
                    redisTemplate.opsForValue().set(  "listen_" + comsumer + "_" + messageId, content);
                    //默认20s没有操作就认为是投递失败
                    redisTemplate.opsForValue().set("fail_"+comsumer+"_"+messageId,content,10, TimeUnit.SECONDS);
            }
        }

    }

}
