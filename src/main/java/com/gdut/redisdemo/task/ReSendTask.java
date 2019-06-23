package com.gdut.redisdemo.task;

import com.gdut.redisdemo.entity.MessageVO;
import com.gdut.redisdemo.producer.Producer;
import com.gdut.redisdemo.repository.MessageVODao;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
    @Autowired
    private MessageVODao dao;
    @Scheduled(initialDelay = 20000,fixedRate = 15000)
    public void reSend(){
//        Cursor<byte[]> list=redisTemplate.getConnectionFactory().getConnection().scan(ScanOptions.scanOptions().count(100).match("fail_*").build());
//0是未被消费，1是已经消费，-1是失败
        List<MessageVO> list=dao.findAllByStatus(-1);
        for (MessageVO e:list)
        {
            if(e.getStatus()==-1){
                e.setReTryCount(e.getReTryCount()+1);
                System.out.printf("第%s次重新投递消息 messageId:%s 主题: %s%n",e.getReTryCount(),e.getMessageId(),e.getTopic());
                p.sendMessage(e.getTopic(),e);
                if(e.getReTryCount()==3){
                    e.setStatus(-2);
                }
                dao.save(e);
            }


        }


    }
}
