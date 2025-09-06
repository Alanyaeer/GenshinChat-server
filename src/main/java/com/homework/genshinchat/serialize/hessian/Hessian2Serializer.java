package com.homework.genshinchat.serialize.hessian;


import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.homework.genshinchat.exception.SerializeException;
import com.homework.genshinchat.serialize.Serializer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Hessian is a dynamically-typed, binary serialization and Web Services protocol designed for object-oriented transmission.
 *
 * @author Vinlee Xiao
 * @createTime 2022/2/23 21:11
 */
@Component
public class Hessian2Serializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Hessian2Output hessianOutput = new Hessian2Output(byteArrayOutputStream);
            hessianOutput.writeObject(obj);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializeException("Serialization failed");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            Hessian2Input hessianInput = new Hessian2Input(byteArrayInputStream);
            Object o = hessianInput.readObject();
            return clazz.cast(o);
        } catch (Exception e) {
            throw new SerializeException("Deserialization failed");
        }
    }
}
