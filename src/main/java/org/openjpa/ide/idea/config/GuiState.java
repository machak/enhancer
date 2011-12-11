package org.openjpa.ide.idea.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openjpa.ide.idea.EnhancerSupportRegistry;
import org.openjpa.ide.idea.PersistenceApi;
import org.openjpa.ide.idea.integration.EnhancerSupport;

/**
 * Holds temporary config data for the gui
 */
public class GuiState {

    private boolean indexReady = false;

    private boolean enhancerEnabled = true;

    private String metaDataExtensions = "jdo";

    /**
     * Indicator if {@link #metaDataExtensions} should be added to compiler resource patterns
     */
    private boolean addToCompilerResourcePatterns = true;

    private boolean includeTestClasses = true;

    private boolean addDefaultConstructor = true;

    private boolean enforcePropertyRestrictions = true;

    private boolean enhancerInitialized = false;

    private PersistenceApi api;

    private EnhancerSupportRegistry enhancerSupportRegistry;

    private EnhancerSupport enhancerSupport;

    private List<AffectedModule> affectedModules;

    private List<MetaDataOrClassFile> metadataFiles;

    private List<MetaDataOrClassFile> annotatedClassFiles;

    //
    // Constructor
    //


    public GuiState(final EnhancerSupportRegistry enhancerSupportRegistry) {
        this(false,
                false,
                "jpa",
                true,
                true,
                false,
                true,
                true,
                PersistenceApi.HIBERNATE,
                enhancerSupportRegistry,
                enhancerSupportRegistry.getDefaultEnhancerSupport(),
                new ArrayList<AffectedModule>(0),
                new ArrayList<MetaDataOrClassFile>(0),
                new ArrayList<MetaDataOrClassFile>(0));
    }

    public GuiState(final boolean indexReady,
                    final boolean enhancerEnabled,
                    final String metaDataExtensions,
                    final boolean addToCompilerResourcePatterns,
                    final boolean includeTestClasses,
                    final boolean addDefaultConstructor,
                    final boolean enforcePropertyRestrictions,
                    final boolean enhancerInitialized,
                    final PersistenceApi api,
                    final EnhancerSupportRegistry enhancerSupportRegistry,
                    final EnhancerSupport enhancerSupport,
                    final List<AffectedModule> affectedModules,
                    final List<MetaDataOrClassFile> metadataFiles,
                    final List<MetaDataOrClassFile> annotatedClassFiles) {

        this.indexReady = indexReady;
        this.enhancerEnabled = enhancerEnabled;
        this.metaDataExtensions = metaDataExtensions;
        this.addToCompilerResourcePatterns = addToCompilerResourcePatterns;
        this.includeTestClasses = includeTestClasses;
        this.addDefaultConstructor = addDefaultConstructor;
        this.enforcePropertyRestrictions = enforcePropertyRestrictions;
        this.enhancerInitialized = enhancerInitialized;
        this.api = api;
        this.enhancerSupportRegistry = enhancerSupportRegistry;
        this.enhancerSupport = enhancerSupport;
        this.affectedModules = new ArrayList<AffectedModule>(affectedModules);
        this.annotatedClassFiles = new ArrayList<MetaDataOrClassFile>(annotatedClassFiles);
        this.metadataFiles = new ArrayList<MetaDataOrClassFile>(metadataFiles);
    }

    public GuiState(final GuiState data) {
        this(data.isIndexReady(),
                data.isEnhancerEnabled(),
                data.getMetaDataExtensions(),
                data.isAddToCompilerResourcePatterns(),
                data.isIncludeTestClasses(),
                data.isAddDefaultConstructor(),
                data.isEnforcePropertyRestrictions(),
                data.isEnhancerInitialized(),
                data.getApi(),
                data.getEnhancerSupportRegistry(),
                data.getEnhancerSupport(),
                deepCopyAffectedModules(data.getAffectedModules()),
                deepCopyMetaFilesModules(data.getMetadataFiles()),
                deepCopyMetaFilesModules(data.getAnnotatedClassFiles()));
    }

    private static List<MetaDataOrClassFile> deepCopyMetaFilesModules(List<MetaDataOrClassFile> metadataFiles) {

        final List<MetaDataOrClassFile> myCopy = new ArrayList<MetaDataOrClassFile>(metadataFiles.size());
        for (final MetaDataOrClassFile file : metadataFiles) {
            myCopy.add(new MetaDataOrClassFile(file.getModuleName(), file.getFileName(), file.getPath(), file.getClassName(), file.isEnabled()));
        }
        return myCopy;
    }

