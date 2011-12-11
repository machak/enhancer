package org.openjpa.ide.idea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.intellij.util.xmlb.annotations.AbstractCollection;

import org.openjpa.ide.idea.integration.EnhancerSupport;

/**
 * Holds plugin's persistent state.
 */
public class PersistentState { // has to be public (for IDEA configuration access)

    private boolean enhancerEnabled = true;

    private Collection<String> metaDataExtensions = new ArrayList<String>(Arrays.asList("jdo", "orm"));

    /**
     * Indicator if {@link #metaDataExtensions} should be added to compiler resource patterns
     */
    private boolean addToCompilerResourcePatterns = true;

    private boolean includeTestClasses = true;
    private boolean addDefaultConstructor = true;
    private boolean enforcePropertyRestrictions = true;
    private boolean tmpClassLoader = true;

    private Collection<String> enabledModules = new ArrayList<String>();

    private Collection<String> enabledFiles = new ArrayList<String>();

    private String api = "JPA";

    private String enhancerSupport = "OPENJPA";

    public boolean isEnhancerEnabled() {
        return this.enhancerEnabled;
    }

    public void setEnhancerEnabled(final boolean enhancerEnabled) {
        this.enhancerEnabled = enhancerEnabled;
    }

    @AbstractCollection(elementTypes = String.class)
    public Collection<String> getMetaDataExtensions() {
        return new LinkedHashSet<String>(this.metaDataExtensions);
    }

    @AbstractCollection(elementTypes = String.class)
    public void setMetaDataExtensions(final Collection<String> metaDataExtensions) {
        this.metaDataExtensions = new LinkedHashSet<String>(metaDataExtensions);
    }

    public boolean isAddToCompilerResourcePatterns() {
        return this.addToCompilerResourcePatterns;
    }

    public void setAddToCompilerResourcePatterns(final boolean addToCompilerResourcePatterns) {
        this.addToCompilerResourcePatterns = addToCompilerResourcePatterns;
    }

    public boolean isIncludeTestClasses() {
        return this.includeTestClasses;
    }

    public void setIncludeTestClasses(final boolean includeTestClasses) {
        this.includeTestClasses = includeTestClasses;
    }

    @AbstractCollection(elementTypes = String.class)
    public Collection<String> getEnabledModules() {
        return new LinkedHashSet<String>(this.enabledModules);
    }

    @AbstractCollection(elementTypes = String.class)
    public void setEnabledModules(final Collection<String> enabledModules) {
        this.enabledModules = new LinkedHashSet<String>(enabledModules);
    }

    @AbstractCollection(elementTypes = String.class)
    public Collection<String> getEnabledFiles() {
        return new LinkedHashSet<String>(this.enabledFiles);
    }

    @AbstractCollection(elementTypes = String.class)
    public void setEnabledFiles(final Collection<String> enabledFiles) {
        this.enabledFiles = new LinkedHashSet<String>(enabledFiles);
    }

    public String getApi() {
        return this.api;
    }

    public void setApi(final String api) {
        this.api = api;
    }

    public String getEnhancerSupport() {
        return this.enhancerSupport;
    }

    public void setEnhancerSupport(final String enhancerSupport) {
        this.enhancerSupport = enhancerSupport;
    }


    public boolean isAddDefaultConstructor() {
        return addDefaultConstructor;
    }

    public void setAddDefaultConstructor(boolean addDefaultConstructor) {
        this.addDefaultConstructor = addDefaultConstructor;
    }

    public boolean isEnforcePropertyRestrictions() {
        return enforcePropertyRestrictions;
    }

    public void setEnforcePropertyRestrictions(boolean enforcePropertyRestrictions) {
        this.enforcePropertyRestrictions = enforcePropertyRestrictions;
    }

    public boolean isTmpClassLoader() {
        return tmpClassLoader;
    }

    public void setTmpClassLoader(boolean tmpClassLoader) {
        this.tmpClassLoader = tmpClassLoader;
    }

    /**
     * Copy method used to update persistent state with plugin's internal state.
     *
     * @param state plugin's internal state
     * @return persistent copy of plugin's internal state
     */
    @SuppressWarnings({"FeatureEnvy", "ChainedMethodCall"})
    public PersistentState copyFrom(final State state) {
        this.enhancerEnabled = state.isEnhancerEnabled();

        if (this.metaDataExtensions == null) {
            this.metaDataExtensions = new ArrayList<String>();
        } else {
            this.metaDataExtensions.clear();
        }
        this.metaDataExtensions.addAll(state.getMetaDataExtensions());

        this.includeTestClasses = state.isIncludeTestClasses();
        this.addDefaultConstructor = state.isAddDefaultConstructor();
        this.enforcePropertyRestrictions = state.isEnforcePropertyRestrictions();
        this.tmpClassLoader = state.isTmpClassLoader();

        if (this.enabledModules == null) {
            this.enabledModules = new ArrayList<String>();
        } else {
            this.enabledModules.clear();
        }
        this.enabledModules.addAll(state.getEnabledModules());
        if (this.enabledFiles == null) {
            this.enabledFiles = new ArrayList<String>();
        } else {
            this.enabledFiles.clear();
        }
        this.enabledFiles.addAll(state.getEnabledFiles());

        final EnhancerSupport configuredEnhancerSupport = state.getEnhancerSupport();
        final EnhancerSupport usedEnhancerSupport;
        if (configuredEnhancerSupport == null) {
            usedEnhancerSupport = state.getEnhancerSupportRegistry().getDefaultEnhancerSupport();
            this.enhancerSupport = usedEnhancerSupport.getId();
        } else {
            usedEnhancerSupport = configuredEnhancerSupport;
            this.enhancerSupport = configuredEnhancerSupport.getId();
        }

        final PersistenceApi myApi = state.getApi();
        if (myApi == null || !usedEnhancerSupport.isSupported(myApi)) {
            this.api = usedEnhancerSupport.getDefaultPersistenceApi().name();
        } else {
            this.api = myApi.name();
        }

        return this;
    }

}
