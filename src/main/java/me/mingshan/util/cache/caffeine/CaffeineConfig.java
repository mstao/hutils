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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * The configuration of Caffeine cache.
 *
 * @author mingshan
 */
public class CaffeineConfig {
    // The capacity of Caffeine cache.
    private static final int MAXINUM_SIZE = 10_000;

    // The expire time of value.
    private static final int EXPIRE_TIME = 5;

    // The unit of expire time.
    private static final TimeUnit EXPIRE_TIME_UNIT = TimeUnit.MINUTES;

    // The refresh time of value.
    private static final int REFRESH_TIME = 1;

    // The unit of refresh time.
    private static final TimeUnit REFRESH_TIME_UNIT = TimeUnit.MINUTES;

    private static Cache<Object, Object> cache;

    public static Cache<Object, Object> getCache() {
        if (cache == null) {
            synchronized (CaffeineConfig.class) {
                if (cache == null) {
                    cache = Caffeine.newBuilder()
                        .maximumSize(MAXINUM_SIZE)
                        .expireAfterWrite(EXPIRE_TIME, EXPIRE_TIME_UNIT)
                        //.refreshAfterWrite( REFRESH_TIME, REFRESH_TIME_UNIT)
                        .build();
                }
            }
        }

        return cache;
    }
}