    //
    // Methods
    //


    public void setMetadataFiles(List<MetaDataOrClassFile> metadataFiles) {
        this.metadataFiles = metadataFiles;
    }

    public void setAnnotatedClassFiles(List<MetaDataOrClassFile> annotatedClassFiles) {
        this.annotatedClassFiles = annotatedClassFiles;
    }

    public boolean isIndexReady() {
        return this.indexReady;
    }

    public boolean isEnhancerEnabled() {
        return this.enhancerEnabled;
    }

    public void setEnhancerEnabled(final boolean enhancerEnabled) {
        this.enhancerEnabled = enhancerEnabled;
    }

    public String getMetaDataExtensions() {
        return this.metaDataExtensions;
    }

    public void setMetaDataExtensions(final String metaDataExtensions) {
        this.metaDataExtensions = metaDataExtensions;
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

    public boolean isEnhancerInitialized() {
        return this.enhancerInitialized;
    }

    public PersistenceApi getApi() {
        return this.api;
    }

    public void setApi(final PersistenceApi api) {
        this.api = api;
    }

    public EnhancerSupportRegistry getEnhancerSupportRegistry() {
        return this.enhancerSupportRegistry;
    }

    public void setEnhancerSupportRegistry(final EnhancerSupportRegistry enhancerSupportRegistry) {
        this.enhancerSupportRegistry = enhancerSupportRegistry;
    }

    public EnhancerSupport getEnhancerSupport() {
        return this.enhancerSupport;
    }

    public void setEnhancerSupport(final EnhancerSupport enhancerSupport) {
        this.enhancerSupport = enhancerSupport;
    }

    public List<AffectedModule> getAffectedModules() {
        return new ArrayList<AffectedModule>(this.affectedModules);
    }

    public void setAffectedModules(final List<AffectedModule> affectedModules) {
        this.affectedModules = new ArrayList<AffectedModule>(affectedModules);
    }


    public List<MetaDataOrClassFile> getMetadataFiles() {
        return new ArrayList<MetaDataOrClassFile>(this.metadataFiles);
    }

    public List<MetaDataOrClassFile> getAnnotatedClassFiles() {
        return new ArrayList<MetaDataOrClassFile>(this.annotatedClassFiles);
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


    //
    // java.lang.Object overrides
    //

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final GuiState guiState = (GuiState) o;

        if (this.enhancerEnabled != guiState.enhancerEnabled) {
            return false;
        }
        if (this.api != null ? this.api != guiState.api : guiState.api != null) {
            if (this.affectedModules != null ? !this.affectedModules.equals(guiState.affectedModules) : guiState.affectedModules != null) {
                return false;
            }
        }
        if (this.metaDataExtensions != null ? !this.metaDataExtensions.equals(guiState.metaDataExtensions)
                : guiState.metaDataExtensions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (this.enhancerEnabled ? 1 : 0);
        result = 31 * result + (this.api != null ? this.api.hashCode() : 0);
        result = 31 * result + (this.metaDataExtensions != null ? this.metaDataExtensions.hashCode() : 0);
        result = 31 * result + (this.affectedModules != null ? this.affectedModules.hashCode() : 0);
        return result;
    }

    @SuppressWarnings("MagicCharacter")
    @Override
    public String toString() {
        return "GuiState{" +
                "affectedModules=" +
                this.affectedModules +
                ", enhancerInitialized=" +
                this.enhancerInitialized +
                ", api=" +
                this.api +
                ", metaDataExtensions='" +
                this.metaDataExtensions +
                '\'' +
                ", enhancerEnabled=" +
                this.enhancerEnabled +
                '}';
    }

    //
    // Helper methods
    //

    private static List<AffectedModule> deepCopyAffectedModules(final Collection<AffectedModule> affectedModules) {
        final List<AffectedModule> copyAffectedModules = new ArrayList<AffectedModule>(affectedModules.size());
        for (final AffectedModule affectedModule : affectedModules) {
            copyAffectedModules.add(new AffectedModule(affectedModule.isEnabled(), affectedModule.getName()));
        }
        return copyAffectedModules;
    }

}

