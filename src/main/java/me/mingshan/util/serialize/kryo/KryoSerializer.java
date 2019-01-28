package me.mingshan.util.serialize.kryo;


import me.mingshan.util.KryoUtil;
import me.mingshan.util.serialize.Serializer;

/**
 * Serializer implementation via Kryo.
 *
 * @author mingshan
 */
public class KryoSerializer implements Serializer {

    @Override
    public <T> byte[] writeObject(T obj) {
        return KryoUtil.writeObjectToByteArray(obj);
    }

    @Override
    public <T> T readObject(byte[] bytes, Class<T> clazz) {
        return KryoUtil.readObjectFromByteArray(bytes, clazz);
    }
}
