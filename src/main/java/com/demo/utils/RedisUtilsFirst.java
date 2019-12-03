package com.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component(value = "redisUtilsFirst")
public class RedisUtilsFirst<T> {

    private static final String REDIS_KEY_PREFIX = "YNote::%s";

    @Resource
    private RedisTemplate<String, T> redisTemplateFrist;

    /**
     * 读取缓存
     *
     * @param key redis key
     * @return cache value object type
     */
    public T get(final String key) {
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplateFrist.opsForValue();
            T data = operations.get(wholeKey);
            log.debug("get redis::key:{} value::{}", wholeKey, data);
            return data;
        } catch (Exception e) {
            log.error("RedisUtil get key:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            return null;
        }
    }

    /**
     * 写入缓存
     *
     * @param key   redis key
     * @param value cache value
     * @return success: true failure: false
     */
    public boolean set(final String key, T value) {
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplateFrist.opsForValue();
            operations.set(wholeKey, value);
            log.debug("set redis::key:{} value::{}", wholeKey, operations.get(wholeKey));
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set key:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            return false;
        }
    }

    /**
     * 写入缓存  value禁止为null
     *
     * @param key     redis key
     * @param value   cache value
     * @param seconds 过期时间，单位为s
     * @return success: true failure: false
     */
    public boolean setNonNull(final String key, T value, long seconds) {
        if (Objects.isNull(value)) {
            return false;
        }
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplateFrist.opsForValue();
            operations.set(wholeKey, value, seconds, TimeUnit.SECONDS);
            log.debug("set redis::key:{} value::{}", wholeKey, operations.get(wholeKey));
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set key:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            return false;
        }
    }

    /**
     * 将数据写入redis set
     *
     * @param key      redis key
     * @param valueSet value set
     */
    public Boolean sadd(final String key, Set<T> valueSet) {
        String wholeKey = getKey(key);
        try {
            SetOperations operations = redisTemplateFrist.opsForSet();
            Object[] valueArray = valueSet.toArray(new Object[valueSet.size()]);
            operations.add(wholeKey, valueArray);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil sadd key:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            return false;
        }
    }

    /**
     * 将数据写入redis set
     *
     * @param key   redis key
     * @param value redis value
     */
    public Boolean sadd(final String key, T value) {
        String wholeKey = getKey(key);
        try {
            SetOperations<String, T> operations = redisTemplateFrist.opsForSet();
            operations.add(wholeKey, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil sadd key:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            return false;
        }
    }

    /**
     * set中是否存在member
     *
     * @param key   redis key
     * @param value value
     */
    public Boolean smember(final String key, T value) {
        String wholeKey = getKey(key);
        SetOperations<String, T> operations = redisTemplateFrist.opsForSet();
        return operations.isMember(wholeKey, value);
    }

    /**
     * 返回一个set的所有类容
     *
     * @param key redis key
     * @return value set
     */
    public Set<T> smembers(final String key) {
        String wholeKey = getKey(key);
        SetOperations<String, T> operations = redisTemplateFrist.opsForSet();
        return operations.members(wholeKey);
    }

    /**
     * 为给定 key 设置过期时间，以秒计
     *
     * @param key     redis key
     * @param seconds 过期时间
     */
    public Boolean expire(final String key, Long seconds) {
        String wholeKey = getKey(key);
        try {
            BoundValueOperations boundValueOperations = redisTemplateFrist
                    .boundValueOps(wholeKey);
            boundValueOperations.expire(seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil expire key:{} expire time failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            return false;
        }
    }

    /**
     * 为给定 key 设置过期时间，以毫秒计
     *
     * @param key          redis key
     * @param milliseconds 过期时间
     */
    public Boolean pexpire(final String key, Long milliseconds) {
        String wholeKey = getKey(key);
        try {
            BoundValueOperations boundValueOperations = redisTemplateFrist
                    .boundValueOps(wholeKey);
            boundValueOperations.expire(milliseconds, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil pexpire key:{} expire time failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            return false;
        }
    }

    /**
     * delete key
     *
     * @param key redis key
     */
    public void delete(final String key) {
        String wholeKey = getKey(key);
        try {
            redisTemplateFrist.delete(wholeKey);
        } catch (Exception e) {
            log.error("RedisUtil delete key:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * delete keys
     * @param keys key list
     */
//    public Long delete(final List<String> keys) {
//        return redisTemplateFrist.delete(keys);
//    }

    /**
     * 获取redis锁
     *
     * @param key        锁的key值，不可以为空字符串
     * @param expireTime 锁过期时间，单位：秒
     */
    public boolean acquireLock(final String key, Long expireTime) {
        boolean result;
        if (null == key || expireTime == null) {
            return false;
        }
        String wholeKey = getKey(key);
        try {
            Object execute = redisTemplateFrist.execute(
                    (RedisCallback) redisConnection -> {
                        RedisSerializer stringSerializer = redisTemplateFrist
                                .getKeySerializer();
                        Object rlt = redisConnection.execute(
                                "SET", stringSerializer.serialize(wholeKey),
                                stringSerializer.serialize("1"),
                                stringSerializer.serialize("EX"),
                                stringSerializer.serialize(expireTime > 0 ?
                                        String.valueOf(expireTime) :
                                        ""),
                                stringSerializer.serialize("NX")
                        );
                        return rlt;
                    }
            );
            result = execute != null && execute.equals("OK");
        } catch (Exception e) {
            log.error("RedisUtil acquire Lock:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            result = false;
        }
        return result;
    }

    /**
     * 释放redis锁
     *
     * @param key 锁的key值，不可以为空字符串
     */
    public void releaseLock(final String key) {
        String wholeKey = getKey(key);
        try {
            redisTemplateFrist.delete(wholeKey);
        } catch (Exception e) {
            log.error("RedisUtil release Lock:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * 检测是否有redis锁
     *
     * @param key 锁的key值，不可以为空字符串
     */
    public boolean checkLock(final String key) {
        String wholeKey = getKey(key);
        try {
            return redisTemplateFrist.hasKey(wholeKey);
        } catch (Exception e) {
            log.error("RedisUtil check Lock:{} failure::{}", wholeKey, ExceptionUtils.getFullStackTrace(e));
            return false;
        }
    }

    private String getKey(String key) {
        return String.format(REDIS_KEY_PREFIX, key);
    }
}