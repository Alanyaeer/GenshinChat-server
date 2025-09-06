package com.homework.genshinchatapi.config;


import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Configuration
public class RedisConfig {

//    @Bean
//    @SuppressWarnings(value = { "unchecked", "rawtypes" })
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
//    {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
//
//        // 使用StringRedisSerializer来序列化和反序列化redis的key值
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(serializer);
//
//        // Hash的key也采用StringRedisSerializer的序列化方式
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(serializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        HessianRedisSerializer<String> stringHessianRedisSerializer = new HessianRedisSerializer<>();
        HessianRedisSerializer<Object> objectHessianRedisSerializer = new HessianRedisSerializer<>();

        redisTemplate.setKeySerializer(stringHessianRedisSerializer);
        redisTemplate.setValueSerializer(objectHessianRedisSerializer);

        redisTemplate.setHashKeySerializer(stringHessianRedisSerializer);
        redisTemplate.setHashValueSerializer(objectHessianRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * 基于Hessian的Redis序列化器
     *
     * @author wps
     * @date 2025/09/06
     */
    static class HessianRedisSerializer<T> implements RedisSerializer<T> {

        @Override
        public byte[] serialize(T value) throws SerializationException {
            if (value == null) {
                return new byte[0]; // 或者 return null;
            }
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
                hessian2Output.writeObject(value);
                hessian2Output.flush();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("serialize fail", e);
            }
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
                @SuppressWarnings("unchecked")
                T obj = (T) hessian2Input.readObject();
                return obj;
            } catch (IOException e) {
                throw new RuntimeException("Deserialize fail", e);
            }
        }
    }
}
