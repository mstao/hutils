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
package me.mingshan.util.serialize;


import me.mingshan.util.serialize.kryo.KryoSerializer;

/**
 * The serializer holder, gets the implementation of {@link Serializer}.
 *
 * @author mingshan
 */
public class SerializerHolder {
    private static final Serializer SERIALIZER = new KryoSerializer();

    /**
     * Get the implementation of serializer.
     *
     * @return the {@link Serializer} implementation
     */
    public static Serializer serializerImpl() {
        return SERIALIZER;
    }
}
