package org.openjpa.ide.idea;

import java.util.Arrays;

/**
 * Enum defining supported persistence api's, including information (fq class names) about
 * corresponding annotations.
 */
public enum PersistenceApi {

    HIBERNATE(),

    JPA(PersistenceApiConstants.ANNOTATION_JPA_ENTITY,
            PersistenceApiConstants.ANNOTATION_JPA_MAPPED_SUPERCLASS,
            PersistenceApiConstants.ANNOTATION_JPA_EMBEDDABLE);

    private final String[] annotationClassNames;

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
        // no outside reference
    PersistenceApi(final String... annotationClassNames) {
        this.annotationClassNames = annotationClassNames;
    }

    public String[] getAnnotationClassNames() {
        return Arrays.copyOf(this.annotationClassNames, this.annotationClassNames.length);
    }

}
