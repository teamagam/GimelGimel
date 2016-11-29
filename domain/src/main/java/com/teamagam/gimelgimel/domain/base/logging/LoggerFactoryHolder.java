package com.teamagam.gimelgimel.domain.base.logging;

public class LoggerFactoryHolder {
    private static LoggerFactory sLoggerFactory;

    public static void initialize(LoggerFactory loggerFactory) {
        if (isInitialized()) {
            throw new RuntimeException(LoggerFactoryHolder.class.getSimpleName()
                    + " cannot be initialized more than once");
        }
        sLoggerFactory = loggerFactory;
    }

    public static LoggerFactory get() {
        if (!isInitialized()) {
            throw new RuntimeException(LoggerFactoryHolder.class.getSimpleName()
                    + " has not been initialized");
        }
        return sLoggerFactory;
    }

    private static boolean isInitialized() {
        return sLoggerFactory != null;
    }
}
