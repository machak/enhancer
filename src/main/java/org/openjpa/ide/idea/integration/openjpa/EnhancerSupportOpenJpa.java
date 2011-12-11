package org.openjpa.ide.idea.integration.openjpa;

import org.jetbrains.annotations.NotNull;
import org.openjpa.ide.idea.PersistenceApi;
import org.openjpa.ide.idea.integration.AbstractEnhancerSupport;

/**
 */
public class EnhancerSupportOpenJpa extends AbstractEnhancerSupport {

    private static final String ID = "OPENJPA";

    private static final String NAME = "OpenJpa";

    //
    // Interface implementation
    //

    /**
     * The name to display in the configuration dialog enhancer support drop-down.
     *
     * @return Enhancer support name
     */
    @Override
    @NotNull
    public String getId() {
        return ID;
    }

    /**
     * The name to display in the configuration dialog enhancer support drop-down.
     *
     * @return Enhancer support name
     */
    @Override
    @NotNull
    public String getName() {
        return NAME;
    }

    @Override
    @NotNull
    public String[] getEnhancerClassNames() {
        return new String[]{OpenJpaEnhancerProxy.OPEN_JPA_ENHANCER_CLASS};
    }

    @Override
    @NotNull
    public PersistenceApi[] getPersistenceApis() {
        return new PersistenceApi[]{PersistenceApi.JPA, PersistenceApi.HIBERNATE};
    }

    @Override
    @NotNull
    public Class<?> getEnhancerProxyClass() {
        return OpenJpaEnhancerProxy.class;
    }

}
