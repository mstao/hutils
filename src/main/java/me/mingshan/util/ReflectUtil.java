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

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * The collection of reflect util.
 *
 * @author mingshan
 */
public class ReflectUtil {

    ///////////////////////////////////////////////////////////////////////////
    // Class
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns the runtime class of this {@code Object}.
     *
     * @param source the source object
     * @return the class of object
     */
    public static Class<?> getClass(Object source) {
        Objects.requireNonNull(source);
        return source.getClass();
    }

    /**
     * Returns the {@code Class} object associated with the class or
     * interface with the given string name. 
     * See {@link Class#forName(String)}
     *
     * @param className the specified class name
     * @return the class of object
     * @throws ClassNotFoundException if the class can not be located
     */
    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    /**
     * Returns the {@code Class} representing the direct superclass of the
     * entity (class, interface, primitive type or void) represented by
     * this {@code Class}. 
     * See {@link Class#getSuperclass()}
     *
     * @param clazz the specified class
     * @return the super class
     */
    public static Class<?> getSuperClass(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getSuperclass();
    }

    /**
     * Returns the {@code Class} representing the direct superclass of the
     * entity (class, interface, primitive type or void) represented by the given {@code Object}. 
     * See {@link Class#getSuperclass()}
     *
     * @param source the source object
     * @return the super class
     */
    public static Class<?> getSuperClass(Object source) {
        return getClass(source).getSuperclass();
    }

    /**
     * Returns the all superclass by this {@code Class}. Not including {@code Object}
     *
     * @param clazz the specified class
     * @return the array of all superclass
     */
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

    /**
     * Returns the all superclass by the specified class name. Not including {@code Object}
     *
     * @param className the specifed class name
     * @return the array of all superclass
     * @throws ClassNotFoundException if the class associated with the specified class name
     * can not be located.
     */
    public static Class<?>[] getAllSuperClass(String className) throws ClassNotFoundException {
        return getAllSuperClass(getClass(className));
    }

    /**
     * Returns the all actual generic type argument of the direct superclass
     * associated with the specified {@code Class}.
     *
     * @param clazz the specified class
     * @return the array of actual generic type argument, if not, returns {@code null}
     */
    public static Class<?>[] getSuperClassGenericTypes(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        Type genericType = clazz.getGenericSuperclass();

        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            return (Class[]) pt.getActualTypeArguments();
        }

