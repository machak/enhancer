package org.openjpa.ide.idea;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.openjpa.ide.idea.integration.EnhancerSupport;

/**
 * Registry of supported enhancers
 */
public interface EnhancerSupportRegistry {

    @NotNull
    public EnhancerSupport getEnhancerSupportById(@NotNull String id);

    public boolean isRegistered(@NotNull String id);

    @NotNull
    public EnhancerSupport getDefaultEnhancerSupport();

    @NotNull
    public Set<EnhancerSupport> getSupportedEnhancers();

    public void registerEnhancerSupport(@NotNull EnhancerSupport enhancerSupport);

    public void unRegisterEnhanderSupport(@NotNull EnhancerSupport enhancerSupport);

}
