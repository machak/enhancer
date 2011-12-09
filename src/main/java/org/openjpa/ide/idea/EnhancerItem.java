package org.openjpa.ide.idea;

import com.intellij.openapi.compiler.FileProcessingCompiler;
import com.intellij.openapi.compiler.TimestampValidityState;
import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.vfs.VirtualFile;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * File that is target or metadata source for the enhancement process.<br/>
 * This can be either a class file or a xml file containing persistence metadata.<br/>
 * <br/>
 * Acts as wrapper to handle validity state for incremental compilation/enhancement.
 * <p/>
 * TODO: seems hacky, do a complete review and cleanup
 */
class EnhancerItem implements FileProcessingCompiler.ProcessingItem {

    private final VirtualMetadataFile virtualMetadata;

    private final VirtualFile classFile;

    EnhancerItem(final VirtualMetadataFile virtualMetadata, final VirtualFile classFile) {
        Validate.notNull(classFile, "classFile is null!");
        this.virtualMetadata = virtualMetadata;
        this.classFile = classFile;
    }

    @NotNull
    @Override
    public VirtualFile getFile() {
        return this.classFile;
    }

    @Override
    public ValidityState getValidityState() {
        return new TimestampValidityState(this.classFile.getTimeStamp());
    }

    public VirtualMetadataFile getVirtualMetadata() {
        return this.virtualMetadata;
    }

}
