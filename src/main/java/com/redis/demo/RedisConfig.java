package com.redis.demo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Autowired
    JedisConnectionFactory jedisConnectionFactory;
    /**
     * 设置数据存入 redis 的序列化方式
     */
    @Bean
    RedisTemplate<String, Object> objRedisTemplate(Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        //连接
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    /**
     * @功能描述 跟JacksonJsonRedisSerializer实际上是一样的
     * 它不仅可以将对象序列化，还可以将对象转换为json字符串并保存到redis中，但需要和jackson配合一起使用。用JacksonJsonRedisSerializer序列化的话，
     * 被序列化的对象不用实现Serializable接口。Jackson是利用反射和getter和setter方法进行读取的，如果不想因为getter和setter方法来影响存储，
     * 就要使用注解来定义被序列化的对象。
     * @param
     * @return
     */
    @Bean
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }


    /**
     * 实例化 HashOperations 对象,可以使用 Hash 类型操作
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    ValueOperations<String, String> strOperations(RedisTemplate<String, String> stringRedisTemplate) {
        return stringRedisTemplate.opsForValue();
    }

    /**
     * 实例化 ValueOperations 对象,可以使用 String 操作
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 实例化 ListOperations 对象,可以使用 List 操作
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 实例化 SetOperations 对象,可以使用 Set 操作
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 实例化 ZSetOperations 对象,可以使用 ZSet 操作
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
