package me.mingshan.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides common methods to retrieve information from JoinPoint and not only.
 */
public class AopUtil {

    private AopUtil() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    /**
     * Gets a {@link Method} object from target object (not proxy class).
     *
     * @param joinPoint the {@link JoinPoint}
     * @return a {@link Method} object or null if method doesn't exist or if the signature at a join point
     *         isn't sub-type of {@link MethodSignature}
     */
    public static Method getMethodFromTarget(JoinPoint joinPoint) {
        Method method = null;
        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            method = getDeclaredMethod(joinPoint.getTarget().getClass(), signature.getName(),
                    getParameterTypes(joinPoint));
        }
        return method;
    }

    /**
     * Gets a {@link Method} object from target object by specified method name.
     *
     * @param joinPoint  the {@link JoinPoint}
     * @param methodName the method name
     * @return a {@link Method} object or null if method with specified <code>methodName</code> doesn't exist
     */
    public static Method getMethodFromTarget(JoinPoint joinPoint, String methodName) {
        return getDeclaredMethod(joinPoint.getTarget().getClass(), methodName,
                getParameterTypes(joinPoint));
    }

    /**
     * Gets parameter types of the join point.
     *
     * @param joinPoint the join point
     * @return the parameter types for the method this object
     *         represents
     */
    public static Class[] getParameterTypes(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getParameterTypes();
    }

    /**
     * Gets parameter array of the join point.
     *
     * @param joinPoint the join point
     * @return the parameter array
     */
    public static Object[] getParameters(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

    /**
     * Gets declared method from specified type by mame and parameters types.
     *
     * @param type           the type
     * @param methodName     the name of the method
     * @param parameterTypes the parameter array
     * @return a {@link Method} object or null if method doesn't exist
     */
    public static Method getDeclaredMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = type.getDeclaredMethod(methodName, parameterTypes);
            if(method.isBridge()){
                // Uses Asm to unbride bride method.
                method = MethodProvider.getInstance().unbride(method, type);
            }
        } catch (NoSuchMethodException e) {
            Class<?> superclass = type.getSuperclass();
            if (superclass != null) {
                method = getDeclaredMethod(superclass, methodName, parameterTypes);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return method;
    }

    /**
     * Gets annotation by {@code JoinPoint} and the class object of annotation.
     *
     * @param joinPoint the join point
     * @param annotation the class object of annotation
     * @param <T> the annotation
     * @return the annotation
     */
    public static <T extends Annotation> Optional<T> getAnnotation(JoinPoint joinPoint, Class<T> annotation) {
        return getAnnotation(joinPoint.getTarget().getClass(), annotation);
    }

    /**
     * Gets annotation by class and the class object of annotation.
     *
     * @param type the type
     * @param annotation the class object of annotation
     * @param <T> the annotation
     * @return the annotation
     */
    public static <T extends Annotation> Optional<T> getAnnotation(Class<?> type, Class<T> annotation) {
        Objects.requireNonNull(annotation, "annotation cannot be null");
        Objects.requireNonNull(type, "type cannot be null");
        Annotation[] annotation1 = type.getDeclaredAnnotations();
        for (Annotation ann : type.getDeclaredAnnotations()) {
            if (ann.annotationType().equals(annotation)) return Optional.of((T) ann);
        }

        Class<?> superType = type.getSuperclass();
        if (superType != null && !superType.equals(Object.class)) {
            return getAnnotation(superType, annotation);
        }

        return Optional.empty();
    }

    /**
     * Gets annotation by {@code JoinPoint} and the method of annotation.
     *
     * @param joinPoint the join point
     * @param annotation the annotation
     * @param <T> the annotation
     * @return the annotation
     */
    public static <T extends Annotation> T getMethodAnnotation(JoinPoint joinPoint, Class<T> annotation) {
        Objects.requireNonNull(annotation, "annotation cannot be null");
        return getMethodFromTarget(joinPoint).getAnnotation(annotation);
    }

    /**
     * Gets annotation by {@code Method} and the method of annotation.
     *
     * @param method the method
     * @param annotation the annotation
     * @param <T> the annotation
     * @return the annotation
     */
    public static <T extends Annotation> T getMethodAnnotation(Method method, Class<T> annotation) {
        Objects.requireNonNull(method, "method cannot be null");
        Objects.requireNonNull(annotation, "annotation cannot be null");
        return method.getAnnotation(annotation);
    }

    /**
     * Prints method information.
     *
     * @param m the method
     * @return the string of method information
     */
    public static String getMethodInfo(Method m) {
        StringBuilder info = new StringBuilder();
        info.append("Method signature:").append("\n");
        info.append(m.toGenericString()).append("\n");

        info.append("Declaring class:\n");
        info.append(m.getDeclaringClass().getCanonicalName()).append("\n");

        info.append("\nFlags:").append("\n");
        info.append("Bridge=").append(m.isBridge()).append("\n");
        info.append("Synthetic=").append(m.isSynthetic()).append("\n");
        info.append("Final=").append(Modifier.isFinal(m.getModifiers())).append("\n");
        info.append("Native=").append(Modifier.isNative(m.getModifiers())).append("\n");
        info.append("Synchronized=").append(Modifier.isSynchronized(m.getModifiers())).append("\n");
        info.append("Abstract=").append(Modifier.isAbstract(m.getModifiers())).append("\n");
        info.append("AccessLevel=").append(getAccessLevel(m.getModifiers())).append("\n");

        info.append("\nReturn Type: \n");
        info.append("ReturnType=").append(m.getReturnType()).append("\n");
        info.append("GenericReturnType=").append(m.getGenericReturnType()).append("\n");

        info.append("\nParameters:");
        Class<?>[] pType = m.getParameterTypes();
        Type[] gpType = m.getGenericParameterTypes();
        if (pType.length != 0) {
            info.append("\n");
        } else {
            info.append("empty\n");
        }
        for (int i = 0; i < pType.length; i++) {
            info.append("parameter [").append(i).append("]:\n");
            info.append("ParameterType=").append(pType[i]).append("\n");
            info.append("GenericParameterType=").append(gpType[i]).append("\n");
        }

        info.append("\nExceptions:");
        Class<?>[] xType = m.getExceptionTypes();
        Type[] gxType = m.getGenericExceptionTypes();
        if (xType.length != 0) {
            info.append("\n");
        } else {
            info.append("empty\n");
        }
        for (int i = 0; i < xType.length; i++) {
            info.append("exception [").append(i).append("]:\n");
            info.append("ExceptionType=").append(xType[i]).append("\n");
            info.append("GenericExceptionType=").append(gxType[i]).append("\n");
        }

        info.append("\nAnnotations:");
        if (m.getAnnotations().length != 0) {
            info.append("\n");
        } else {
            info.append("empty\n");
        }

        for (int i = 0; i < m.getAnnotations().length; i++) {
            info.append("annotation[").append(i).append("]=").append(m.getAnnotations()[i]).append("\n");
        }

        return info.toString();
    }

    /**
     * Gets access level by modifier.
     *
     * @param modifiers the modifier
     * @return the access level
     */
    private static String getAccessLevel(int modifiers) {
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