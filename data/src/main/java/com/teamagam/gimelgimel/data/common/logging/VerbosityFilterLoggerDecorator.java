package com.teamagam.gimelgimel.data.common.logging;

/**
 * Decorator class that filters log methods according to given verbosity configuration
 */
class VerbosityFilterLoggerDecorator implements Logger {

    private Logger mDecoratedLogger;
    private VerbosityConfiguration mVerbosityConfiguration;

    public VerbosityFilterLoggerDecorator(Logger logger,
                                          VerbosityConfiguration configuration) {
        mDecoratedLogger = logger;
        mVerbosityConfiguration = configuration;
    }

    @Override
    public void d(String message) {
        if (mVerbosityConfiguration.isLoggingDebug()) {
            mDecoratedLogger.d(message);
        }
    }

    @Override
    public void d(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingDebug()) {
            mDecoratedLogger.d(message, tr);
        }
    }

    @Override
    public void e(String message) {
        if (mVerbosityConfiguration.isLoggingError()) {
            mDecoratedLogger.e(message);
        }
    }

    @Override
    public void e(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingError()) {
            mDecoratedLogger.e(message, tr);
        }
    }

    @Override
    public void i(String message) {
        if (mVerbosityConfiguration.isLoggingInfo()) {
            mDecoratedLogger.i(message);
        }
    }

    @Override
    public void i(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingInfo()) {
            mDecoratedLogger.i(message, tr);
        }
    }

    @Override
    public void v(String message) {
        if (mVerbosityConfiguration.isLoggingVerbose()) {
            mDecoratedLogger.v(message);
        }
    }

    @Override
    public void v(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingVerbose()) {
            mDecoratedLogger.v(message, tr);
        }
    }

    @Override
    public void w(String message) {
        if (mVerbosityConfiguration.isLoggingWarning()) {
            mDecoratedLogger.w(message);
        }
    }

    @Override
    public void w(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingWarning()) {
            mDecoratedLogger.w(message, tr);
        }
    }

    @Override
    public void userInteraction(String message) {
        if (mVerbosityConfiguration.isLoggingUserInteraction()) {
            mDecoratedLogger.userInteraction(message);
        }
    }

    @Override
    public void onCreate() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onCreate();
        }
    }

    @Override
    public void onCreate(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onCreate(message);
        }
    }

    @Override
    public void onStart() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onStart();
        }
    }

    @Override
    public void onStart(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onStart(message);
        }
    }

    @Override
    public void onRestart() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onResume();
        }
    }

    @Override
    public void onRestart(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onResume(message);
        }
    }

    @Override
    public void onResume() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onResume();
        }
    }

    @Override
    public void onResume(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onResume(message);
        }
    }

    @Override
    public void onPause() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onPause();
        }
    }

    @Override
    public void onPause(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onPause(message);
        }
    }

    @Override
    public void onStop() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onStop();
        }
    }

    @Override
    public void onStop(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onStop(message);
        }
    }

    @Override
    public void onDestroy() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onDestroy();
        }
    }

    @Override
    public void onDestroy(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onDestroy(message);
        }
    }

    @Override
    public void onAttach() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onAttach();
        }
    }

    @Override
    public void onAttach(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onAttach(message);
        }
    }

    @Override
    public void onCreateView() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onCreateView();
        }
    }

    @Override
    public void onCreateView(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onCreateView(message);
        }
    }

    @Override
    public void onActivityCreated() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onActivityCreated();
        }
    }

    @Override
    public void onActivityCreated(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onActivityCreated(message);
        }
    }

    @Override
    public void onDestroyView() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onDestroyView();
        }
    }

    @Override
    public void onDestroyView(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onDestroy(message);
        }
    }

    @Override
    public void onDetach() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onDetach();
        }
    }

    @Override
    public void onDetach(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogger.onDetach(message);
        }
    }
}
