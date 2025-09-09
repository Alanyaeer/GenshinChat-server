package com.homework.chatserver.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.homework.chatserver.serialize.Serialization;
import com.homework.common.exception.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/9
 */
public class HessianSerialization implements Serialization {
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializeException("Serialization failed");
        }

    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            HessianInput hessianInput = new HessianInput(byteArrayInputStream);
            Object o = hessianInput.readObject();

            return clazz.cast(o);

        } catch (Exception e) {
            throw new SerializeException("Deserialization failed");
        }

    }
}
