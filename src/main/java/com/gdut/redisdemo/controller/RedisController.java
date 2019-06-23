package com.gdut.redisdemo.controller;

import com.gdut.redisdemo.comsumer.Comsumer;
import com.gdut.redisdemo.entity.MessageVO;
import com.gdut.redisdemo.VO.UserVO;
import com.gdut.redisdemo.comsumer.ComsumerBuilder;
import com.gdut.redisdemo.producer.Producer;
import com.gdut.redisdemo.repository.MessageVODao;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lulu
 * @Date 2019/6/22 17:02
 */
@RestController
public class RedisController {


    @Autowired
    private Producer producer;
    @Autowired
    private Gson gson;
    @Autowired
    private RedisMessageListenerContainer container;
    @Autowired
    private ComsumerBuilder comsumerBuilder;
@Autowired
private MessageVODao dao;
    @GetMapping("/sendMessage/{topic}")
    public void p(@RequestParam("name") String name, @RequestParam("age") Integer age, @PathVariable("topic") String topic) {
        String messageId = "um_" + System.currentTimeMillis();
        String userId = System.nanoTime() + "";
        UserVO u = new UserVO(name, age, messageId, userId);
        MessageVO m = new MessageVO();
        m.setContent(gson.toJson(u));
        m.setMessageId(messageId);
        m.setTopic(topic);
        dao.save(m);
        producer.sendMessage(topic, m);
    }

    @GetMapping("/subTopic")
    public void addChannel(@RequestParam("topic") String topic) {
        Comsumer c=comsumerBuilder.getComsumer();
        container.addMessageListener(c,new ChannelTopic(topic));
        c.addChannel(topic);
    }


}
