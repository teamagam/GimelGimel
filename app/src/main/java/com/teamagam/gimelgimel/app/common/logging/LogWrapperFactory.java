package com.teamagam.gimelgimel.app.common.logging;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple factory class to enable future different concrete implementations
 * to different tags and an easy way to change the LogWrapper implementation throughout the app.
 */
public class LogWrapperFactory {

    private static final String LOG_DIR_NAME = "Logs";
    private static final String LOG_FILE_NAME_SUFFIX = "log.txt";

    private static FileWriter sLogWriter;

    public static void init(Context context){
        initializeLogfile(context);
    }

    public static LogWrapper create(String tag) {
        NativeLogWrapper nativeLogWrapper = new NativeLogWrapper(tag);
        WriteToDiskLogWrapper writeToDiskLogWrapper = new WriteToDiskLogWrapper(tag, sLogWriter);

        List<LogWrapper> logWrappers = new ArrayList<>(2);
        logWrappers.add(nativeLogWrapper);
        logWrappers.add(writeToDiskLogWrapper);

        return new MultipleLogWrapper(logWrappers);
    }

    public static LogWrapper create(Class loggingClass) {
        return create(loggingClass.getSimpleName());
    }

    public static void initializeLogfile(Context context) {
        if (isExternalStorageWritable()) {
            File logsDir = getLogStorageDir(context);
            sLogWriter = createLogfileWriter(logsDir);
        } else {
            throw new LogfileCreationException();
        }
    }

    private static FileWriter createLogfileWriter(File logsDir) {
        String logFilename = System.currentTimeMillis() + "_" + LOG_FILE_NAME_SUFFIX;
        String logAbsFilepath = logsDir.getAbsolutePath() + "/" + logFilename;
        try {
            return new FileWriter(logAbsFilepath);
        } catch (IOException e) {
            throw new LogfileCreationException();
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static File getLogStorageDir(Context context) {
        // Get the directory for the app's private pictures directory.
        File logsDir = new File(context.getExternalFilesDir(null), LOG_DIR_NAME);
        if (logsDir.mkdirs() || logsDir.isDirectory()) {
            //
            return logsDir;
        }
        throw new LogfileCreationException();
    }



    private static class LogfileCreationException extends RuntimeException {
    }
}
