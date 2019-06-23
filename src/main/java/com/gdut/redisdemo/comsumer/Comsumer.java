package com.gdut.redisdemo.comsumer;

import com.gdut.redisdemo.entity.MessageVO;
import com.gdut.redisdemo.VO.UserVO;
import com.gdut.redisdemo.config.PubSubTable;
import com.gdut.redisdemo.repository.MessageVODao;
import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lulu
 * @Date 2019/6/22 16:54
 */
@Getter
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Comsumer implements MessageListener {
    private static AtomicInteger index = new AtomicInteger(0);
    private Integer id;
    private RedisConnection redisConnection;

    public Comsumer() {
        this.id = index.incrementAndGet();
    }

    public void setRedisConnection(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    @PreDestroy
    public void destoryConnection() {
        this.redisConnection.close();
    }

    @Autowired
    private Gson gson;
    @Autowired
    private PubSubTable pubSubTable;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MessageVODao dao;

    public void addChannel(String topic) {

        pubSubTable.addComsumer(topic, this);
        System.out.printf("消费者%s 订阅一个主题%s%n", id, topic);
        redisConnection.subscribe(this, topic.getBytes());
    }


    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        Integer name = id;
        String topic = new String(message.getChannel());
        String content = new String(message.getBody());
        System.out.printf("%s从主题%s收到消息%n", name, topic);//业务处理
        MessageVO messageVO = gson.fromJson(content, MessageVO.class);
        UserVO userVO = gson.fromJson(messageVO.getContent(), UserVO.class);
        System.out.printf("消息内容:%s%n", userVO.toString());

        //设置消费完成
        redisTemplate.expire("listen_" + name + "_" + messageVO.getMessageId(), 1,TimeUnit.NANOSECONDS);

        //删除监听过期的键
        redisTemplate.delete("fail_" + name + "_" + messageVO.getMessageId());


    }
}
