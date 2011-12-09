package org.openjpa.ide.idea.config;

import org.apache.commons.lang.Validate;

/**
 */
public class MetaDataOrClassFile {

    private boolean enabled;

    private final String moduleName;

    private final String fileName;

    private final String path;

    private final String className;

    public MetaDataOrClassFile(final String moduleName, final String fileName, final String path, final String className, final boolean enabled) {
        Validate.notNull(moduleName, "moduleName is null!");
        Validate.notNull(fileName, "fileName is null!");
        Validate.notNull(className, "className is null!");

        this.enabled = enabled;
        this.moduleName = moduleName;
        this.fileName = fileName;
        this.path = path;
        this.className = className;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getPath() {
        return this.path;
    }

    public String getClassName() {
        return this.className;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
