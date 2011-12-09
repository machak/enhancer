package org.openjpa.ide.idea.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Most of the reflection/bean utils use caching of classes, which leads to ClassLoader memory leaks
 * when accessing enhancer instances created by a different ClassLoader.<br/>
 * Using our own reflection util seems to be the only solution, as it's not certain, that any
 * utility used inside this plugin wouldn't switch to use caching.<br/>
 * TODO: this hack seems to fulfil our requirements, nevertheless, there's a lot room for improvement
 */
public abstract class InternalReflectionHelper {

    private InternalReflectionHelper() {
        // prohibit instantiation
    }

    public static Method getDeclaredMethod(final Object object, final String name, final Class<?>... paramTypes)
            throws NoSuchMethodException {
        Method method = null;
        Class<?> clazz = object.getClass();

        // TODO: this is a hack, implement better solution
        do {
            try {
                method = clazz.getDeclaredMethod(name, paramTypes);
            } catch (Exception ignored) {
            }
        } while (method == null && (clazz = clazz.getSuperclass()) != null);

        if (method == null) {
            throw new NoSuchMethodException(object.getClass() + "." + name + '(' + Arrays.toString(paramTypes) + ')');
        }

        return method;
    }

    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] paramTypes, final Object[] parameters)
            throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        final Method method = getDeclaredMethod(object, methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(object, parameters);
    }

}
