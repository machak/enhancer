package org.openjpa.ide.idea.integration.openjpa;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.module.Module;

import org.apache.openjpa.enhance.PCEnhancer;
import org.apache.openjpa.lib.util.Options;
import org.openjpa.ide.idea.PersistenceApi;
import org.openjpa.ide.idea.integration.AbstractEnhancerProxy;
import org.openjpa.ide.idea.integration.ClassLoaderFactory;


public class OpenJpaEnhancerProxy extends AbstractEnhancerProxy {


    public static final String OPEN_JPA_ENHANCER_CLASS = "PCEnhancer";
    public static final String OPEN_JPA_GENERIC_ENHANCER_CLASS_FQ = "org.apache.openjpa.enhance." + OPEN_JPA_ENHANCER_CLASS;

    private boolean addDefaultConstructor;
    private static final String OPTION_ADD_DEFAULT_CONSTRUCTOR = "addDefaultConstructor";
    private boolean enforcePropertyRestrictions;
    private static final String OPTION_ENFORCE_PROPERTY_RESTRICTION = "enforcePropertyRestrictions";


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
        opts.put(OPTION_ADD_DEFAULT_CONSTRUCTOR, Boolean.toString(addDefaultConstructor));
        opts.put(OPTION_ENFORCE_PROPERTY_RESTRICTION, Boolean.toString(enforcePropertyRestrictions));
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
        boolean done = PCEnhancer.run(classes.toArray(new String[classes.size()]), createOptions());
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

    @Override
    public String toString() {
        return "OpenJpaEnhancerProxy";
    }


}
