/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.mingshan.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by dmgcodevil
 */
public final class MethodProvider {

    private MethodProvider() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    private static class MethodProviderHolder {
        private static final MethodProvider INSTANCE = new MethodProvider();
    }

    public static MethodProvider getInstance() {
        return MethodProviderHolder.INSTANCE;
    }

    private Map<Method, Method> cache = new ConcurrentHashMap<Method, Method>();

    /**
     * Gets method by name and parameters types using reflection,
     * if the given type doesn't contain required method then continue applying this method for all super classes up to Object class.
     *
     * @param type           the type to search method
     * @param name           the method name
     * @param parameterTypes the parameters types
     * @return Some if method exists otherwise None
     */
    public static Optional<Method> getMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        Method[] methods = type.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(name) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                return Optional.of(method);
            }
        }
        Class<?> superClass = type.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            return getMethod(superClass, name, parameterTypes);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Finds generic method for the given bridge method.
     *
     * @param bridgeMethod the bridge method
     * @param aClass       the type where the bridge method is declared
     * @return generic method
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    public Method unbride(final Method bridgeMethod, Class<?> aClass) throws IOException, NoSuchMethodException, ClassNotFoundException {
        if (bridgeMethod.isBridge() && bridgeMethod.isSynthetic()) {
            if (cache.containsKey(bridgeMethod)) {
                return cache.get(bridgeMethod);
            }

            ClassReader classReader = new ClassReader(aClass.getName());
            final MethodSignature methodSignature = new MethodSignature();
            classReader.accept(new ClassVisitor(ASM5) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    boolean bridge = (access & ACC_BRIDGE) != 0 && (access & ACC_SYNTHETIC) != 0;
                    if (bridge && bridgeMethod.getName().equals(name) && getParameterCount(desc) == bridgeMethod.getParameterTypes().length) {
                        return new MethodFinder(methodSignature);
                    }
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }, 0);
            Method method = aClass.getDeclaredMethod(methodSignature.name, methodSignature.getParameterTypes());
            cache.put(bridgeMethod, method);
            return method;

        } else {
            return bridgeMethod;
        }
    }

    private static int getParameterCount(String desc) {
        return parseParams(desc).length;
    }

    private static String[] parseParams(String desc) {
        String params = desc.split("\\)")[0].replace("(", "");
        if (params.length() == 0) {
            return new String[0];
        }
        return params.split(";");
    }

    private static class MethodSignature {
        String name;
        String desc;

        public Class<?>[] getParameterTypes() throws ClassNotFoundException {
            if (desc == null) {
                return new Class[0];
            }
            String[] params = parseParams(desc);
            Class<?>[] parameterTypes = new Class[params.length];

            for (int i = 0; i < params.length; i++) {
                String arg = params[i].substring(1).replace("/", ".");
                parameterTypes[i] = Class.forName(arg);
            }
            return parameterTypes;
        }
    }

    private static class MethodFinder extends MethodVisitor {
        private MethodSignature methodSignature;

        public MethodFinder(MethodSignature methodSignature) {
            super(ASM5);
            this.methodSignature = methodSignature;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            methodSignature.name = name;
            methodSignature.desc = desc;
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}
