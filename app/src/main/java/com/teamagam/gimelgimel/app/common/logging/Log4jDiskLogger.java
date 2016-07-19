package com.teamagam.gimelgimel.app.common.logging;

import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by CV on 7/19/2016.
 */
class Log4jDiskLogger extends BaseLifecycleLogger {

    private static final String APPLICATION_NAME = "GimelGimel";

    private Logger mLogger;

    Log4jDiskLogger(long maxFileSize, String tag) {
        mLogger = createLogger(maxFileSize, tag);
    }

    private Logger createLogger(long maxFileSize, String tag) {
        configureLogger(getDirectory(), maxFileSize);

        return Logger.getLogger(tag);
    }

    @Override
    public void d(String message) {
        mLogger.debug(message);
    }

    @Override
    public void d(String message, Throwable tr) {
        mLogger.debug(message, tr);
    }

    @Override
    public void e(String message) {

    }

    @Override
    public void e(String message, Throwable tr) {

    }

    @Override
    public void i(String message) {

    }

    @Override
    public void i(String message, Throwable tr) {

    }

    @Override
    public void v(String message) {
        mLogger.info(message);
    }

    @Override
    public void v(String message, Throwable tr) {

    }

    @Override
    public void w(String message) {

    }

    @Override
    public void w(String message, Throwable tr) {

    }

    @Override
    public void userInteraction(String message) {

    }

    @Override
    public void onCreate(String message) {

    }

    @Override
    public void onStart(String message) {

    }

    @Override
    public void onRestart(String message) {

    }

    @Override
    public void onResume(String message) {

    }

    @Override
    public void onPause(String message) {

    }

    @Override
    public void onStop(String message) {

    }

    @Override
    public void onDestroy(String message) {

    }

    @Override
    public void onAttach(String message) {

    }

    @Override
    public void onCreateView(String message) {

    }

    @Override
    public void onActivityCreated(String message) {

    }

    @Override
    public void onDestroyView(String message) {

    }

    @Override
    public void onDetach(String message) {

    }

    private void configureLogger(String directory, long maxFileSize)  {
        LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(directory + "logs.txt");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(maxFileSize);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }

    private String getDirectory() {
        String directory = Environment.getExternalStorageDirectory()
                + File.separator + APPLICATION_NAME + File.separator + "Files"
                + File.separator;

        File file = new File(directory);

        if(!file.exists()) {
            file.mkdirs();
        }

        return  directory;
    }
}
