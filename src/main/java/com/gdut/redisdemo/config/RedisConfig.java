package com.gdut.redisdemo.config;

import com.gdut.redisdemo.comsumer.CheckKeyExpire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lulu
 * @Date 2019/6/22 17:31
 */
@Configuration
public class RedisConfig {


    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public RedisConnection redisConnection(RedisConnectionFactory redisConnectionFactory) {
        return redisConnectionFactory.getConnection();
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }

    @Bean
    public PubSubTable pubSubTable() {
        return new PubSubTable();
    }

    @Bean
    public CheckKeyExpire checkKeyExpire(
            RedisConnectionFactory redisConnectionFactory
    ) {
        return new CheckKeyExpire(container(redisConnectionFactory));
    }

}
