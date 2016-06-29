package com.teamagam.gimelgimel.app.common.logging;

import android.content.Context;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * In charge of instantiating logger(s) to be used throughout the app
 * Should be the only way the rest of the app creates loggers
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
        Collection<Logger> loggers = createLoggers(tag);

        return new MultipleLogger(loggers);
    }

    public static Logger create(Class loggingClass) {
        return create(loggingClass.getSimpleName());
    }

    private static List<Logger> createLoggers(String tag) {
        List<Logger> loggers = new ArrayList<>(2);

        NativeLogger nativeLogWrapper = new NativeLogger(tag);
        loggers.add(nativeLogWrapper);

        if (sLogWriter != null) {
            Logger diskLogger = createDiskLogger(tag, sLogWriter);
            loggers.add(diskLogger);
        }
        return loggers;
    }

    private static Logger createDiskLogger(String tag, FileWriter logWriter) {
        DiskLogger writeToDiskLogWrapper = new DiskLogger(tag, logWriter);
        VerbosityConfiguration configuration = VerbosityConfiguration.createLogsAllBut(
                VerbosityConfiguration.VerbosityLevel.INFO,
                VerbosityConfiguration.VerbosityLevel.VERBOSE);

        return new VerbosityFilterLoggerDecorator(writeToDiskLogWrapper, configuration);
    }
}
