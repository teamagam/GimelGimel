package com.teamagam.gimelgimel.app.common.logging;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import com.teamagam.gimelgimel.app.utils.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Logger that write logs to disk
 */
class DiskLogger extends BaseLifecycleLogger {

    private static Logger sInnerLogger = new NativeLogger(DiskLogger.class.getSimpleName());

    public static FileWriter createLogfileWriter(Context context) {
        if (isExternalStorageWritable()) {
            File logsDir = getLogStorageDir(context);
            return createLogfileWriter(logsDir);
        } else {
            throw new LogfileCreationException();
        }
    }

    private static FileWriter createLogfileWriter(File logsDir) {
        String logFilename = System.currentTimeMillis() + "_" + Constants.LOG_FILE_NAME_SUFFIX;
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
        File externalFilesDir = context.getExternalFilesDir(null);
        File logsDir = new File(externalFilesDir, Constants.LOG_DIR_NAME);
        if (logsDir.mkdirs() || logsDir.isDirectory()) {
            MediaScannerConnection.scanFile(context,
                    new String[]{externalFilesDir.getAbsolutePath(), logsDir.getAbsolutePath()},
                    null, null);
            return logsDir;
        }
        throw new LogfileCreationException();
    }


    private static final String VERBOSITY_DEBUG = "DEBUG";
    private static final String VERBOSITY_ERROR = "ERROR";
    private static final String VERBOSITY_INFO = "INFO";
    private static final String VERBOSITY_VERBOSE = "VERBOSE";
    private static final String VERBOSITY_WARNING = "WARNING";
    private static final String VERBOSITY_USER_INTERACTION = "USER_INTERACTION";
    private static final String VERBOSITY_LIFECYCLE = "LIFECYCLE";

    private static final String EOL = "\n";

    private FileWriter mLogWriter;

    private String mTag;

    public DiskLogger(String tag, FileWriter logWriter) {
        mTag = tag;
        mLogWriter = logWriter;
    }

    @Override
    public void d(String message) {
        logToFile(VERBOSITY_DEBUG, message);
    }

    @Override
    public void d(String message, Throwable tr) {
        logToFile(VERBOSITY_DEBUG, message, tr);
    }

    @Override
    public void e(String message) {
        logToFile(VERBOSITY_ERROR, message);
    }

    @Override
    public void e(String message, Throwable tr) {
        logToFile(VERBOSITY_ERROR, message, tr);
    }

    @Override
    public void i(String message) {
        logToFile(VERBOSITY_INFO, message);
    }

    @Override
    public void i(String message, Throwable tr) {
        logToFile(VERBOSITY_INFO, message, tr);
    }

    @Override
    public void v(String message) {
        logToFile(VERBOSITY_VERBOSE, message);
    }

    @Override
    public void v(String message, Throwable tr) {
        logToFile(VERBOSITY_VERBOSE, message, tr);
    }

    @Override
    public void w(String message) {
        logToFile(VERBOSITY_WARNING, message);
    }

    @Override
    public void w(String message, Throwable tr) {
        logToFile(VERBOSITY_WARNING, message, tr);
    }

    @Override
    public void userInteraction(String message) {
        logToFile(VERBOSITY_USER_INTERACTION, message);
    }

    @Override
    public void onCreate(String message) {
        logLifecycleToFile("onCreate", message);
    }

    @Override
    public void onStart(String message) {
        logLifecycleToFile("onStart", message);
    }

    @Override
    public void onRestart(String message) {
        logLifecycleToFile("onRestart", message);
    }

    @Override
    public void onResume(String message) {
        logLifecycleToFile("onResume", message);
    }

    @Override
    public void onPause(String message) {
        logLifecycleToFile("onPause", message);
    }

    @Override
    public void onStop(String message) {
        logLifecycleToFile("onStop", message);
    }

    @Override
    public void onDestroy(String message) {
        logLifecycleToFile("onDestroy", message);
    }

    @Override
    public void onAttach(String message) {
        logLifecycleToFile("onAttach", message);
    }

    @Override
    public void onCreateView(String message) {
        logLifecycleToFile("onCreateView", message);
    }

    @Override
    public void onActivityCreated(String message) {
        logLifecycleToFile("onActivityCreated", message);
    }

    @Override
    public void onDestroyView(String message) {
        logLifecycleToFile("onDestroyView", message);
    }

    @Override
    public void onDetach(String message) {
        logLifecycleToFile("onDetach", message);
    }

    private void logToFile(String verbosity, String message) {
        try {
            writeLineToLogFileWithRetries(createLogLine(verbosity, message),
                    Constants.MAX_WRITE_RETRIES);
        } catch (IOException ex) {
            sInnerLogger.e("Failed writing log statement to file", ex);
        }
    }

    private void writeLineToLogFileWithRetries(String line, int maxRetries) throws IOException {
        try {
            mLogWriter.write(line);
        } catch (IOException ex) {
            if (maxRetries == 0) {
                throw ex;
            }
            writeLineToLogFileWithRetries(line, maxRetries - 1);
        }
    }

    private String createLogLine(String verbosity, String message) {
        DateFormat dateFormatter = DateFormat.getDateTimeInstance();
        String date = dateFormatter.format(new Date());
        return String.format("%s %s %s %s%s", date, verbosity, mTag, message, EOL);
    }

    private void logToFile(String verbosity, String message, Throwable tr) {
        logToFile(verbosity, message + EOL + Log.getStackTraceString(tr));
    }

    private void logLifecycleToFile(String lifecycleType, String message) {
        logToFile(VERBOSITY_LIFECYCLE, lifecycleType + EOL + message);
    }

    public static class LogfileCreationException extends RuntimeException {
    }
}
