package com.gimelgimel.domain.logging;

/**
 * Log wrapper class to abstract the use of loggers.
 * Contains application-specific logging functionality.
 */
public interface Logger {

    void d(String message);
    void d(String message, Throwable tr);

    void e(String message);
    void e(String message, Throwable tr);

    void i(String message);
    void i(String message, Throwable tr);

    void v(String message);
    void v(String message, Throwable tr);

    void w(String message);
    void w(String message, Throwable tr);

    void userInteraction(String message);

    void onCreate();
    void onCreate(String message);

    void onStart();
    void onStart(String message);

    void onRestart();
    void onRestart(String message);

    void onResume();
    void onResume(String message);

    void onPause();
    void onPause(String message);

    void onStop();
    void onStop(String message);

    void onDestroy();
    void onDestroy(String message);

    void onAttach();
    void onAttach(String message);

    void onCreateView();
    void onCreateView(String message);

    void onActivityCreated();
    void onActivityCreated(String message);

    void onDestroyView();
    void onDestroyView(String message);

    void onDetach();
    void onDetach(String message);
}
