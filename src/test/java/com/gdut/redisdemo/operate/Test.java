package com.gdut.redisdemo.operate;

import com.gdut.redisdemo.repository.MessageVODao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lulu
 * @Date 2019/6/22 0:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test  {
@Autowired
private MessageVODao dao;

    @org.junit.Test
    public void testSet(){
       /* for(int i=0;i<100000;i++){
            template.opsForValue().set("cart_"+System.nanoTime(),i+"a");
        }*/
    dao.findAllByStatus(-1);
    }


}
