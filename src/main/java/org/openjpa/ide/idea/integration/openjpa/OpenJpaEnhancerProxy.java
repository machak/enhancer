package org.openjpa.ide.idea.integration.openjpa;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.module.Module;

import org.openjpa.ide.idea.PersistenceApi;
import org.openjpa.ide.idea.integration.AbstractEnhancerProxy;
import org.openjpa.ide.idea.integration.ClassLoaderFactory;
import org.openjpa.ide.idea.util.InternalReflectionHelper;


public class OpenJpaEnhancerProxy extends AbstractEnhancerProxy {

    private static final Class<?>[] NO_PARAMETER_TYPES = {};

    public static final String OPEN_JPA_ENHANCER_CLASS = "PCEnhancer";


    public static final String OPEN_JPA_GENERIC_ENHANCER_CLASS_FQ = "org.apache.openjpa.enhance." + OPEN_JPA_ENHANCER_CLASS;

    private final Object enhancer;


    @SuppressWarnings("UnusedParameters")
    public OpenJpaEnhancerProxy(final PersistenceApi api,
                                final CompileContext compileContext,
                                final Module module,
                                final String persistenceUnitName)
            throws IOException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException,
            NoSuchMethodException {

        super(api, compileContext, module, persistenceUnitName);

        final ClassLoader classLoader = ClassLoaderFactory.newClassLoader(compileContext, module, OpenJpaEnhancerProxy.class);
        final Class<?> enhancerClass = Class.forName(OPEN_JPA_GENERIC_ENHANCER_CLASS_FQ, true, classLoader);
        final Constructor<?> constructor = enhancerClass.getConstructor(String.class, String.class);
        this.enhancer = constructor.newInstance(api.name(), "ASM");
        // log to system out
        this.invokeMethod("setVerbose", new Class[]{Boolean.TYPE}, true);

        this.invokeMethod("setSystemOut", new Class[]{Boolean.TYPE}, true);
        this.invokeMethod("setClassLoader", new Class[]{ClassLoader.class}, classLoader);
    }

    //
    // Method implementation
    //

    @Override
    public void addClasses(final String... classNames) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final Object parameter = convertVarargsParameter(classNames);
        final Object parameterClassesArray = Array.newInstance(String.class, 0);

        invokeMethod("addClasses", new Class[]{parameterClassesArray.getClass()}, parameter);
    }

    @Override
    public void addMetadataFiles(final String... metadataFiles) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final Object parameter = convertVarargsParameter(metadataFiles);
        final Object parameterClassesArray = Array.newInstance(String.class, 0);

        invokeMethod("addFiles", new Class[]{parameterClassesArray.getClass()}, parameter);
    }

    @Override
    public int enhance() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return (Integer) invokeMethod("enhance", NO_PARAMETER_TYPES);
    }

    @Override
    public String toString() {
        return "OpenJpaEnhancerProxy";
    }

    //
    // Helper methods
    //

    private Object invokeMethod(final String methodName, final Class<?>[] parameterTypes, final Object... parameters)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        return InternalReflectionHelper.invokeMethod(this.enhancer, methodName, parameterTypes, parameters);
    }

    private static Object convertVarargsParameter(final String[] metadataFiles) {
        final Object parameter = Array.newInstance(String.class, metadataFiles.length);
        for (int i = 0; i < metadataFiles.length; ++i) {
            Array.set(parameter, i, metadataFiles[i]);
        }
        return parameter;
    }

}
