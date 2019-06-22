package com.gdut.redisdemo.task;

import com.gdut.redisdemo.VO.MessageVO;
import com.gdut.redisdemo.producer.Producer;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author lulu
 * @Date 2019/6/22 21:26
 */
@Component
public class ReSendTask {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private Gson gson;
    @Autowired
    private Producer p;
    @Scheduled(initialDelay = 20000,fixedRate = 15000)
    public void reSend(){
//        Cursor<byte[]> list=redisTemplate.getConnectionFactory().getConnection().scan(ScanOptions.scanOptions().count(100).match("fail_*").build());

Set<String>  set=       redisTemplate.keys("fail_*");

        for (String e:set)
        {
            String value=redisTemplate.opsForValue().get(e);
            MessageVO m=gson.fromJson(value,MessageVO.class);
            //重投3次视为失败-1

            if(m.getReTryCount()==3&&m.getStatus()==0){
                //删除该fail处理的key，这里如果用数据库的话，也可以持久化并变改状态 因为剩余的消息会被抛出队列 m.setStatus(-1);
                redisTemplate.delete(e);
                // "fail_name_key",这里要删除用于监听消息是否消费的key
                redisTemplate.delete(e.substring(e.indexOf("_")+1));
                continue;
            }
            if(m.getStatus()!=-1){
                System.out.printf("第%s次重新投递消息 messageId:%s 主题: %s%n",m.getReTryCount()+1,m.getMessageId(),m.getTopic());
                m.setReTryCount(m.getReTryCount()+1);
                p.sendMessage(m.getTopic(),m);
            }

        }


    }
}
