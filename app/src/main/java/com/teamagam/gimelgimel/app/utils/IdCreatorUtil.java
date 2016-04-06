package com.teamagam.gimelgimel.app.utils;

/**
 * Utility class for managing unique IDs for entities and layers
 * throughout the application lifetime.
 * An ID is unique within the app,
 * There is no guarantee it will be unique cross-applications
 */
public class IdCreatorUtil {

    private static int sEntitiesCounter;
    private static final String sIdPrefix;

    static {
        sEntitiesCounter = 0;
        sIdPrefix = "ggid";
    }

    /**
     * Randomly generates a UUID.
     *
     * @return a string representation of a randomly chosen 128-bit number
     */
    public static synchronized String getId() {
        return String.format("%s_%d", sIdPrefix, sEntitiesCounter++);
    }
}
