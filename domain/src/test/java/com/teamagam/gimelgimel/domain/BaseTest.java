package com.teamagam.gimelgimel.domain;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;

/**
 * Created on 12/1/2016.
 */

public class BaseTest {

    private static boolean isLoggerInitialized = false;

    public BaseTest() {
        if(!isLoggerInitialized ) {
            LoggerFactory.initialize(tag -> new EmptyLogger());
            isLoggerInitialized = true;
        }
    }

    private class EmptyLogger implements Logger {
        @Override
        public void d(String message) {

        }

        @Override
        public void d(String message, Throwable tr) {

        }

        @Override
        public void e(String message) {

        }

        @Override
        public void e(String message, Throwable tr) {

        }

        @Override
        public void i(String message) {

        }

        @Override
        public void i(String message, Throwable tr) {

        }

        @Override
        public void v(String message) {

        }

        @Override
        public void v(String message, Throwable tr) {

        }

        @Override
        public void w(String message) {

        }

        @Override
        public void w(String message, Throwable tr) {

        }
    }
}
