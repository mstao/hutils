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
package me.mingshan.util.cache;

/**
 * Interface that defines common cache operations.
 *
 * @author mingshan
 */
public interface Cache {

    /**
     * Gets the name of cache.
     *
     * @return the name of cache.
     */
    String getName();

    /**
     * Gets the value by specified key from cache.
     *
     * @param key the specified key
     * @return the value which is associated with the specified key
     */
    Object get(Object key);

    /**
     * Puts key and value into cache.
     *
     * @param key the specified key
     * @param value the value which is associated with the specified key
     */
    void put(Object key, Object value);

    /**
     * Atomically associate the specified value with the specified key in this cache
     * if it is not set already.
     * <p>This is equivalent to:
     * <pre><code>
     * Object existingValue = cache.get(key);
     * if (existingValue == null) {
     *     cache.put(key, value);
     *     return null;
     * } else {
     *     return existingValue;
     * }
     * </code></pre>
     *
     * @param key the specified key
     * @param value the value which is associated with the specified key
     * @return if the value which is associated with the specified key is {@code null},
     * return {@code null}, if the value is existed, return the existed value.
     */
    Object putIfPresent(Object key, Object value);

    /**
     * Evicts the mapping for this key from this cache if it is present.
     *
     * @param key the key whose mapping is to be removed from the cache
     */
    void evict(Object key);

    /**
     * Removes all mappings from the cache.
     */
    void clear();
}
