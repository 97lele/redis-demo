package com.gdut.redisdemo.comsumer;

import com.gdut.redisdemo.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author lulu
 * @Date 2019/6/23 10:08
 */
@Component
public class ComsumerBuilder {


    public Comsumer getComsumer() {
        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
//将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

// 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();


        String[] beanNamesForType = defaultListableBeanFactory.getBeanNamesForType(Comsumer.class);

        System.out.println("beanNamesForType:" + Arrays.toString(beanNamesForType));

        defaultListableBeanFactory.removeBeanDefinition(beanNamesForType[0]);

// 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Comsumer.class);



// 注册bean
        defaultListableBeanFactory.registerBeanDefinition("com.gdut.redisdemo.comsumer.Comsumer", beanDefinitionBuilder.getRawBeanDefinition());

        Object bean = SpringContextUtils.getBean(Comsumer.class);
        Object connection=SpringContextUtils.getBean(RedisConnection.class);
        Comsumer result=(Comsumer)bean;
        result.setRedisConnection((RedisConnection) connection);
        return result;
    }
}
