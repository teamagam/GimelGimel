package com.teamagam.gimelgimel.domain.base.logging;

public class DomainLoggerFactoryHolder {
    private static DomainLoggerFactory sDomainLoggerFactory;

    public static void initialize(DomainLoggerFactory domainLoggerFactory) {
        if (isInitialized()) {
            throw new RuntimeException(DomainLoggerFactoryHolder.class.getSimpleName()
                    + " cannot be initialized more than once");
        }
        sDomainLoggerFactory = domainLoggerFactory;
    }

    public static DomainLoggerFactory get() {
        if (!isInitialized()) {
            throw new RuntimeException(DomainLoggerFactoryHolder.class.getSimpleName()
                    + " has not been initialized");
        }
        return sDomainLoggerFactory;
    }

    private static boolean isInitialized() {
        return sDomainLoggerFactory != null;
    }
}
