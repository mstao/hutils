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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static Class<?> getSuperClass(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getSuperclass();
    }

    public static Class<?> getSuperClass(Object source) {
        return getClass(source).getSuperclass();
    }

    public static Class<?>[] getAllSuperClass(Class<?> clazz) {
        Objects.requireNonNull(clazz);
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
        Class<?>[] superClasses = new Class<?>[listSuperClass.size()];
        return listSuperClass.toArray(superClasses);
    }

    public static Class<?>[] getAllSuperClass(String className) throws ClassNotFoundException {
        return getAllSuperClass(getClass(className));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    public static Constructor getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Objects.requireNonNull(clazz);
        return clazz.getConstructor(parameterTypes);
    }

    public static Constructor[] getConstructors(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getConstructors();
    }

    public static Constructor getDeclaredConstructor (Class<?> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Objects.requireNonNull(clazz);
        return clazz.getDeclaredConstructor(parameterTypes);
    }

    public static Constructor[] getDeclaredConstructors(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getDeclaredConstructors();
    }

    ///////////////////////////////////////////////////////////////////////////
    // newInstance
    ///////////////////////////////////////////////////////////////////////////

    public static Object newInstance(Class<?> clazz, Object... args) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Objects.requireNonNull(clazz);
        Constructor<?> constructor = getConstructor(clazz, getArgsType(args));
        return constructor.newInstance(args);
    }

    public static Object newInstance(String className, Object... args) throws ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = getClass(className);
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

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Objects.requireNonNull(clazz);
        return clazz.getField(name);
    }

    public static Field getDeclaredField(Class<?> clazz, String name) throws NoSuchFieldException {
        Objects.requireNonNull(clazz);
        return clazz.getDeclaredField(name);
    }

    public static Field[] getDeclaredFields(Class<?> clazz, String name) throws NoSuchFieldException {
        Objects.requireNonNull(clazz);
        return clazz.getDeclaredFields();
    }

    public static Field[] getAllFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != Object.class) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    public static Field[] getAccessibleField() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getConstValue(Class<?> clazz, String constName) throws NoSuchFieldException,
            IllegalAccessException {
        Objects.requireNonNull(clazz);
        Field field = ReflectUtil.getDeclaredField(clazz, constName);
        field.setAccessible(true);
        Object object = field.get(null);
        if (object != null) {
            return (T) object;
        }
        return null;
    }

    public static Object getPrivateProperty(Object source, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = getDeclaredField(getClass(source), fieldName);
        field.setAccessible(true);
        return field.get(source);
    }

    public static void setPrivateProperty(Object source, String name, Object value) throws IllegalAccessException,
            NoSuchFieldException {
        Field field = getDeclaredField(getClass(source), name);
        field.setAccessible(true);
        field.set(source, value);
    }

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
