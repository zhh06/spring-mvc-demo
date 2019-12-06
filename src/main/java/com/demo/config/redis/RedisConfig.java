package com.demo.config.redis;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {

    public static AbstractBeanDefinition getJedisPoolConfigBean (
            int maxTotal, int maxIdle, int minIdle, long maxWaitMillis,
            boolean testOnBorrow, boolean testOnReturn
    ) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(JedisPoolConfig.class);
        builder.addPropertyValue("maxTotal", maxTotal);
        builder.addPropertyValue("maxIdle", maxIdle);
        builder.addPropertyValue("minIdle", minIdle);
        builder.addPropertyValue("maxWaitMillis", maxWaitMillis);
        builder.addPropertyValue("testOnBorrow", testOnBorrow);
        builder.addPropertyValue("testOnReturn", testOnReturn);

        return builder.getBeanDefinition();
    }



}
