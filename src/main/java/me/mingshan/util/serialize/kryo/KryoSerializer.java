package me.mingshan.util.serialize.kryo;


import me.mingshan.util.KryoUtil;
import me.mingshan.util.serialize.Serializer;

import java.util.List;

/**
 * Serializer implementation via Kryo.
 *
 * @author mingshan
 */
public class KryoSerializer implements Serializer {
    @Override
    public <T> byte[] serializeObject(T obj) {
        return KryoUtil.writeObjectToByteArray(obj);
    }

    @Override
    public <T> T deserializeObject(byte[] bytes, Class<T> clazz) {
        return KryoUtil.readObjectFromByteArray(bytes, clazz);
    }

    @Override
    public <T> byte[] serializeList(List<T> objList) {
        return null;
    }

    @Override
    public <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass) {
        return null;
    }
}
