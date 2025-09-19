package com.homework.genshinchatclient.serialize.json;

import com.homework.common.utils.GsonUtils;
import com.homework.genshinchatclient.serialize.Serialization;
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
