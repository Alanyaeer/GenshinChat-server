package com.homework.genshinchatapi.config;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 键（Key）使用StringRedisSerializer，确保键是字符串格式
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        // 值（Value）使用Kryo序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 基于Kryo的Redis序列化器
     * 1. 保留泛型类型信息
     * 2. 正确处理资源关闭
     * 3. 符合RedisSerializer异常规范
     */
    static class KryoRedisSerializer<T> implements RedisSerializer<T> {

        private final Class<T> type;
        private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
            Kryo kryo = new Kryo();
            // 配置Kryo
            kryo.setRegistrationRequired(false); // 关闭注册要求，允许序列化任何类
            kryo.setReferences(true); // 支持循环引用
            return kryo;
        });

        // 通过构造方法传入类型信息
        public KryoRedisSerializer(Class<T> type) {
            this.type = type;
        }

        @Override
        public byte[] serialize(T value) throws SerializationException {
            if (value == null) {
                return new byte[0];
            }

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 Output output = new Output(byteArrayOutputStream)) {

                Kryo kryo = kryoThreadLocal.get();
                kryo.writeObject(output, value);
                output.flush();
                return byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                throw new SerializationException("Kryo serialize failed", e);
            }
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length == 0) {
                return null;
            }

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                 Input input = new Input(byteArrayInputStream)) {

                Kryo kryo = kryoThreadLocal.get();
                T obj = kryo.readObject(input, type);
                return obj;
            } catch (Exception e) {
                throw new SerializationException("Kryo deserialize failed", e);
            }
        }
    }
}
