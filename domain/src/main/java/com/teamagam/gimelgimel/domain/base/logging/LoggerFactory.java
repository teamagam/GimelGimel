package com.teamagam.gimelgimel.domain.base.logging;

public class LoggerFactory {

    private static Factory sFactory;

    public static void initialize(Factory loggerFactory) {
        if (isInitialized()) {
            throw new RuntimeException(LoggerFactory.class.getSimpleName()
                    + " cannot be initialized more than once");
        }
        sFactory = loggerFactory;
    }

    public static Logger create(String tag) {
        if (!isInitialized()) {
            throw new RuntimeException(LoggerFactory.class.getSimpleName()
                    + " has not been initialized");
        }
        return sFactory.create(tag);
    }

    private static boolean isInitialized() {
        return sFactory != null;
    }

    public interface Factory {
        Logger create(String tag);
    }
}
