package com.redis.demo;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    private static Logger logger = Logger.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private ListOperations<?, ?> s;
    private HashOperations<?, ?, ?> h;
    /**
     * 写入或更新缓存
     * @param key
     * @param value
     * @return
     */
    public void set(final String key, Object value)
    {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
        } catch (Exception e) {
            logger.error("write redis is faill");
            e.printStackTrace();

        }
    }


    /**
     * 写入缓存
     *  设置失效时间
     * @param key
     * @param value
     * @return
     */
    public void set(final String key, Object value, Long expireTime) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key)
    {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key)
    {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern 正则表达式
     */
    public void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


}
