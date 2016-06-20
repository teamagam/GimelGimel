package com.teamagam.gimelgimel.app.common.logging;

import android.content.Context;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple factory class to enable future different concrete implementations
 * to different tags and an easy way to change the Logger implementation throughout the app.
 */
public class LoggerFactory {

    private static final NativeLogger sInnerLogger =
            new NativeLogger(LoggerFactory.class.getSimpleName());

    private static FileWriter sLogWriter;

    public static void init(Context context) {
        try {
            sLogWriter = DiskLogger.createLogfileWriter(context);
        } catch (DiskLogger.LogfileCreationException ex) {
            sInnerLogger.e("Initializing disk-logger failed", ex);
            sLogWriter = null;
        }
    }

    public static Logger create(String tag) {
        List<Logger> loggers = new ArrayList<>(2);

        NativeLogger nativeLogWrapper = new NativeLogger(tag);
        loggers.add(nativeLogWrapper);

        if (sLogWriter != null) {
            Logger diskLogger = createDiskLogger(tag, sLogWriter);
            loggers.add(diskLogger);
        }

        return new MultipleLogger(loggers);
    }

    public static Logger create(Class loggingClass) {
        return create(loggingClass.getSimpleName());
    }

    private static Logger createDiskLogger(String tag, FileWriter logWriter) {
        DiskLogger writeToDiskLogWrapper = new DiskLogger(tag, logWriter);
        VerbosityConfiguration configuration = VerbosityConfiguration.createLogsAllBut(
                VerbosityConfiguration.VerbosityLevel.INFO,
                VerbosityConfiguration.VerbosityLevel.VERBOSE);

        return new VerbosityFilterLoggerDecorator(writeToDiskLogWrapper, configuration);
    }
}
