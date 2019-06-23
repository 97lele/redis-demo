package com.gdut.redisdemo.producer;

import com.gdut.redisdemo.entity.MessageVO;
import com.gdut.redisdemo.config.PubSubTable;
import com.google.gson.Gson;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lulu
 * @Date 2019/6/22 16:47
 */
@Component
public class Producer {

    @Autowired
    private Gson gson;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PubSubTable pubSubTable;



    public void sendMessage(String topic, MessageVO messageVO) {
        //这里给订阅该主题的链接的每个队列进行广播该消息
        String content=gson.toJson(messageVO);
        pubSubTable.boradCast(topic, content,messageVO.getMessageId());
        redisTemplate.getConnectionFactory().getConnection().publish(topic.getBytes(CharsetUtil.UTF_8), content.getBytes());
    }
}
