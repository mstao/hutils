/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.mingshan.util.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import me.mingshan.util.serialize.Serializer;

/**
 * Serializer implementation via protostuff.
 *
 * @author mingshan
 */
public class ProtostuffSerializer implements Serializer {

    @SuppressWarnings("unchecked")
    @Override
    public <T> byte[] writeObject(T obj) {

        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T readObject(byte[] bytes, Class<T> clazz) {
        try {
            Schema<T> schema = getSchema(clazz);
            T message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Simplify the way to get schema, do not need to cache the generated schema in this class
     * because it is automatically cached in {@link RuntimeSchema}.
     *
     * @param cls the specified class
     * @param <T> the generics class
     * @return the {@link Schema}
     */
    private static <T> Schema<T> getSchema(Class<T> cls) {
        return RuntimeSchema.getSchema(cls);
    }
}
