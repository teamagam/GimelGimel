package com.teamagam.gimelgimel.app.utils;

/**
 * Utility class for managing unique IDs for entities and layers
 * throughout the application lifetime.
 * An ID is unique within the app,
 * There is no guarantee it will be unique cross-applications
 */
public class IdCreatorUtil {

    private static final String sIdPrefix;
    private static int sEntitiesCounter;

    static {
        sEntitiesCounter = 0;
        sIdPrefix = "ggid";
    }

    /**
     * Generates unique identifier within the app, given that identifiers are
     * generated solely by this method.
     * <p/>
     * <b>Identifier is not unique across different applications</b>
     *
     * @return a unique identifier
     */
    public static synchronized String getId() {
        return String.format("%s_%d", sIdPrefix, sEntitiesCounter++);
    }
}
