package com.gdut.redisdemo.comsumer;

import com.gdut.redisdemo.VO.MessageVO;
import com.gdut.redisdemo.VO.UserVO;
import com.gdut.redisdemo.config.PubSubTable;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author lulu
 * @Date 2019/6/22 16:54
 */
@Component
public class Comsumer1 implements MessageListener{
@Autowired
private Gson gson;
    @Autowired
    private RedisConnection redisConnection;
@Autowired
private PubSubTable pubSubTable;
@Autowired
private StringRedisTemplate redisTemplate;
    public void addChannel(String topic){

        pubSubTable.addComsumer(topic,this.getClass().getSimpleName());
        System.out.printf("%s 订阅一个主题%s%n",this.getClass().getSimpleName(),topic);
       redisConnection.subscribe(this,topic.getBytes());
    }





    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        String name=this.getClass().getSimpleName();
        String topic=new String(message.getChannel());
        String content=new String(message.getBody());
        MessageVO messageVO=gson.fromJson(content,MessageVO.class);
        //如果这个取出来的不是正确的id，丢弃并记录。
        String b=redisTemplate.<String>opsForList().rightPop(topic+"_"+name);
        if(b!=null&&b.equals(topic+"_"+messageVO.getMessageId())){
            UserVO userVO=gson.fromJson(messageVO.getContent(),UserVO.class);
            System.out.printf("%s从主题%s收到消息:%s%n",name,topic,content);//业务处理
            System.out.printf("消息内容:%s%n",userVO.toString());
            redisTemplate.expire(topic+"_"+name+"_"+messageVO.getMessageId(),1,TimeUnit.NANOSECONDS);
        }else{
            //把他设为fail,准备重新处理
            redisTemplate.opsForValue().set("fail_"+topic+"_"+name+"_"+messageVO.getMessageId(),content);
        }
    }
}
