package com.gdut.redisdemo.controller;

import com.gdut.redisdemo.VO.MessageVO;
import com.gdut.redisdemo.VO.UserVO;
import com.gdut.redisdemo.comsumer.Comsumer1;
import com.gdut.redisdemo.comsumer.Comsumer2;
import com.gdut.redisdemo.config.PubSubTable;
import com.gdut.redisdemo.producer.Producer;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
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
    private Comsumer1 comsumer1;
    @Autowired
    private Comsumer2 comsumer2;
    @Autowired
    private Producer producer;
    @Autowired
    private Gson gson;
    @Autowired
    private RedisMessageListenerContainer messageListenerContainer;
    @Autowired
    private PubSubTable pubSubTable;

    @GetMapping("/sendMessage/{topic}")
    public void p(@RequestParam("name") String name, @RequestParam("age") Integer age, @PathVariable("topic") String topic) {
        String messageId = "um_" + System.currentTimeMillis();
        String userId = System.nanoTime() + "";
        UserVO u = new UserVO(name, age, messageId, userId);
        MessageVO m = new MessageVO();
        m.setContent(gson.toJson(u));
        m.setMessageId(messageId);
        m.setTopic(topic);
        producer.sendMessage(topic, m);
    }

    @GetMapping("/unSubTopic/{id}")
    public void unSubTopci(@RequestParam("topic") String topic, @PathVariable("id") Integer id) {
        if (id == 1) {
            messageListenerContainer.removeMessageListener(comsumer1, new ChannelTopic(topic));
            pubSubTable.removeComsumer(topic, comsumer1.getClass().getSimpleName());
        } else if (id == 2) {
            comsumer2.addChannel(topic);
            messageListenerContainer.removeMessageListener(comsumer2, new ChannelTopic(topic));
            pubSubTable.removeComsumer(topic, comsumer2.getClass().getSimpleName());
        }
    }

    @GetMapping("/subTopic/{id}")
    public void addChannel(@RequestParam("topic") String topic, @PathVariable("id") Integer id) {
        if (id == 1) {
            messageListenerContainer.addMessageListener(comsumer1, new ChannelTopic(topic));
            comsumer1.addChannel(topic);

        } else if (id == 2) {
            comsumer2.addChannel(topic);
            messageListenerContainer.addMessageListener(comsumer2, new ChannelTopic(topic));

        }

    }


}
