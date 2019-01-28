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

/**
 * Interface that defines serialize operation.
 *
 * @author mingshan
 */
public interface Serializer {

    /**
     * Serializes the specified object to byte array.
     *
     * @param obj the specified object
     * @param <T> the generics class
     * @return the byte array
     */
    <T> byte[] writeObject(T obj);

    /**
     * Deserializes the specified byte array to object.
     *
     * @param bytes the specified byte array
     * @param clazz the specified class
     * @param <T> the generics class
     * @return the object
     */
    <T> T readObject(byte[] bytes, Class<T> clazz);
}
