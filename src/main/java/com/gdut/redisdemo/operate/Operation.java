package com.gdut.redisdemo.operate;

import com.gdut.redisdemo.VO.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lulu
 * @Date 2019/6/21 23:20
 */
@Component
public class Operation {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    public void opForString() {
        stringRedisTemplate.opsForValue().set("xx", "xx");
    }

    public void opForHash() {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        //不会插入
        operations.putIfAbsent("hash", "name", null);
    }

    public void opForList() {
//        List<UserVO> userList = Arrays.asList(new UserVO("1", 20), new UserVO("2", 21), new UserVO("3", 22));
        ListOperations<String, UserVO> operations = redisTemplate.opsForList();
//        operations.leftPushAll("list", userList);
        operations.range("list", 0, -2).stream().forEach(System.out::println);
        for (long i = operations.size("list"); i > 0; i--) {
            UserVO u = operations.leftPop("list");
            System.out.println(u.toString());
        }
        redisTemplate.delete("list");
    }

    public void getKey() {
        long start = System.currentTimeMillis();
        redisTemplate.keys("cart*");
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        RedisConnection connection = RedisConnectionUtils.getConnection(redisTemplate.getConnectionFactory());
        Cursor<byte[]> result = connection.scan(new ScanOptions.ScanOptionsBuilder().count(10).match("cart*").build());
        long start1 = System.currentTimeMillis();
        //cursor有id和position这两个属性，id则对应 scan cursor 的cursor的值，poisition则是当前遍历到第几个
        while (result.hasNext()) {//这里可以改用for循环来获取指定数量的key
            String key = new String(result.next());
            //对key的操作
        }
        long end1 = System.currentTimeMillis();
        System.out.println(end1 - start1);
    }


}
