package com.teamagam.gimelgimel.app.common.logging;

/**
 * Decorator class that filters log methods according to given verbosity configuration
 */
public class VerbosityFilterLogWrapperDecorator implements LogWrapper {

    private LogWrapper mDecoratedLogWrapper;
    private VerbosityConfiguration mVerbosityConfiguration;

    public VerbosityFilterLogWrapperDecorator(LogWrapper logWrapper,
                                              VerbosityConfiguration configuration) {
        mDecoratedLogWrapper = logWrapper;
        mVerbosityConfiguration = configuration;
    }

    @Override
    public void d(String message) {
        if (mVerbosityConfiguration.isLoggingDebug()) {
            mDecoratedLogWrapper.d(message);
        }
    }

    @Override
    public void d(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingDebug()) {
            mDecoratedLogWrapper.d(message, tr);
        }
    }

    @Override
    public void e(String message) {
        if (mVerbosityConfiguration.isLoggingError()) {
            mDecoratedLogWrapper.e(message);
        }
    }

    @Override
    public void e(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingError()) {
            mDecoratedLogWrapper.e(message, tr);
        }
    }

    @Override
    public void i(String message) {
        if (mVerbosityConfiguration.isLoggingInfo()) {
            mDecoratedLogWrapper.i(message);
        }
    }

    @Override
    public void i(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingInfo()) {
            mDecoratedLogWrapper.i(message, tr);
        }
    }

    @Override
    public void v(String message) {
        if (mVerbosityConfiguration.isLoggingVerbose()) {
            mDecoratedLogWrapper.v(message);
        }
    }

    @Override
    public void v(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingVerbose()) {
            mDecoratedLogWrapper.v(message, tr);
        }
    }

    @Override
    public void w(String message) {
        if (mVerbosityConfiguration.isLoggingWarning()) {
            mDecoratedLogWrapper.w(message);
        }
    }

    @Override
    public void w(String message, Throwable tr) {
        if (mVerbosityConfiguration.isLoggingWarning()) {
            mDecoratedLogWrapper.w(message, tr);
        }
    }

    @Override
    public void userInteraction(String message) {
        if (mVerbosityConfiguration.isLoggingUserInteraction()) {
            mDecoratedLogWrapper.userInteraction(message);
        }
    }

    @Override
    public void onCreate() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onCreate();
        }
    }

    @Override
    public void onCreate(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onCreate(message);
        }
    }

    @Override
    public void onStart() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onStart();
        }
    }

    @Override
    public void onStart(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onStart(message);
        }
    }

    @Override
    public void onRestart() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onResume();
        }
    }

    @Override
    public void onRestart(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onResume(message);
        }
    }

    @Override
    public void onResume() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onResume();
        }
    }

    @Override
    public void onResume(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onResume(message);
        }
    }

    @Override
    public void onPause() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onPause();
        }
    }

    @Override
    public void onPause(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onPause(message);
        }
    }

    @Override
    public void onStop() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onStop();
        }
    }

    @Override
    public void onStop(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onStop(message);
        }
    }

    @Override
    public void onDestroy() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onDestroy();
        }
    }

    @Override
    public void onDestroy(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onDestroy(message);
        }
    }

    @Override
    public void onAttach() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onAttach();
        }
    }

    @Override
    public void onAttach(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onAttach(message);
        }
    }

    @Override
    public void onCreateView() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onCreateView();
        }
    }

    @Override
    public void onCreateView(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onCreateView(message);
        }
    }

    @Override
    public void onActivityCreated() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onActivityCreated();
        }
    }

    @Override
    public void onActivityCreated(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onActivityCreated(message);
        }
    }

    @Override
    public void onDestroyView() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onDestroyView();
        }
    }

    @Override
    public void onDestroyView(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onDestroy(message);
        }
    }

    @Override
    public void onDetach() {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onDetach();
        }
    }

    @Override
    public void onDetach(String message) {
        if (mVerbosityConfiguration.isLoggingLifecycle()) {
            mDecoratedLogWrapper.onDetach(message);
        }
    }
}
