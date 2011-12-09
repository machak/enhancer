package org.openjpa.ide.idea;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.openjpa.ide.idea.integration.EnhancerSupport;
import org.openjpa.ide.idea.integration.openjpa.EnhancerSupportOpenJpa;

/**
 */
class EnhancerSupportRegistryDefault implements EnhancerSupportRegistry {

    private static final EnhancerSupportRegistry instance = new EnhancerSupportRegistryDefault();

    public static final EnhancerSupport DEFAULT_ENHANCER_SUPPORT = new EnhancerSupportOpenJpa();

    private final Map<String, EnhancerSupport> supported = new HashMap<String, EnhancerSupport>();

    public static EnhancerSupportRegistry getInstance() {
        return instance;
    }

    @Override
    @SuppressWarnings("MagicCharacter")
    @NotNull
    public EnhancerSupport getEnhancerSupportById(@NotNull final String id) {
        final EnhancerSupport enhancerSupport = this.supported.get(id);
        Validate.notNull(enhancerSupport, "no enhancer support for id '" + id + '\'');
        return enhancerSupport;
    }

    @Override
    public boolean isRegistered(@NotNull final String id) {
        return this.supported.get(id) != null;
    }

    @Override
    @NotNull
    public EnhancerSupport getDefaultEnhancerSupport() {
        return DEFAULT_ENHANCER_SUPPORT;
    }

    @Override
    @NotNull
    public Set<EnhancerSupport> getSupportedEnhancers() {
        return new LinkedHashSet<EnhancerSupport>(this.supported.values());
    }

    @Override
    public void registerEnhancerSupport(@NotNull final EnhancerSupport enhancerSupport) {
        final String id = enhancerSupport.getId();
        this.supported.put(id, enhancerSupport);
    }

    @Override
    public void unRegisterEnhanderSupport(@NotNull final EnhancerSupport enhancerSupport) {
        final String id = enhancerSupport.getId();
        this.supported.remove(id);
    }

}