        return null;
    }

    /**
     * Returns the first actual generic type argument of the direct superclass
     * associated with the specified {@code Class}.
     *
     * @param clazz the specified class
     * @return the array of actual generic type argument, if not, returns {@code null}
     */
    public static Class<?> getSuperClassFirstGenericType(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * Returns the first actual generic type argument of the direct superclass
     * associated with the specified {@code Class} by the specified index.
     *
     * @param clazz the specified class
     * @param index the specified index
     * @return actual generic type argument, if not, returns {@code null}
     */
    public static Class<?> getSuperClassGenericType(Class<?> clazz, int index) {
        Objects.requireNonNull(clazz);
        if (index < 0) {
            throw new IllegalArgumentException("Index: {" + index + "} is invalid.");
        }
        Class<?>[] types = getSuperClassGenericTypes(clazz);

        if (types == null) {
            return null;
        }

        if (types.length <= index) {
            throw new IllegalArgumentException("Index: {" + index + "} is out of range.");
        }

        return types[index];
    }

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns a {@code Constructor} object that reflects the specified
     * public constructor of the class represented by this {@code Class}
     * object. 
     * See {@link Class#getConstructor(Class[])}
     *
     * @param clazz the specified class
     * @param parameterTypes the parameter array
     * @return the {@code Constructor} object of the public constructor that
     *         matches the specified {@code parameterTypes}
     * @throws NoSuchMethodException if a matching method is not found.
     */
    public static Constructor getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Objects.requireNonNull(clazz);
        return clazz.getConstructor(parameterTypes);
    }

    /**
     * Returns an array containing {@code Constructor} objects reflecting
     * all the public constructors of the class represented by this
     * {@code Class} object. Just public constructors. 
     * See {@link Class#getConstructors()}
     *
     * @param clazz the specified class
     * @return an array containing {@code Constructor} objects
     */
    public static Constructor[] getConstructors(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getConstructors();
    }

    /**
     * Returns a {@code Constructor} object that reflects the specified
     * constructor of the class represented by this {@code Class}
     * object. 
     * See {@link Class#getDeclaredConstructor(Class[])}
     *
     * @param clazz the specified class
     * @param parameterTypes the parameter array
     * @return the {@code Constructor} object of the constructor that
     *         matches the specified {@code parameterTypes}
     * @throws NoSuchMethodException if a matching method is not found
     */
    public static Constructor getDeclaredConstructor (Class<?> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Objects.requireNonNull(clazz);
        return clazz.getDeclaredConstructor(parameterTypes);
    }

    /**
     * Returns an array containing {@code Constructor} objects reflecting
     * all constructors of the class represented by this
     * {@code Class} object. 
     * See {@link Class#getDeclaredConstructors()}
     *
     * @param clazz the specified class
     * @return an array containing {@code Constructor} objects
     */
    public static Constructor[] getDeclaredConstructors(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getDeclaredConstructors();
    }

    ///////////////////////////////////////////////////////////////////////////
    // newInstance
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns an instance of specified {@code class} and arguments array.
     *
     * See {@link Constructor#newInstance(Object...)}
     *
     * @param clazz the specified class
     * @param args an array of arguments
     * @return the instance
     * @throws NoSuchMethodException if a matching method is not found
     * @throws IllegalAccessException See {@link Constructor#newInstance(Object...)}
     * @throws InvocationTargetException See {@link Constructor#newInstance(Object...)}
     * @throws InstantiationException See {@link Constructor#newInstance(Object...)}
     */
    public static Object newInstance(Class<?> clazz, Object... args) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Objects.requireNonNull(clazz);
        Constructor<?> constructor = getConstructor(clazz, getArgsType(args));
        return constructor.newInstance(args);
    }

    /**
     * Returns an instance of specified class name and arguments array.
     *
     * See {@link Constructor#newInstance(Object...)}
     *
     * @param className the class name
     * @param args an array of arguments
     * @return the instance
     * @throws ClassNotFoundException See {@link Constructor#newInstance(Object...)}
     * @throws InvocationTargetException See {@link Constructor#newInstance(Object...)}
     * @throws NoSuchMethodException See {@link Constructor#newInstance(Object...)}
     * @throws InstantiationException See {@link Constructor#newInstance(Object...)}
     * @throws IllegalAccessException See {@link Constructor#newInstance(Object...)}
     */
    public static Object newInstance(String className, Object... args) throws ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = getClass(className);
        return newInstance(clazz, args);
    }

    /**
     * Gets types of given arguments.
     *
     * @param args the array of argument
     * @return the array of type
     */
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

    /**
     * Returns the public {@code Field} associated with the specified {@code Class} by field name.
     *
     * @param clazz the specified class
     * @param name the specified field name
     * @return the {@code Field} object
     * @throws NoSuchFieldException if a matching field can not found
     */
    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Objects.requireNonNull(clazz);
        return clazz.getField(name);
    }

    /**
     * Returns the {@code Field} associated with the specified {@code Class} by field name.
     *
     * @param clazz the specified class
     * @param name the specified field name
     * @return the {@code Field} object
     * @throws NoSuchFieldException if a matching field can not found
     */
    public static Field getDeclaredField(Class<?> clazz, String name) throws NoSuchFieldException {
        Objects.requireNonNull(clazz);
        return clazz.getDeclaredField(name);
    }

    /**
     * Returns all {@code Field} associated with the specified {@code Class} by field name.
     *
     * @param clazz the specified class
     * @return the array of {@code Field} object
     * @throws NoSuchFieldException if a matching field can not found
     */
    public static Field[] getDeclaredFields(Class<?> clazz) throws NoSuchFieldException {
        Objects.requireNonNull(clazz);
        return clazz.getDeclaredFields();
    }

    /**
     * Returns all {@code Field} associated with the specified {@code Class} and all super classes by field name.
     * Not including {@code Object}.
     *
     * @param clazz the specified class
     * @return the array of {@code Field} object
     */
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

    /**
     * Returns the const value associated with the specified {@code Class} by const name.
     *
     * @param clazz the specified class
     * @param constName the const name
     * @param <T> the generic type
     * @return the const value
     * @throws NoSuchFieldException if a matching field can not found
     * @throws IllegalAccessException See {@link IllegalAccessException}
     */
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

    /***
     * Returns the value of private field associated with the specified {@code Object} by the field name.
     *
     * @param source the source object
     * @param fieldName the field name
     * @return the value of private field
     * @throws NoSuchFieldException if a matching field can not found
     * @throws IllegalAccessException See {@link IllegalAccessException}
     */
    public static Object getPrivateProperty(Object source, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = getDeclaredField(getClass(source), fieldName);
        field.setAccessible(true);
        return field.get(source);
    }

    /**
     * Sets the specified value of private field associated with the specified {@code Object} by the field name.
     *
     * @param source the source object
     * @param fieldName the field name
     * @param value the value of field
     * @throws NoSuchFieldException if a matching field can not found
     * @throws IllegalAccessException See {@link IllegalAccessException}
     */
    public static void setPrivateProperty(Object source, String fieldName, Object value) throws IllegalAccessException,
            NoSuchFieldException {
        Field field = getDeclaredField(getClass(source), fieldName);
        field.setAccessible(true);
        field.set(source, value);
    }

    /**
     * Returns the first generic type of field.
     *
     * @param field the specified field
     * @return the generic type of field, if not, returns {@code null}
     */
    public static Class<?> getFieldFirstGenericClass(Field field) {
        Class<?>[] fieldGenericTypes = getFieldGenericTypes(field);
        if (fieldGenericTypes != null) {
            return fieldGenericTypes[0];
        }

        return null;
    }

    /**
     * Returns an array of all generic type of field.
     *
     * @param field the specified field
     * @return the array of generic type, if not, returns {@code null}
     */
    public static Class<?>[] getFieldGenericTypes(Field field) {
        Objects.requireNonNull(field);
        Type fc = field.getGenericType();
        if (fc instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) fc;
            return (Class[]) pt.getActualTypeArguments();
        }

        return null;
    }

    /**
     * Returns the generic type of field by specified index.
     *
     * @param field the specified field
     * @param index the specified index
     * @return the generic type of field, if not, returns {@code null}
     */
    public static Class<?> getFieldGenericType(Field field, int index) {
        Objects.requireNonNull(field);
        if (index < 0) {
            throw new IllegalArgumentException("Index: {" + index + "} is invalid.");
        }
        Class<?>[] types = getFieldGenericTypes(field);

        if (types == null) {
            return null;
        }

        if (types.length <= index) {
            throw new IllegalArgumentException("Index: {" + index + "} is out of range.");
        }

        return types[index];
    }

    ///////////////////////////////////////////////////////////////////////////
    // Method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Gets declared method from specified type by mame and parameters types.
     *
     * @param type           the type
     * @param methodName     the name of the method
     * @param parameterTypes the parameter array
     * @return a {@link Method} object or null if method doesn't exist
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Method getDeclaredMethod(Class<?> type, String methodName, Class<?>... parameterTypes)
        throws IOException, ClassNotFoundException {
        Method method = null;
        try {
            method = type.getDeclaredMethod(methodName, parameterTypes);
            if (method.isBridge()) {
                // Uses Asm to unbride bride method.
                method = MethodProvider.getInstance().unbride(method, type);
            }
        } catch (NoSuchMethodException e) {
            Class<?> superclass = type.getSuperclass();
            if (superclass != null) {
                method = getDeclaredMethod(superclass, methodName, parameterTypes);
            }
        }
        return method;
    }

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

    public Class<?> boxing(final Class<?> type) {
        if (type == null) {
            return null;
        } else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }
        return type;
    }
}
