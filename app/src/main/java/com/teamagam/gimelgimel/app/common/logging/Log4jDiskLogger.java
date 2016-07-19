package com.teamagam.gimelgimel.app.common.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;


class Log4jDiskLogger extends BaseLifecycleLogger {

    private static final String VERBOSITY_USER_INTERACTION = "USER_INTERACTION";
    private static final String VERBOSITY_LIFECYCLE = "LIFECYCLE";

    private Logger mLogger;

    Log4jDiskLogger(String directory, String fileName, long maxFileSize, int maxBackupLogFiles, String tag) {
        try {
            mLogger = createLogger(directory, fileName, maxFileSize, maxBackupLogFiles, tag);
        } catch (Exception ex) {
            mLogger = null;
        }
    }

    private Logger createLogger(String directory, String fileName, long maxFileSize, int maxBackupLogFiles, String tag) {
        createDirectory(directory);
        configureLogger(directory, fileName, maxFileSize, maxBackupLogFiles);

        return Logger.getLogger(tag);
    }

    @Override
    public void d(String message) {
        if (mLogger != null) {
            mLogger.debug(message);
        }
    }

    @Override
    public void d(String message, Throwable tr) {
        if (mLogger != null) {
            mLogger.debug(message, tr);
        }
    }

    @Override
    public void e(String message) {
        if (mLogger != null) {
            mLogger.error(message);
        }
    }

    @Override
    public void e(String message, Throwable tr) {
        if (mLogger != null) {
            mLogger.error(message, tr);
        }
    }

    @Override
    public void i(String message) {
        if (mLogger != null) {
            mLogger.info(message);
        }
    }

    @Override
    public void i(String message, Throwable tr) {
        if (mLogger != null) {
            mLogger.info(message, tr);
        }
    }

    @Override
    public void v(String message) {
        if (mLogger != null) {
            mLogger.trace(message);
        }
    }

    @Override
    public void v(String message, Throwable tr) {
        if (mLogger != null) {
            mLogger.trace(message, tr);
        }
    }

    @Override
    public void w(String message) {
        if (mLogger != null) {
            mLogger.warn(message);
        }
    }

    @Override
    public void w(String message, Throwable tr) {
        if (mLogger != null) {
            mLogger.warn(message, tr);
        }
    }

    @Override
    public void userInteraction(String message) {
        if (mLogger != null) {
            mLogger.info(createLogLine(VERBOSITY_USER_INTERACTION, message));
        }
    }

    @Override
    public void onCreate(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onCreate", message));
        }
    }

    @Override
    public void onStart(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onStart", message));
        }
    }

    @Override
    public void onRestart(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onRestart", message));
        }
    }

    @Override
    public void onResume(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onResume", message));
        }
    }

    @Override
    public void onPause(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onPause", message));
        }
    }

    @Override
    public void onStop(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onStop", message));
        }
    }

    @Override
    public void onDestroy(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onDestroy", message));
        }
    }

    @Override
    public void onAttach(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onAttach", message));
        }
    }

    @Override
    public void onCreateView(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onCreateView", message));
        }
    }

    @Override
    public void onActivityCreated(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onActivityCreated", message));
        }
    }

    @Override
    public void onDestroyView(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onDestroyView", message));
        }
    }

    @Override
    public void onDetach(String message) {
        if (mLogger != null) {
            mLogger.trace(createLifeCycleLogLine(VERBOSITY_LIFECYCLE, "onDetach", message));
        }
    }

    private void configureLogger(String directory, String fileName, long maxFileSize, int maxBackupLogFiles) {
        LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(directory + File.separator + fileName);
        logConfigurator.setRootLevel(Level.ALL);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(maxFileSize);
        logConfigurator.setMaxBackupSize(maxBackupLogFiles);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }

    private void createDirectory(String directoryPath) {
        File file = new File(directoryPath);

        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private String createLogLine(String verbosity, String message) {
        return String.format("[%s] %s", verbosity, message);
    }

    private String createLifeCycleLogLine(String verbosity, String lifeCycle, String message) {
        return String.format("[%s] [%s] %s", verbosity, lifeCycle, message);
    }
}
