package com.gdut.redisdemo.util;

import com.gdut.redisdemo.comsumer.Comsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 获取ApplicationContext和Object的工具类
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        SpringContextUtils.applicationContext = applicationContext; // NOSONAR
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }

    /**
     * 获取bean
     *
     * @param name bean的id
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    //通过类型获取上下文中的bean
    public static Object getBean(Class<?> requiredType) {
        return applicationContext.getBean(requiredType);
    }




}