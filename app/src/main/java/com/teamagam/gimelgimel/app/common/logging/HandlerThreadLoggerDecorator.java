package com.teamagam.gimelgimel.app.common.logging;

import android.os.Handler;

import com.teamagam.gimelgimel.domain.base.logging.Logger;

/**
 * Executes logging methods on given handler.
 */
public class HandlerThreadLoggerDecorator implements Logger {

    private Logger mDecoratedLogger;

    private Handler mHandler;

    public HandlerThreadLoggerDecorator(
            Logger decoratedLogger, Handler handler) {
        mDecoratedLogger = decoratedLogger;
        mHandler = handler;
    }

    @Override
    public void d(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.d(message);
            }
        });
    }

    @Override
    public void d(final String message, final Throwable tr) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.d(message, tr);
            }
        });
    }

    @Override
    public void e(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.e(message);
            }
        });
    }

    @Override
    public void e(final String message, final Throwable tr) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.e(message,tr);
            }
        });
    }

    @Override
    public void i(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.i(message);
            }
        });
    }

    @Override
    public void i(final String message, final Throwable tr) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.i(message,tr);
            }
        });
    }

    @Override
    public void v(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.v(message);
            }
        });
    }

    @Override
    public void v(final String message, final Throwable tr) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.v(message,tr);
            }
        });
    }

    @Override
    public void w(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.w(message);
            }
        });
    }

    @Override
    public void w(final String message, final Throwable tr) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.w(message, tr);
            }
        });
    }

    @Override
    public void userInteraction(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.userInteraction(message);
            }
        });
    }

    @Override
    public void onCreate() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onCreate();
            }
        });
    }

    @Override
    public void onCreate(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onCreate(message);
            }
        });
    }

    @Override
    public void onStart() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onStart();
            }
        });
    }

    @Override
    public void onStart(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onStart(message);
            }
        });
    }

    @Override
    public void onRestart() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onRestart();
            }
        });
    }

    @Override
    public void onRestart(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onRestart(message);
            }
        });
    }

    @Override
    public void onResume() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onResume();
            }
        });
    }

    @Override
    public void onResume(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onResume(message);
            }
        });
    }

    @Override
    public void onPause() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onPause();
            }
        });
    }

    @Override
    public void onPause(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onPause(message);
            }
        });
    }

    @Override
    public void onStop() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onStop();
            }
        });
    }

    @Override
    public void onStop(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onStop(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onDestroy();
            }
        });
    }

    @Override
    public void onDestroy(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onDestroy(message);
            }
        });
    }

    @Override
    public void onAttach() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onAttach();
            }
        });
    }

    @Override
    public void onAttach(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onAttach(message);
            }
        });
    }

    @Override
    public void onCreateView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onCreateView();
            }
        });
    }

    @Override
    public void onCreateView(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onCreateView(message);
            }
        });
    }

    @Override
    public void onActivityCreated() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onActivityCreated();
            }
        });
    }

    @Override
    public void onActivityCreated(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onActivityCreated(message);
            }
        });
    }

    @Override
    public void onDestroyView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onDestroyView();
            }
        });
    }

    @Override
    public void onDestroyView(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onDestroyView(message);
            }
        });
    }

    @Override
    public void onDetach() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onDetach();
            }
        });
    }

    @Override
    public void onDetach(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDecoratedLogger.onDetach(message);
            }
        });
    }
}
