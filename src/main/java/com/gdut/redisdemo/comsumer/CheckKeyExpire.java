package com.gdut.redisdemo.comsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author lulu
 * @Date 2019/6/22 18:40
 */
public class CheckKeyExpire extends KeyspaceEventMessageListener {
    @Autowired
    private StringRedisTemplate redisTemplate;

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
            System.out.println("消息id为：" + body + "成功处理");
            redisTemplate.delete("fail_" + new String(message.getChannel())+"_"+ body);
            //如果这里有状态改变为接受成功的消息之类的，可以在这里操作数据库
        }
    }
}
