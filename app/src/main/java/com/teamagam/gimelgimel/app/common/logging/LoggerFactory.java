package com.teamagam.gimelgimel.app.common.logging;

import android.content.Context;

import com.teamagam.gimelgimel.app.utils.Constants;

import java.io.File;
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
    private static String sExternalStorageDirectoryPath;

    public static void init(Context context) {
        try {
            sLogWriter = DiskLogger.createLogfileWriter(context);
            sExternalStorageDirectoryPath = getExternalStorageDirectoryPath(context);
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

    public static Logger create() {
        String simpleClassName = getClassSimpleName();
        return create(simpleClassName);
    }

    private static String getClassSimpleName() {
        StackTraceElement stackTrace[] = Thread.currentThread().getStackTrace();

        //take stackTrace element at index 4 because:
        //0: VMStack.getThreadStackTrace(Native Method)
        //1: java.lang.Thread.getStackTrace
        //2: LogFactory.getClassSimpleName method (this method)
        //3: LogFactory.create
        //4: this is the calling method!
        String callingClassFullName = stackTrace[4].getClassName();
        return callingClassFullName.substring(
                callingClassFullName.lastIndexOf(".") + 1);
    }

    private static List<Logger> createLoggers(String tag) {
        List<Logger> loggers = new ArrayList<>(2);

        NativeLogger nativeLogWrapper = new NativeLogger(tag);
        loggers.add(nativeLogWrapper);

        if (sLogWriter != null) {
            Logger diskLogger = createDiskLogger(tag, sLogWriter);
            loggers.add(diskLogger);
        }

        if (sExternalStorageDirectoryPath != null && !sExternalStorageDirectoryPath.isEmpty()) {
            loggers.add(createLog4jLogger(tag));
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

    private static Logger createLog4jLogger(String tag) {
        Log4jDiskLogger log4jLogger = new Log4jDiskLogger(
                sExternalStorageDirectoryPath,
                Constants.LOG_FILE_NAME_SUFFIX,
                Constants.MAX_LOG_SIZE,
                Constants.MAX_BACKUP_LOG_FILES,
                tag);

        VerbosityConfiguration configuration = VerbosityConfiguration.createLogsAllBut();

        return new VerbosityFilterLoggerDecorator(log4jLogger, configuration);
    }

    private static String getExternalStorageDirectoryPath(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);

        return externalFilesDir + File.separator + Constants.LOG_DIR_NAME;
    }
}
