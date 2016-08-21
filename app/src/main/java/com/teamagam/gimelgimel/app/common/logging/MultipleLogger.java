package com.teamagam.gimelgimel.app.common.logging;

import com.teamagam.gimelgimel.domain.base.logging.Logger;

import java.util.Collection;

/**
 * Logger that holds multiple log-wrappers and delegate each log requests
 * to every log wrapper it holds
 */
class MultipleLogger implements Logger {

    private Collection<Logger> mLoggers;

    public MultipleLogger(Collection<Logger> loggers) {
        mLoggers = loggers;
    }

    @Override
    public void d(String message) {
        for (Logger lw : mLoggers) {
            lw.d(message);
        }
    }

    @Override
    public void d(String message, Throwable tr) {
        for (Logger lw : mLoggers) {
            lw.d(message, tr);
        }
    }

    @Override
    public void e(String message) {
        for (Logger lw : mLoggers) {
            lw.e(message);
        }
    }

    @Override
    public void e(String message, Throwable tr) {
        for (Logger lw : mLoggers) {
            lw.e(message, tr);
        }
    }

    @Override
    public void i(String message) {
        for (Logger lw : mLoggers) {
            lw.i(message);
        }
    }

    @Override
    public void i(String message, Throwable tr) {
        for (Logger lw : mLoggers) {
            lw.i(message, tr);
        }
    }

    @Override
    public void v(String message) {
        for (Logger lw : mLoggers) {
            lw.v(message);
        }
    }

    @Override
    public void v(String message, Throwable tr) {
        for (Logger lw : mLoggers) {
            lw.v(message, tr);
        }
    }

    @Override
    public void w(String message) {
        for (Logger lw : mLoggers) {
            lw.w(message);
        }
    }

    @Override
    public void w(String message, Throwable tr) {
        for (Logger lw : mLoggers) {
            lw.w(message, tr);
        }
    }

    @Override
    public void userInteraction(String message) {
        for (Logger lw : mLoggers) {
            lw.userInteraction(message);
        }
    }

    @Override
    public void onCreate() {
        for (Logger lw : mLoggers) {
            lw.onCreate();
        }
    }

    @Override
    public void onCreate(String message) {
        for (Logger lw : mLoggers) {
            lw.onCreate(message);
        }
    }

    @Override
    public void onStart() {
        for (Logger lw : mLoggers) {
            lw.onStart();
        }
    }

    @Override
    public void onStart(String message) {
        for (Logger lw : mLoggers) {
            lw.onStart(message);
        }
    }

    @Override
    public void onRestart() {
        for (Logger lw : mLoggers) {
            lw.onRestart();
        }
    }

    @Override
    public void onRestart(String message) {
        for (Logger lw : mLoggers) {
            lw.onRestart(message);
        }
    }

    @Override
    public void onResume() {
        for (Logger lw : mLoggers) {
            lw.onResume();
        }
    }

    @Override
    public void onResume(String message) {
        for (Logger lw : mLoggers) {
            lw.onResume(message);
        }
    }

    @Override
    public void onPause() {
        for (Logger lw : mLoggers) {
            lw.onPause();
        }
    }

    @Override
    public void onPause(String message) {
        for (Logger lw : mLoggers) {
            lw.onPause(message);
        }
    }

    @Override
    public void onStop() {
        for (Logger lw : mLoggers) {
            lw.onStop();
        }
    }

    @Override
    public void onStop(String message) {
        for (Logger lw : mLoggers) {
            lw.onStop(message);
        }
    }

    @Override
    public void onDestroy() {
        for (Logger lw : mLoggers) {
            lw.onDestroy();
        }
    }

    @Override
    public void onDestroy(String message) {
        for (Logger lw : mLoggers) {
            lw.onDestroy(message);
        }
    }

    @Override
    public void onAttach() {
        for (Logger lw : mLoggers) {
            lw.onAttach();
        }
    }

    @Override
    public void onAttach(String message) {
        for (Logger lw : mLoggers) {
            lw.onAttach(message);
        }
    }

    @Override
    public void onCreateView() {
        for (Logger lw : mLoggers) {
            lw.onCreateView();
        }
    }

    @Override
    public void onCreateView(String message) {
        for (Logger lw : mLoggers) {
            lw.onCreateView(message);
        }
    }

    @Override
    public void onActivityCreated() {
        for (Logger lw : mLoggers) {
            lw.onActivityCreated();
        }
    }

    @Override
    public void onActivityCreated(String message) {
        for (Logger lw : mLoggers) {
            lw.onActivityCreated(message);
        }
    }

    @Override
    public void onDestroyView() {
        for (Logger lw : mLoggers) {
            lw.onDestroyView();
        }
    }

    @Override
    public void onDestroyView(String message) {
        for (Logger lw : mLoggers) {
            lw.onDestroyView(message);
        }
    }

    @Override
    public void onDetach() {
        for (Logger lw : mLoggers) {
            lw.onDetach();
        }
    }

    @Override
    public void onDetach(String message) {
        for (Logger lw : mLoggers) {
            lw.onDetach(message);
        }
    }
}
