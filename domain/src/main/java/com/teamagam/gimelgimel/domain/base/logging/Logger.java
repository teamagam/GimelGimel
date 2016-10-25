package com.teamagam.gimelgimel.domain.base.logging;

/**
 * Log wrapper class to abstract the use of loggers.
 * Contains application-specific logging functionality.
 */
//TODO: move to app project (it changes around 40 files, so it should be done in develop when its stable/end of sprint)
public interface Logger extends DomainLogger {
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
