package org.openjpa.ide.idea.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.module.Module;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openjpa.ide.idea.PersistenceApi;

/**
 * Interface to implement for every new enhancer to support.<br/>
 * See {@link AbstractEnhancerSupport}.
 */
public interface EnhancerSupport {

    public static final String EXTENSION_POINT_NAME = "OpenJpaIntegration.openjpaEnhancerExtension";

    /**
     * The name to display in the configuration dialog enhancer support drop-down.
     *
     * @return Enhancer support name
     */
    @NotNull
    String getId();

    /**
     * The name to display in the configuration dialog enhancer support drop-down.
     *
     * @return Enhancer support name
     */
    @NotNull
    String getName();

    /**
     * API's supported by this enhancer integration, also see {@link org.openjpa.ide.idea.PersistenceApi}.
     *
     * @return supported API's
     */
    @NotNull
    PersistenceApi[] getPersistenceApis();

    /**
     * Checks if provided persistence api is supported by actual enhancer integration.
     *
     * @param persistenceApi the api to check support for
     * @return true if supported
     */
    boolean isSupported(PersistenceApi persistenceApi);

    /**
     * Get the class name of the enhancer proxy.<br/>
     * <p/>
     * Used to instantiate the proxies.
     *
     * @return the proxy class
     */
    @NotNull
    Class<?> getEnhancerProxyClass();

    /**
     * Get the default persistence api.
     *
     * @return .
     */
    @NotNull
    PersistenceApi getDefaultPersistenceApi();

    /**
     * Persistence implementations may use different enhancer classes per API.<br/>
     * This method delivers all fully qualified class names of enhancer classes to be used for enhancement.<br/>
     * <br/>
     * By now this classes are only used to find enhancer support in project modules.<br/>
     *
     * @return Array of fully qualified enhancer class names
     */
    @NotNull
    String[] getEnhancerClassNames();

    /**
     * Annotations this enhancer implementation supports.
     *
     * @return List of fully qualified annotation class names
     */
    @NotNull
    List<String> getAnnotationNames();

    /**
     * Interface every enhancer proxy has to implement, see {@link org.openjpa.ide.idea.integration.EnhancerProxy}.
     *
     * @param api                 Persistence API used to enhance classes with
     * @param compileCtx          IntelliJ IDEA compile context
     * @param module              Module to enhance in
     * @param persistenceUnitName Optional persistence unit name (if null is provided, all persistence capable classes have to be enhanced)
     * @return Proxy to the selected enhancer
     * @throws NoSuchMethodException  .
     * @throws java.lang.reflect.InvocationTargetException
     *                                .
     * @throws IllegalAccessException .
     * @throws InstantiationException .
     */
    @NotNull
    EnhancerProxy newEnhancerProxy(PersistenceApi api,
                                   CompileContext compileCtx,
                                   Module module,
                                   @Nullable String persistenceUnitName)
            throws NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            InstantiationException;

}
