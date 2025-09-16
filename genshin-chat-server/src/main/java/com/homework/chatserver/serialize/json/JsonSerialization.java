package com.homework.chatserver.serialize.json;

import com.homework.chatserver.serialize.Serialization;
import com.homework.common.utils.GsonUtils;
import org.springframework.stereotype.Component;

/**
 * json序列化器
 *
 * @author wps
 * @date 2025/09/06
 */
@Component
public class JsonSerialization implements Serialization {
    @Override
    public byte[] serialize(Object obj) {
        return GsonUtils.toJson(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return GsonUtils.fromJson(new String(bytes), clazz);
    }
}
