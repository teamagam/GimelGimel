package com.teamagam.gimelgimel.app.common.logging;

import android.content.Context;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple factory class to enable future different concrete implementations
 * to different tags and an easy way to change the LogWrapper implementation throughout the app.
 */
public class LogWrapperFactory {

    private static final NativeLogWrapper sInnerLogger =
            new NativeLogWrapper(LogWrapperFactory.class.getSimpleName());

    private static FileWriter sLogWriter;

    public static void init(Context context) {
        try {
            sLogWriter = WriteToDiskLogWrapper.createLogfileWriter(context);
        } catch (WriteToDiskLogWrapper.LogfileCreationException ex) {
            sInnerLogger.e("Initializing disk-logger failed", ex);
            sLogWriter = null;
        }
    }

    public static LogWrapper create(String tag) {
        List<LogWrapper> logWrappers = new ArrayList<>(2);

        NativeLogWrapper nativeLogWrapper = new NativeLogWrapper(tag);
        logWrappers.add(nativeLogWrapper);

        if (sLogWriter != null) {
            LogWrapper diskLogger = createDiskLogger(tag, sLogWriter);
            logWrappers.add(diskLogger);
        }

        return new MultipleLogWrapper(logWrappers);
    }

    public static LogWrapper create(Class loggingClass) {
        return create(loggingClass.getSimpleName());
    }

    private static LogWrapper createDiskLogger(String tag, FileWriter logWriter) {
        WriteToDiskLogWrapper writeToDiskLogWrapper = new WriteToDiskLogWrapper(tag, logWriter);
        VerbosityConfiguration configuration = VerbosityConfiguration.createLogsAllBut(
                VerbosityConfiguration.VerbosityLevel.INFO,
                VerbosityConfiguration.VerbosityLevel.VERBOSE);

        return new VerbosityFilterLogWrapperDecorator(writeToDiskLogWrapper, configuration);
    }
}
