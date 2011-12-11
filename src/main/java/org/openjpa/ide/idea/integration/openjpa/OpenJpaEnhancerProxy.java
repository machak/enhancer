package org.openjpa.ide.idea.integration.openjpa;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.module.Module;

import org.openjpa.ide.idea.PersistenceApi;
import org.openjpa.ide.idea.integration.AbstractEnhancerProxy;
import org.openjpa.ide.idea.integration.ClassLoaderFactory;


public class OpenJpaEnhancerProxy extends AbstractEnhancerProxy {


    public static final String OPEN_JPA_ENHANCER_CLASS = "PCEnhancer";
    public static final String OPEN_JPA_GENERIC_ENHANCER_CLASS_FQ = "org.apache.openjpa.enhance." + OPEN_JPA_ENHANCER_CLASS;

    private boolean addDefaultConstructor;
    private static final String OPTION_ADD_DEFAULT_CONSTRUCTOR = "addDefaultConstructor";
    //
    private static final String OPTION_ENFORCE_PROPERTY_RESTRICTION = "enforcePropertyRestrictions";
    private boolean enforcePropertyRestrictions;
    //
    private static final String OPTION_USE_TMP_CLASSLOADER = "tcl";
    private boolean tmpClassLoader = true;


    private final Class<?> configClass;
    private final Class<?> enhancerClass;
    private final Class<?> optionsClass;
    private final Class<?> configParamClass;

    private List<String> classes = new ArrayList<String>();

    final ClassLoader classLoader;

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


        classLoader = ClassLoaderFactory.newClassLoader(compileContext, module, OpenJpaEnhancerProxy.class);
        enhancerClass = Class.forName(OPEN_JPA_GENERIC_ENHANCER_CLASS_FQ, true, classLoader);
        optionsClass = Class.forName("org.apache.openjpa.lib.util.Options", true, classLoader);
        configParamClass = Class.forName("org.apache.openjpa.conf.OpenJPAConfiguration", true, classLoader);
        configClass = Class.forName("org.apache.openjpa.conf.OpenJPAConfigurationImpl", true, classLoader);


    }


    protected Object createOptions() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        Object options = optionsClass.newInstance();
        Method method = optionsClass.getMethod("setProperty", String.class, boolean.class);
        method.invoke(options, OPTION_ADD_DEFAULT_CONSTRUCTOR, addDefaultConstructor);
        method.invoke(options, OPTION_ENFORCE_PROPERTY_RESTRICTION, enforcePropertyRestrictions);
        method.invoke(options, OPTION_USE_TMP_CLASSLOADER, tmpClassLoader);
        return options;
    }

    private Object createConfig() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object config = configClass.newInstance();
        Method setSpecification = configClass.getMethod("setSpecification", String.class);
        setSpecification.invoke(config, "jpa");
        return config;
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
    public int enhance() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, ClassNotFoundException {

        Object options = createOptions();
        Object jpaConfig = createConfig();
        String[] args = classes.toArray(new String[classes.size()]);
        Method method = enhancerClass.getMethod("run", configParamClass, String[].class, options.getClass());

        Boolean done = (Boolean) method.invoke(null, jpaConfig, args, options);
        if (done) {
            return classes.size();
        }
        return 0;


    }


    public void setAddDefaultConstructor(boolean addDefaultConstructor) {
        this.addDefaultConstructor = addDefaultConstructor;
    }

    public void setEnforcePropertyRestrictions(boolean enforcePropertyRestrictions) {
        this.enforcePropertyRestrictions = enforcePropertyRestrictions;
    }

    public void setTmpClassLoader(boolean tmpClassLoader) {
        this.tmpClassLoader = tmpClassLoader;
    }

    @Override
    public String toString() {
        return "OpenJpaEnhancerProxy";
    }


}
