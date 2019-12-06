package com.demo.config.redis;

import com.demo.utils.RedisUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RedisContext<T> {

    private static ApplicationContext ctx;

    private static volatile RedisContext instance = null;

    public static RedisContext getInstance() {
        if (null == instance) {
            synchronized (RedisContext.class) {
                if (null == instance) {
                    initCtx();
                    instance = new RedisContext();
                }
            }
        }
        return instance;
    }

    private static void initCtx() {
        ApplicationContext parent = new ClassPathXmlApplicationContext(
                new String[]{"redis-parent.xml"},
                RedisContext.class
        );

        BeanDefinitionRegistry reg = (BeanDefinitionRegistry) parent.getAutowireCapableBeanFactory();
        reg.registerBeanDefinition("jedisPoolConfig", RedisConfig.getJedisPoolConfigBean(600, 100, 10, 10000L, true, true));

        ctx = new ClassPathXmlApplicationContext(
                new String[] {"redis-context.xml"},
                RedisContext.class, parent
        );
    }

    public RedisUtils<T> getRedisUtils(String name) {
        return ctx.getBean(name, RedisUtils.class);
    }

}
