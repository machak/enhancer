package org.openjpa.ide.idea.integration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.module.Module;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openjpa.ide.idea.PersistenceApi;

/**
 */
public abstract class AbstractEnhancerSupport implements EnhancerSupport {

    @Override
    public boolean isSupported(final PersistenceApi persistenceApi) {
        final List<PersistenceApi> supported = Arrays.asList(this.getPersistenceApis());
        return supported.contains(persistenceApi);
    }

    @Override
    @NotNull
    public List<String> getAnnotationNames() {
        final List<String> annotationNames = new ArrayList<String>(5);

        for (final PersistenceApi persistenceApi : this.getPersistenceApis()) {
            annotationNames.addAll(Arrays.asList(persistenceApi.getAnnotationClassNames()));
        }

        return annotationNames;
    }

    @Override
    @NotNull
    public PersistenceApi getDefaultPersistenceApi() {
        return this.getPersistenceApis()[0];
    }

    @Override
    @NotNull
    public EnhancerProxy newEnhancerProxy(final PersistenceApi api,
                                          final CompileContext compileCtx,
                                          final Module module,
                                          @Nullable final String persistenceUnitName)
            throws NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            InstantiationException {

        final Class<?> enhancerProxyClass = this.getEnhancerProxyClass();
        final Constructor<?> constructor = enhancerProxyClass
                .getConstructor(new Class[]{PersistenceApi.class, CompileContext.class, Module.class, String.class});

        return (EnhancerProxy) constructor.newInstance(api, compileCtx, module, persistenceUnitName);
    }

}
