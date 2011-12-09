package org.openjpa.ide.idea;

/**
 * Persistence api related annotation class names.
 */
class PersistenceApiConstants {

    //
    // Constants
    //

    static final String ANNOTATION_JPA_ENTITY = "javax.persistence.Entity";

    static final String ANNOTATION_JPA_MAPPED_SUPERCLASS = "javax.persistence.MappedSuperclass";

    static final String ANNOTATION_JPA_EMBEDDABLE = "javax.persistence.Embeddable";
//
    // Hidden constructor
    //

    private PersistenceApiConstants() {
        // prohibit instantiation
    }

}
