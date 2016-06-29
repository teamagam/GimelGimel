package com.teamagam.gimelgimel.app.common.logging;

/**
 * Helps implementing lifecycle log methods
 */
abstract class BaseLifecycleLogger implements Logger {
    @Override
    public abstract void d(String message);

    @Override
    public abstract void d(String message, Throwable tr);

    @Override
    public abstract void e(String message);

    @Override
    public abstract void e(String message, Throwable tr);

    @Override
    public abstract void i(String message);

    @Override
    public abstract void i(String message, Throwable tr);

    @Override
    public abstract void v(String message);

    @Override
    public abstract void v(String message, Throwable tr);

    @Override
    public abstract void w(String message);

    @Override
    public abstract void w(String message, Throwable tr);

    @Override
    public abstract void userInteraction(String message);

    @Override
    public void onCreate() {
        onCreate("");
    }

    @Override
    public abstract void onCreate(String message);

    @Override
    public void onStart() {
        onStart("");
    }

    @Override
    public abstract void onStart(String message);

    @Override
    public void onRestart() {
        onRestart("");
    }

    @Override
    public abstract void onRestart(String message);

    @Override
    public void onResume() {
        onResume("");
    }

    @Override
    public abstract void onResume(String message);

    @Override
    public void onPause() {
        onPause("");
    }

    @Override
    public abstract void onPause(String message);

    @Override
    public void onStop() {
        onStop("");
    }

    @Override
    public abstract void onStop(String message);

    @Override
    public void onDestroy() {
        onDestroy("");
    }

    @Override
    public abstract void onDestroy(String message);

    @Override
    public void onAttach() {
        onAttach("");
    }

    @Override
    public abstract void onAttach(String message);

    @Override
    public void onCreateView() {
        onCreateView("");
    }

    @Override
    public abstract void onCreateView(String message);

    @Override
    public void onActivityCreated() {
        onActivityCreated("");
    }

    @Override
    public abstract void onActivityCreated(String message);

    @Override
    public void onDestroyView() {
        onDestroyView("");
    }

    @Override
    public abstract void onDestroyView(String message);

    @Override
    public void onDetach() {
        onDetach("");
    }

    @Override
    public abstract void onDetach(String message);
}
