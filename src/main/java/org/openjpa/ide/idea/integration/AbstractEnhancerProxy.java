package org.openjpa.ide.idea.integration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.module.Module;

import org.openjpa.ide.idea.PersistenceApi;

/**
 */
public abstract class AbstractEnhancerProxy implements EnhancerProxy {

    private final PersistenceApi api;

    private final CompileContext compileContext;

    private final Module module;

    private final String persistenceUnitName;

    @SuppressWarnings("RedundantThrowsDeclaration")
    protected AbstractEnhancerProxy(final PersistenceApi api,
                                    final CompileContext compileContext,
                                    final Module module,
                                    final String persistenceUnitName)
            throws IOException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException,
            NoSuchMethodException {

        this.api = api;
        this.compileContext = compileContext;
        this.module = module;
        this.persistenceUnitName = persistenceUnitName;
    }

    public final PersistenceApi getApi() {
        return this.api;
    }

    public final CompileContext getCompileContext() {
        return this.compileContext;
    }

    public final Module getModule() {
        return this.module;
    }

    public final String getPersistenceUnitName() {
        return this.persistenceUnitName;
    }

}
