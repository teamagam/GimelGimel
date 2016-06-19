package com.teamagam.gimelgimel.app.common.logging;

import java.util.Collection;

/**
 * LogWrapper that holds multiple log-wrappers and delegate each log requests
 * to every log wrapper it holds
 */
public class MultipleLogWrapper implements LogWrapper {

    private Collection<LogWrapper> mLogWrappers;

    public MultipleLogWrapper(Collection<LogWrapper> logWrappers) {
        mLogWrappers = logWrappers;
    }

    @Override
    public void d(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.d(message);
        }
    }

    @Override
    public void d(String message, Throwable tr) {
        for (LogWrapper lw : mLogWrappers) {
            lw.d(message, tr);
        }
    }

    @Override
    public void e(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.e(message);
        }
    }

    @Override
    public void e(String message, Throwable tr) {
        for (LogWrapper lw : mLogWrappers) {
            lw.e(message, tr);
        }
    }

    @Override
    public void i(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.i(message);
        }
    }

    @Override
    public void i(String message, Throwable tr) {
        for (LogWrapper lw : mLogWrappers) {
            lw.i(message, tr);
        }
    }

    @Override
    public void v(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.v(message);
        }
    }

    @Override
    public void v(String message, Throwable tr) {
        for (LogWrapper lw : mLogWrappers) {
            lw.v(message, tr);
        }
    }

    @Override
    public void w(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.w(message);
        }
    }

    @Override
    public void w(String message, Throwable tr) {
        for (LogWrapper lw : mLogWrappers) {
            lw.w(message, tr);
        }
    }

    @Override
    public void userInteraction(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.userInteraction(message);
        }
    }

    @Override
    public void onCreate() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onCreate();
        }
    }

    @Override
    public void onCreate(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onCreate(message);
        }
    }

    @Override
    public void onStart() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onStart();
        }
    }

    @Override
    public void onStart(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onStart(message);
        }
    }

    @Override
    public void onRestart() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onRestart();
        }
    }

    @Override
    public void onRestart(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onRestart(message);
        }
    }

    @Override
    public void onResume() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onResume();
        }
    }

    @Override
    public void onResume(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onResume(message);
        }
    }

    @Override
    public void onPause() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onPause();
        }
    }

    @Override
    public void onPause(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onPause(message);
        }
    }

    @Override
    public void onStop() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onStop();
        }
    }

    @Override
    public void onStop(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onStop(message);
        }
    }

    @Override
    public void onDestroy() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onDestroy();
        }
    }

    @Override
    public void onDestroy(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onDestroy(message);
        }
    }

    @Override
    public void onAttach() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onAttach();
        }
    }

    @Override
    public void onAttach(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onAttach(message);
        }
    }

    @Override
    public void onCreateView() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onCreateView();
        }
    }

    @Override
    public void onCreateView(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onCreateView(message);
        }
    }

    @Override
    public void onActivityCreated() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onActivityCreated();
        }
    }

    @Override
    public void onActivityCreated(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onActivityCreated(message);
        }
    }

    @Override
    public void onDestroyView() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onDestroyView();
        }
    }

    @Override
    public void onDestroyView(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onDestroyView(message);
        }
    }

    @Override
    public void onDetach() {
        for (LogWrapper lw : mLogWrappers) {
            lw.onDetach();
        }
    }

    @Override
    public void onDetach(String message) {
        for (LogWrapper lw : mLogWrappers) {
            lw.onDetach(message);
        }
    }
}
