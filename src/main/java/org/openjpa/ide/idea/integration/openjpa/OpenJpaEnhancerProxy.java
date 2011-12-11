package org.openjpa.ide.idea.integration.openjpa;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.module.Module;

import org.apache.openjpa.lib.util.Options;
import org.openjpa.ide.idea.PersistenceApi;
import org.openjpa.ide.idea.integration.AbstractEnhancerProxy;
import org.openjpa.ide.idea.integration.ClassLoaderFactory;


public class OpenJpaEnhancerProxy extends AbstractEnhancerProxy {

    private static final Class<?>[] NO_PARAMETER_TYPES = {};
    public static final String OPEN_JPA_ENHANCER_CLASS = "PCEnhancer";
    public static final String OPEN_JPA_GENERIC_ENHANCER_CLASS_FQ = "org.apache.openjpa.enhance." + OPEN_JPA_ENHANCER_CLASS;

    private final Class<?> enhancerClass;

    private List<String> classes = new ArrayList<String>();

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
        enhancerClass = Class.forName(OPEN_JPA_GENERIC_ENHANCER_CLASS_FQ, true, classLoader);

    }


    protected Options createOptions() {
        Options opts = new Options();
        return opts;
    }

    @Override
    public void addClasses(final String... classNames) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        classes.addAll(Arrays.asList(classNames));
    }

    @Override
    public void addMetadataFiles(final String... metadataFiles) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // do nothing
    }

    @Override
    public int enhance() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        return 0;
    }

    @Override
    public String toString() {
        return "OpenJpaEnhancerProxy";
    }


}
