package com.teamagam.gimelgimel.app.common.logging;

import android.util.Log;

/**
 * Wraps the native android logger and delegates logging calls to it.
 * Special logging methods are logged with DEBUG verbosity with a suitable prefix
 */
class NativeLogger extends BaseLifecycleLogger {

    private static final String USER_INTERACTION_PREFIX = "User Interaction";
    private static final String LIFE_CYCLE_PREFIX = "Life Cycle";

    private String mTag;

    public NativeLogger(String tag) {
        mTag = tag;
    }

    @Override
    public void d(String message) {
        Log.d(mTag, message);
    }

    @Override
    public void d(String message, Throwable tr) {
        Log.d(mTag, message, tr);
    }

    @Override
    public void e(String message) {
        Log.e(mTag, message);
    }

    @Override
    public void e(String message, Throwable tr) {
        Log.e(mTag, message, tr);
    }

    @Override
    public void i(String message) {
        Log.i(mTag, message);
    }

    @Override
    public void i(String message, Throwable tr) {
        Log.i(mTag, message, tr);
    }

    @Override
    public void v(String message) {
        Log.v(mTag, message);
    }

    @Override
    public void v(String message, Throwable tr) {
        Log.v(mTag, message, tr);
    }

    @Override
    public void w(String message) {
        Log.w(mTag, message);
    }

    @Override
    public void w(String message, Throwable tr) {
        Log.w(mTag, message, tr);
    }

    @Override
    public void userInteraction(String message) {
        logCustom(USER_INTERACTION_PREFIX, message);
    }

    @Override
    public void onCreate(String message) {
        logCustom(createLifecyclePrefix("onCreate"), message);
    }

    @Override
    public void onStart(String message) {
        logCustom(createLifecyclePrefix("onStart"), message);
    }

    @Override
    public void onRestart(String message) {
        logCustom(createLifecyclePrefix("onRestart"), message);
    }

    @Override
    public void onResume(String message) {
        logCustom(createLifecyclePrefix("onResume"), message);
    }

    @Override
    public void onPause(String message) {
        logCustom(createLifecyclePrefix("onPause"), message);
    }

    @Override
    public void onStop(String message) {
        logCustom(createLifecyclePrefix("onStop"), message);
    }

    @Override
    public void onDestroy(String message) {
        logCustom(createLifecyclePrefix("onDestroy"), message);
    }

    @Override
    public void onAttach(String message) {
        logCustom(createLifecyclePrefix("onAttach"), message);
    }

    @Override
    public void onCreateView(String message) {
        logCustom(createLifecyclePrefix("onCreateView"), message);
    }

    @Override
    public void onActivityCreated(String message) {
        logCustom(createLifecyclePrefix("onActivityCreated"), message);
    }

    @Override
    public void onDestroyView(String message) {
        logCustom(createLifecyclePrefix("onDestroyView"), message);
    }

    @Override
    public void onDetach(String message) {
        logCustom(createLifecyclePrefix("onDetach"), message);
    }

    private String createLifecyclePrefix(String lifecycleType) {
        return LIFE_CYCLE_PREFIX + String.format("(%s)", lifecycleType);
    }

    private void logCustom(String prefix, String message) {
        Log.d(mTag, String.format("[%s] %s", prefix, message));
    }
}
