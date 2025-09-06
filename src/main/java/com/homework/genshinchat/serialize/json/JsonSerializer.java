package com.homework.genshinchat.serialize.json;

import com.homework.genshinchat.serialize.Serializer;
import com.homework.genshinchat.utils.GsonUtils;
import org.springframework.stereotype.Component;

/**
 * json序列化器
 *
 * @author wps
 * @date 2025/09/06
 */
@Component
public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return GsonUtils.toJson(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return GsonUtils.fromJson(new String(bytes), clazz);
    }
}
