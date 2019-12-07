package com.demo.config.redis;

import com.demo.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@Configuration
public class RedisUtilsConfig {

    @Resource
    private RedisTemplate<String, String> redisTemplateFirst;

    @Resource
    @Qualifier(value = "redisTemplateSecond")
    private RedisTemplate<String, String> redisTemplateSecond;

    @Bean(name = "redisUtilsFirst")
    public RedisUtils<String> getRedisUtilsFirst() {
        return new RedisUtils<>(redisTemplateFirst);
    }

    @Bean(name = "redisUtilsSecond")
    public RedisUtils<String> getRedisUtilsSecond() {
        return new RedisUtils<>(redisTemplateSecond);
    }

}
