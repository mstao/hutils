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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The collection of reflect util.
 *
 * @author mingshan
 */
public class ReflectUtil {

    ///////////////////////////////////////////////////////////////////////////
    // Class
    ///////////////////////////////////////////////////////////////////////////

    public static Class<?> getClass(Object source) {
        Objects.requireNonNull(source);
        return source.getClass();
    }

    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    public static Class<?> getSuperClass(Object source) {
        return getClass(source).getSuperclass();
    }

    public static List<Class<?>> getAllSuperClass(Class<?> clazz) {
        List<Class<?>> listSuperClass = new ArrayList<>();

        Class<?> superClass = clazz.getSuperclass();
        listSuperClass.add(superClass);
        while (superClass != null) {
            if (Object.class.getName().equals(superClass.getName())){
                break;
            }
            listSuperClass.add(superClass.getSuperclass());
            superClass = superClass.getSuperclass();
        }

        return listSuperClass;
    }

    public static List<Class<?>>  getAllSuperClass(String className) throws ClassNotFoundException {
        return getAllSuperClass(getClass(className));
    }


    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    public static Constructor getConstructor(Class<?> clazz) throws NoSuchMethodException {
        Objects.requireNonNull(clazz);
        return clazz.getConstructor();
    }

    public static Constructor getConstructor(Class<?> clazz, Class<?> parameterTypes) throws NoSuchMethodException {
        Objects.requireNonNull(clazz);
        if (parameterTypes == null) {
            return getConstructor(clazz);
        }
        return clazz.getConstructor(parameterTypes);
    }

    public static Constructor[] getConstructors(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getConstructors();
    }

    ///////////////////////////////////////////////////////////////////////////
    // newInstance
    ///////////////////////////////////////////////////////////////////////////

    public static Object newInstance(Class<?> clazz) throws IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        Objects.requireNonNull(clazz);
        Constructor constructor = getConstructor(clazz);
        return constructor.newInstance();
    }

    public static Object newInstance(Class<?> clazz, Object... args) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Objects.requireNonNull(clazz);
        Constructor<?> constructor = clazz.getConstructor(getArgsType(args));
        return constructor.newInstance(args);
    }

    public static Object newInstance(String className) throws ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        return newInstance(clazz);
    }

    public static Object newInstance(String className, Object... args) throws ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        return newInstance(clazz, args);
    }

    private static Class<?>[] getArgsType(Object... args) {
        if (args == null) {
            return new Class<?>[0];
        }
        Class<?>[] argsTypes = new Class<?>[args.length];

        for (int i = 0; i < args.length; i++) {
            argsTypes[i] = args.getClass();
        }

        return argsTypes;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Field
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // Method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Gets access level by modifier.
     *
     * @param modifiers the modifier
     * @return the access level
     */
    public static String getAccessLevel(int modifiers) {
        if (Modifier.isPublic(modifiers)) {
            return "public";
        } else if (Modifier.isProtected(modifiers)) {
            return "protected";
        } else if (Modifier.isPrivate(modifiers)) {
            return "private";
        } else {
            return "default";
        }
    }
}
