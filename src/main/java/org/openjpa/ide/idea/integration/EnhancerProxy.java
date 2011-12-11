package org.openjpa.ide.idea.integration;

import java.lang.reflect.InvocationTargetException;

/**
 * Classes implementing this interface have to ensure that an invoked enhancer is instantiated
 * inside it's own {@link java.lang.ClassLoader}<br/>
 * <br/>
 * DO NOT USE EXTERNAL LIBRARIES LIKE APACHE BEANUTILS OR IDEA's OWN REFLECTION/PROPERTY HELPERS because this can/will lead to ClassLoader
 * memory leaks due to PermGen Space exhaustion, as those implementations tend to cache class references even if they're not related
 * to their own {@link java.lang.ClassLoader} hierarchy.<br/>
 * <br/>
 * Use {@link ClassLoaderFactory#newClassLoader(com.intellij.openapi.compiler.CompileContext, com.intellij.openapi.module.Module)} ONLY! -
 * <br/>
 * to instantiate new ClassLoaders, as they're strictly project-module-related and do not include other dependencies, which ensures
 * project-module autonomic enhancement.<br/>
 * <br/>
 * EVERY implementing class also has to provide a constructor defined by
 * {@link org.openjpa.ide.idea.integration.AbstractEnhancerProxy#AbstractEnhancerProxy(org.openjpa.ide.idea.PersistenceApi, com.intellij.openapi.compiler.CompileContext, com.intellij.openapi.module.Module, String)}
 * and has to be added to {@link org.openjpa.ide.idea.integration.openjpa.EnhancerSupportOpenJpa} as enumeration entry to be selectable in the configuration gui.
 */
public interface EnhancerProxy {


    void setAddDefaultConstructor(boolean addDefaultConstructor);


    void setEnforcePropertyRestrictions(boolean enforcePropertyRestrictions);


    void setTmpClassLoader(boolean addDefaultConstructor);

    /**
     * Add names of classes annotated by persistence related annotations (e.g. javax.jdo.annotations.PersistenceCapable,
     * javax.persistence.Entity javax.jdo.annotations.PersistenceAware,...)
     *
     * @param classNames Fully qualified names of classes to be enhanced
     * @throws InvocationTargetException .
     * @throws IllegalAccessException    .
     * @throws NoSuchMethodException     .
     */
    void addClasses(String... classNames) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    /**
     * Add paths (full path) of files containing xml metadata for classes to
     * be enhanced (Also add related classes via {@link #addClasses(String...)}).
     *
     * @param metadataFiles Full path names of files containing xml metadata.
     * @throws InvocationTargetException .
     * @throws IllegalAccessException    .
     * @throws NoSuchMethodException     .
     */
    void addMetadataFiles(String... metadataFiles) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    /**
     * Start enhancement of added classes and metadata files.<br/>
     * <br/>
     * Be sure to have added class- and metadata file names via {@link #addClasses(String...)} and {@link #addMetadataFiles(String...)}
     *
     * @return Number of classes enhanced in this process.
     * @throws InvocationTargetException .
     * @throws IllegalAccessException    .
     * @throws NoSuchMethodException     .
     * @throws ClassNotFoundException    .
     */
    int enhance() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InstantiationException, NoSuchFieldException;

}
