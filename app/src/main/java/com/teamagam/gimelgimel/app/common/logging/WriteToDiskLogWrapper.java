package com.teamagam.gimelgimel.app.common.logging;

import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * LogWrapper that write logs to disk
 */
public class WriteToDiskLogWrapper extends BaseLifecycleLogWrapper {



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

    public WriteToDiskLogWrapper(String tag, FileWriter logWriter) {
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
        DateFormat dateFormatter = DateFormat.getDateTimeInstance();
        String date = dateFormatter.format(new Date());
        String completeLogLine = String.format("%s %s %s %s%s", date, verbosity, mTag, message,
                EOL);
        try {
            mLogWriter.write(completeLogLine);
        } catch (IOException e) {
            throw new LogWriteException();
        }
    }

    private void logToFile(String verbosity, String message, Throwable tr) {
        logToFile(verbosity, message + EOL + Log.getStackTraceString(tr));
    }

    private void logLifecycleToFile(String lifecycleType, String message) {
        logToFile(VERBOSITY_LIFECYCLE, lifecycleType + EOL + message);
    }

    private static class LogfileCreationException extends RuntimeException {
    }

    private static class LogWriteException extends RuntimeException {
    }
}
