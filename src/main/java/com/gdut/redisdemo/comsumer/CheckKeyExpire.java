package com.gdut.redisdemo.comsumer;

import com.gdut.redisdemo.entity.MessageVO;
import com.gdut.redisdemo.repository.MessageVODao;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author lulu
 * @Date 2019/6/22 18:40
 */
public class CheckKeyExpire extends KeyspaceEventMessageListener {
    @Autowired
    private StringRedisTemplate redisTemplate;
 @Autowired
 private MessageVODao dao;
 @Autowired
 private Gson gson;

    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     */
    public CheckKeyExpire(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    protected void doHandleMessage(Message message) {
        String channel = new String(message.getChannel());
        if (channel.equals("__keyevent@0__:expired")) {
            String body = new String(message.getBody());
          if(body.startsWith("fail")){
              System.out.printf("消息%s超时5s没有响应，失败了%n",body);
             String key= "listen"+body.substring(body.indexOf("_"));
              String value=redisTemplate.opsForValue().get(key);
              System.out.println(value);
              MessageVO m=gson.fromJson(value,MessageVO.class);
              redisTemplate.delete(key);
              m.setStatus(-1);
              dao.save(m);
          }else if(body.startsWith("listen")){
              System.out.printf("消息%s成功消费!%n",body);
              //删除监听过期的键
              String key= "fail"+body.substring(body.indexOf("_"));
              String value=redisTemplate.opsForValue().get(key);
              System.out.println(value);
              MessageVO m=gson.fromJson(value,MessageVO.class);
              m.setStatus(1);
              dao.save(m);
              //设置消费完成

              //删除监听过期的键
              redisTemplate.delete(key);
          }
        }
    }
}
