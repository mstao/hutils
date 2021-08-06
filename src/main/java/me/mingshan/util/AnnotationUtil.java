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

import java.lang.annotation.Annotation;

/**
 * @author mingshan
 */
public class AnnotationUtil {

    private AnnotationUtil() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }


    /**
     * 获取给定类{@code Class}上指定的注解，如果不存在，返回null
     *
     * @param clazz          指定的class
     * @param annotationType 指定的注解类
     * @param <A>            注解类型
     * @return 注解
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        if (clazz == null || annotationType == null) {
            return null;
        }

        A annotation = clazz.getDeclaredAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }

        Class<?> superclass = clazz.getSuperclass();
        return superclass != null && superclass != Object.class ? findAnnotation(superclass, annotationType) : null;
    }

}
