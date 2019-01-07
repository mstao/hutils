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
package me.mingshan.util.cache.caffeine;


import me.mingshan.util.cache.Cache;

import java.util.Objects;

/**
 * The cache implementation with Caffeine.
 *
 * @author mingshan
 */
public class CaffeineCache implements Cache {
    private com.github.benmanes.caffeine.cache.Cache<Object, Object> cache;

    /**
     * No Public.
     */
    public CaffeineCache() {
        cache = CaffeineConfig.getCache();
    }

    /**
     * Inner class for Lazy load.
     */
    private static final class CaffeineCacheHolder {
        private static final CaffeineCache INSTANCE = new CaffeineCache();
    }

    /**
     * Returns the instance of {@link CaffeineCache}.
     *
     * @return the instance of {@link CaffeineCache}.
     */
    public static CaffeineCache getInstance() {
        return CaffeineCacheHolder.INSTANCE;
    }

    @Override
    public String getName() {
        return CaffeineCache.class.getSimpleName();
    }

    @Override
    public Object get(Object key) {
        Objects.requireNonNull(key);
        return this.cache.getIfPresent(key);
    }

    @Override
    public void put(Object key, Object value) {
        Objects.requireNonNull(key);
        this.cache.put(key, value);
    }

    @Override
    public Object putIfPresent(Object key, Object value) {
        Objects.requireNonNull(key);
        Object existingValue = this.cache.getIfPresent(key);
        if (existingValue == null) {
            cache.put(key, value);
            return null;
        } else {
            return existingValue;
        }
    }

    @Override
    public void evict(Object key) {
        Objects.requireNonNull(key);
        this.cache.invalidate(key);
    }

    @Override
    public void clear() {
        this.cache.invalidateAll();
    }
}
