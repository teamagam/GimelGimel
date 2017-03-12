package com.teamagam.gimelgimel.app.common.logging;

import android.os.Handler;
import android.os.HandlerThread;

import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.data.common.ExternalDirProvider;

import org.apache.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * In charge of instantiating logger(s) to be used throughout the app
 * Should be the only way the rest of the app creates loggers
 */
public class AppLoggerFactory {

    private static final NativeLogger sInnerLogger =
            new NativeLogger(AppLoggerFactory.class.getSimpleName());

    private static String sExternalStorageLogDirectoryPath;
    private static Handler sLoggingHandler;

    public static void init(ExternalDirProvider externalDirProvider) {
        try {
            setupDiskLoggerConfigurations(externalDirProvider);
            sLoggingHandler = createHandler();
        } catch (Exception ex) {
            sInnerLogger.w("Disk logger setup failed", ex);
        }
    }

    private static Handler createHandler() {
        HandlerThread ht = new HandlerThread("Logging");
        ht.start();
        return new Handler(ht.getLooper());
    }

    public static AppLogger create(String tag) {
        Collection<AppLogger> loggers = createLoggers(tag);

        return new MultipleLogger(loggers);
    }

    public static AppLogger create(Class loggingClass) {
        return create(loggingClass.getSimpleName());
    }

    public static AppLogger create() {
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

    private static List<AppLogger> createLoggers(String tag) {
        List<AppLogger> loggers = new ArrayList<>(1);

        if (sExternalStorageLogDirectoryPath != null && !sExternalStorageLogDirectoryPath.isEmpty()) {
            try {
                loggers.add(createLog4jLogger(tag));
            } catch (Exception ex) {
                sInnerLogger.w("Could not create Log4j logger");
            }
        }

        if (loggers.isEmpty()) {
            NativeLogger nativeLogWrapper = new NativeLogger(tag);
            loggers.add(nativeLogWrapper);
        }

        return loggers;
    }

    private static void setupDiskLoggerConfigurations(ExternalDirProvider externalDirProvider) {
        sExternalStorageLogDirectoryPath = getExternalStorageLogDirectoryPath(externalDirProvider);
        createDirectory(sExternalStorageLogDirectoryPath);
        configureLogger(sExternalStorageLogDirectoryPath,
                Constants.LOG_FILE_NAME_SUFFIX,
                Constants.MAX_LOG_SIZE,
                Constants.MAX_BACKUP_LOG_FILES);
    }

    private static void createDirectory(String directoryPath) {
        File file = new File(directoryPath);

        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static void configureLogger(String directory, String fileName, long maxFileSize,
                                        int maxBackupLogFiles) {
        LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(directory + File.separator + fileName);
        logConfigurator.setRootLevel(Level.ALL);

        // Pattern - %DATE% %LEVEL% [%CLASS]-[%THREAD%] %MESSAGE%%NEWLINE%
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(maxFileSize);
        logConfigurator.setMaxBackupSize(maxBackupLogFiles);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }

    private static String getExternalStorageLogDirectoryPath(ExternalDirProvider externalDirProvider) {
        File externalFilesDir = externalDirProvider.getExternalFilesDir();

        return externalFilesDir + File.separator + Constants.LOG_DIR_NAME;
    }

    private static AppLogger createLog4jLogger(String tag) {
        org.apache.log4j.Logger logger = getLog4jLogger(tag);
        Log4jDiskLogger log4jLogger = new Log4jDiskLogger(logger);

        VerbosityConfiguration configuration = VerbosityConfiguration.createLogsAllBut();

        VerbosityFilterLoggerDecorator verbosityFilteredLogger = new VerbosityFilterLoggerDecorator(
                log4jLogger, configuration);

        return new HandlerThreadLoggerDecorator(verbosityFilteredLogger, sLoggingHandler);
    }

    private static org.apache.log4j.Logger getLog4jLogger(String tag) {
        return org.apache.log4j.Logger.getLogger(tag);
    }
}
