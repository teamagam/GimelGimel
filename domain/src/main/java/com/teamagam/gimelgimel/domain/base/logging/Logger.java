package com.teamagam.gimelgimel.domain.base.logging;

/**
 * Log wrapper interface to abstract the use of loggers.
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

}
