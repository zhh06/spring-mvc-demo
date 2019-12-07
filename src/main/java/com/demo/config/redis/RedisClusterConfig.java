package com.demo.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisClusterConfig {

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(600);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        return jedisPoolConfig;
    }

    @Bean(name = "redisClusterConfigurationFirst")
    public RedisClusterConfiguration getRedisClusterConfigurationFirst() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setMaxRedirects(3);
        Set<RedisNode> redisClusterNodeSet = new HashSet<>();
        redisClusterNodeSet.add(new RedisClusterNode("ymath-redis-cluster", 7000));
        redisClusterNodeSet.add(new RedisClusterNode("ymath-redis-cluster", 7001));
        redisClusterNodeSet.add(new RedisClusterNode("ymath-redis-cluster", 7002));
        redisClusterConfiguration.setClusterNodes(redisClusterNodeSet);
        return redisClusterConfiguration;
    }

    @Bean(name = "redisClusterConfigurationSecond")
    public RedisClusterConfiguration getRedisClusterConfigurationSecond() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setMaxRedirects(3);
        Set<RedisNode> redisClusterNodeSet = new HashSet<>();
        redisClusterNodeSet.add(new RedisClusterNode("redis-cluster", 7000));
        redisClusterNodeSet.add(new RedisClusterNode("redis-cluster", 7001));
        redisClusterNodeSet.add(new RedisClusterNode("redis-cluster", 7002));
        redisClusterConfiguration.setClusterNodes(redisClusterNodeSet);
        return redisClusterConfiguration;
    }

    @Bean(name = "jeidsConnectionFactoryFirst")
    public JedisConnectionFactory getJedisConnectionFactoryFirst() {
        return new JedisConnectionFactory(getRedisClusterConfigurationFirst(), getJedisPoolConfig());
    }

    @Bean(name = "jeidsConnectionFactorySecond")
    public JedisConnectionFactory getJedisConnectionFactorySecond() {
        return new JedisConnectionFactory(getRedisClusterConfigurationSecond(), getJedisPoolConfig());
    }

    @Bean(name = "redisTemplateFirst")
    public RedisTemplate getRedisTemplateFirst() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(getJedisConnectionFactoryFirst());
        setSerializer(redisTemplate);
        return redisTemplate;
    }

    @Bean(name = "redisTemplateSecond")
    public RedisTemplate getRedisTemplateSecond() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(getJedisConnectionFactorySecond());
        setSerializer(redisTemplate);
        return redisTemplate;
    }

    private void setSerializer(RedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
    }

}
