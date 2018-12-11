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
package me.mingshan.util;

import java.util.Objects;

/**
 * Provides common methods to operate class.
 *
 * @author mingshan
 */
public class ClassUtil {

    private ClassUtil() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    /**
     * Gets class loader.
     *
     * @param clazz the current class.
     * @return the class loader
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        ClassLoader c1 = Thread.currentThread().getContextClassLoader();

        if (c1 == null) {
            c1 = clazz.getClassLoader();
            if (c1 == null) {
                c1 = ClassLoader.getSystemClassLoader();
            }
        }

        return c1;
    }

    /**
     * Gets class loader.
     *
     * @return the class loader.
     */
    public static ClassLoader getClassLoader() {
        return getClassLoader(ClassUtil.class);
    }
}
