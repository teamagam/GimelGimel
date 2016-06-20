package com.teamagam.gimelgimel.app.common.logging;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Data class that holds logging-verbosity configuration
 */
class VerbosityConfiguration {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VerbosityLevel.DEBUG, VerbosityLevel.ERROR, VerbosityLevel.INFO,
            VerbosityLevel.VERBOSE, VerbosityLevel.WARNING, VerbosityLevel.USER_INTERACTION,
            VerbosityLevel.LIFECYCLE})
    public @interface VerbosityLevel {
        int DEBUG = 1;
        int ERROR = 2;
        int INFO = 3;
        int VERBOSE = 4;
        int WARNING = 5;
        int USER_INTERACTION = 6;
        int LIFECYCLE = 7;
    }

    public static VerbosityConfiguration createLogsAllBut(@VerbosityLevel int... levels) {
        boolean debug = true;
        boolean error = true;
        boolean info = true;
        boolean verbose = true;
        boolean warning = true;
        boolean userInteraction = true;
        boolean lifecycle = true;

        for (@VerbosityLevel int level : levels) {
            switch (level) {
                case VerbosityLevel.DEBUG:
                    debug = false;
                    break;
                case VerbosityLevel.ERROR:
                    error = false;
                    break;
                case VerbosityLevel.INFO:
                    info = false;
                    break;
                case VerbosityLevel.LIFECYCLE:
                    lifecycle = false;
                    break;
                case VerbosityLevel.USER_INTERACTION:
                    userInteraction = false;
                    break;
                case VerbosityLevel.VERBOSE:
                    verbose = false;
                    break;
                case VerbosityLevel.WARNING:
                    warning = false;
                    break;
            }
        }

        return new VerbosityConfiguration(debug, error, info, verbose, warning, userInteraction,
                lifecycle);
    }


    private boolean mIsDebug;
    private boolean mIsError;
    private boolean mIsInfo;
    private boolean mIsVerbose;
    private boolean mIsWarning;
    private boolean mIsUserInteraction;
    private boolean mIsLifecycle;

    private VerbosityConfiguration(boolean debug, boolean error, boolean info, boolean verbose,
                                   boolean warning, boolean userInteraction, boolean lifecycle) {
        mIsDebug = debug;
        mIsError = error;
        mIsInfo = info;
        mIsVerbose = verbose;
        mIsWarning = warning;
        mIsUserInteraction = userInteraction;
        mIsLifecycle = lifecycle;
    }

    public boolean isLoggingDebug() {
        return mIsDebug;
    }

    public boolean isLoggingError() {
        return mIsError;
    }

    public boolean isLoggingInfo() {
        return mIsInfo;
    }

    public boolean isLoggingVerbose() {
        return mIsVerbose;
    }

    public boolean isLoggingWarning() {
        return mIsWarning;
    }

    public boolean isLoggingUserInteraction() {
        return mIsUserInteraction;
    }

    public boolean isLoggingLifecycle() {
        return mIsLifecycle;
    }
}
