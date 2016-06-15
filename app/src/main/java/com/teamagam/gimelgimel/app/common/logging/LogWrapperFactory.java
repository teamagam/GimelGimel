package com.teamagam.gimelgimel.app.common.logging;

/**
 * A simple factory class to enable future different concrete implementations
 * to different tags and an easy way to change the LogWrapper implementation throughout the app.
 */
public class LogWrapperFactory {
    public static LogWrapper create(String tag) {
        return new NativeLogWrapper(tag);
    }
}
