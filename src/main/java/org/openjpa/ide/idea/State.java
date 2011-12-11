package org.openjpa.ide.idea;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.openjpa.ide.idea.integration.EnhancerSupport;

public class State {


    private boolean enhancerEnabled = true;

    private Set<String> metaDataExtensions = new LinkedHashSet<String>(Arrays.asList("jdo", "orm"));

    /**
     * Indicator if {@link #metaDataExtensions} should be added to compiler resource patterns
     */
    private boolean addToCompilerResourcePatterns = true;

    private boolean includeTestClasses = true;

    private boolean addDefaultConstructor = true;

    private boolean enforcePropertyRestrictions = true;

    private Set<String> enabledModules = new HashSet<String>();

    private Set<String> enabledFiles = new HashSet<String>();

    private PersistenceApi api = null;

    private final EnhancerSupportRegistry enhancerSupportRegistry = EnhancerSupportRegistryDefault.getInstance();

    private EnhancerSupport enhancerSupport = EnhancerSupportRegistryDefault.getInstance().getDefaultEnhancerSupport();

    public State() {
    }

    public State(final boolean enhancerEnabled,
                 final Set<String> metaDataExtensions,
                 final boolean addToCompilerResourcePatterns,
                 final boolean includeTestClasses,
                 final boolean addDefaultConstructor,
                 final boolean enforcePropertyRestrictions,
                 final Set<String> enabledModules,
                 final Set<String> enabledFiles,
                 final PersistenceApi api,
                 final EnhancerSupport enhancerSupport) {
        this.enhancerEnabled = enhancerEnabled;
        this.metaDataExtensions = new LinkedHashSet<String>(metaDataExtensions);
        this.addToCompilerResourcePatterns = addToCompilerResourcePatterns;
        this.includeTestClasses = includeTestClasses;
        this.addDefaultConstructor = addDefaultConstructor;
        this.enforcePropertyRestrictions = enforcePropertyRestrictions;
        this.enabledModules = new LinkedHashSet<String>(enabledModules);
        this.enabledFiles = new LinkedHashSet<String>(enabledFiles);
        this.api = api;
        this.enhancerSupport = enhancerSupport;
    }

    public boolean isEnhancerEnabled() {
        return this.enhancerEnabled;
    }

    public void setEnhancerEnabled(final boolean enhancerEnabled) {
        this.enhancerEnabled = enhancerEnabled;
    }

    public Set<String> getMetaDataExtensions() {
        return new LinkedHashSet<String>(this.metaDataExtensions);
    }

    public void setMetaDataExtensions(final Collection<String> metaDataExtensions) {
        this.metaDataExtensions.clear();
        if (metaDataExtensions != null && !metaDataExtensions.isEmpty()) {
            this.metaDataExtensions.addAll(metaDataExtensions);
        }
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

    public Set<String> getEnabledModules() {
        return new LinkedHashSet<String>(this.enabledModules);
    }


    public Set<String> getEnabledFiles() {
        return new LinkedHashSet<String>(this.enabledFiles);
    }

    public void setEnabledModules(final Collection<String> enabledModules) {
        this.enabledModules.clear();
        if (enabledModules != null && !enabledModules.isEmpty()) {
            this.enabledModules.addAll(enabledModules);
        } else {
            if (this.enabledModules != null) {
                this.enabledModules.clear();
            }
        }

    }

    public void setEnabledFiles(final Collection<String> files) {
        this.enabledFiles.clear();
        if (files != null && !files.isEmpty()) {
            this.enabledFiles.addAll(files);

        } else {
            if (this.enabledFiles != null) {
                this.enabledFiles.clear();
            }
        }
    }


    public EnhancerSupportRegistry getEnhancerSupportRegistry() {
        return this.enhancerSupportRegistry;
    }

    public EnhancerSupport getEnhancerSupport() {
        return this.enhancerSupport;
    }

    public void setEnhancerSupport(final EnhancerSupport enhancerSupport) {
        this.enhancerSupport = enhancerSupport;
    }

    public PersistenceApi getApi() {
        return this.api;
    }

    public void setApi(final PersistenceApi api) {
        this.api = api;
    }

    /**
     * Copy method for instances of this class.
     *
     * @param state the instance to retrieve the values from
     */
    public void copyFrom(final State state) {
        this.enhancerEnabled = state.enhancerEnabled;
        this.setMetaDataExtensions(state.metaDataExtensions);
        this.setAddToCompilerResourcePatterns(state.addToCompilerResourcePatterns);
        this.includeTestClasses = state.includeTestClasses;
        this.addDefaultConstructor = state.addDefaultConstructor;
        this.enforcePropertyRestrictions = state.enforcePropertyRestrictions;
        this.setEnabledModules(state.enabledModules);
        this.setEnabledFiles(state.enabledFiles);
        this.setApi(state.api);
        this.setEnhancerSupport(state.enhancerSupport);
    }

    /**
     * Update method from persistent state.
     *
     * @param state the instance to retrieve the values from
     */
    @SuppressWarnings({"FeatureEnvy", "ChainedMethodCall"})
    public void copyFrom(final PersistentState state) {
        this.enhancerEnabled = state.isEnhancerEnabled();

        final Collection<String> myMetaDataExtensions = state.getMetaDataExtensions();
        if (myMetaDataExtensions == null || myMetaDataExtensions.isEmpty()) {
            this.setMetaDataExtensions(new LinkedHashSet<String>());
        } else {
            this.setMetaDataExtensions(new LinkedHashSet<String>(myMetaDataExtensions));
        }

        this.addToCompilerResourcePatterns = state.isAddToCompilerResourcePatterns();
        this.includeTestClasses = state.isIncludeTestClasses();
        this.addDefaultConstructor = state.isAddDefaultConstructor();
        this.enforcePropertyRestrictions = state.isEnforcePropertyRestrictions();

        final Collection<String> enabledModules1 = state.getEnabledModules();
        if (enabledModules1 == null || enabledModules1.isEmpty()) {
            this.setEnabledModules(new HashSet<String>());
        } else {
            this.setEnabledModules(new HashSet<String>(enabledModules1));
        }

        final Collection<String> myEnabledFiles = state.getEnabledFiles();
        if (myEnabledFiles == null || myEnabledFiles.isEmpty()) {
            this.setEnabledFiles(new HashSet<String>());
        } else {
            this.setEnabledFiles(new HashSet<String>(myEnabledFiles));
        }

        final EnhancerSupportRegistry eSR = this.enhancerSupportRegistry;
        final String enhancerSupportString = state.getEnhancerSupport();
        final EnhancerSupport myEnhancerSupport;
        if (enhancerSupportString == null || enhancerSupportString.trim().isEmpty() || !eSR.isRegistered(enhancerSupportString)) {
            myEnhancerSupport = eSR.getDefaultEnhancerSupport();
        } else {
            myEnhancerSupport = eSR.getEnhancerSupportById(enhancerSupportString);
        }
        this.setEnhancerSupport(myEnhancerSupport);

        final String persistenceApiString = state.getApi();
        final PersistenceApi configuredApi =
                persistenceApiString == null ? PersistenceApi.JPA : PersistenceApi.valueOf(persistenceApiString.toUpperCase());

        final PersistenceApi validForEnhancerSupport = myEnhancerSupport.isSupported(configuredApi) ? configuredApi : myEnhancerSupport.getDefaultPersistenceApi();

        this.setApi(validForEnhancerSupport);
    }

}
