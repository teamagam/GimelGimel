package com.teamagam.gimelgimel.app.common.logging;

class Log4jDiskLogger extends BaseLifecycleLogger {

    private static final String VERBOSITY_USER_INTERACTION = "USER_INTERACTION";
    private static final String VERBOSITY_LIFECYCLE = "LIFECYCLE";

    private org.apache.log4j.Logger mLogger;

    Log4jDiskLogger(org.apache.log4j.Logger logger) {
        mLogger = logger;
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
        mLogger.error(message);
    }

    @Override
    public void e(String message, Throwable tr) {
        mLogger.error(message, tr);
    }

    @Override
    public void i(String message) {
        mLogger.info(message);
    }

    @Override
    public void i(String message, Throwable tr) {
        mLogger.info(message, tr);
    }

    @Override
    public void v(String message) {
        mLogger.trace(message);
    }

    @Override
    public void v(String message, Throwable tr) {
        mLogger.trace(message, tr);
    }

    @Override
    public void w(String message) {
        mLogger.warn(message);
    }

    @Override
    public void w(String message, Throwable tr) {
        mLogger.warn(message, tr);
    }

    @Override
    public void userInteraction(String message) {
        mLogger.info(createLogLine(VERBOSITY_USER_INTERACTION, message));
    }

    @Override
    public void onCreate(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onCreate", message));
    }

    @Override
    public void onStart(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onStart", message));
    }

    @Override
    public void onRestart(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onRestart", message));
    }

    @Override
    public void onResume(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onResume", message));
    }

    @Override
    public void onPause(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onPause", message));
    }

    @Override
    public void onStop(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onStop", message));
    }

    @Override
    public void onDestroy(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onDestroy", message));
    }

    @Override
    public void onAttach(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onAttach", message));
    }

    @Override
    public void onCreateView(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onCreateView", message));
    }

    @Override
    public void onActivityCreated(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onActivityCreated", message));
    }

    @Override
    public void onDestroyView(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onDestroyView", message));
    }

    @Override
    public void onDetach(String message) {
        mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onDetach", message));
    }

    private String createLogLine(String verbosity, String message) {
        return String.format("[%s] %s", verbosity, message);
    }

    private String createLifeCycleLogLine(String verbosity, String lifeCycle, String message) {
        return String.format("[%s] [%s] %s", verbosity, lifeCycle, message);
    }
}
