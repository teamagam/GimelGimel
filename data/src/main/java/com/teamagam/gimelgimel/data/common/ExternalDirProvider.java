package com.teamagam.gimelgimel.data.common;

import android.content.Context;
import android.os.Environment;

import com.teamagam.gimelgimel.data.config.Constants;

import java.io.File;

import javax.inject.Inject;

public class ExternalDirProvider {

    public static final String FILES_DIR_NAME = "files";
    public static final String CACHE_DIR_NAME = "cache";

    private final Context mContext;

    private File mExternalFilesDir;
    private File mExternalCacheDir;

    @Inject
    public ExternalDirProvider(Context context) {
        mContext = context;
    }

    public File getExternalFilesDir() {
        if (mExternalFilesDir == null) {
            mExternalFilesDir = createExternalFilesDir();
        }
        return mExternalFilesDir;
    }

    public File getExternalCacheDir() {
        if (mExternalCacheDir == null) {
            mExternalCacheDir = createExternalCacheDir();
        }
        return mExternalCacheDir;
    }

    private File createExternalFilesDir() {
        if (Constants.USE_DOWNLOADS_DIR_AS_HOME) {
            return getExternalDir(FILES_DIR_NAME);
        } else {
            return mContext.getExternalFilesDir(null);
        }
    }

    private File createExternalCacheDir() {
        if (Constants.USE_DOWNLOADS_DIR_AS_HOME) {
            return getExternalDir(CACHE_DIR_NAME);
        } else {
            return mContext.getExternalCacheDir();
        }
    }

    private File getExternalDir(String dirName) {
        File externalDir = new File(getExternalDirPath(dirName));
        if (!externalDir.exists()) {
            externalDir.mkdirs();
        }
        return externalDir;
    }

    private String getExternalDirPath(String dirName) {
        return getAppHomeDirPath() + File.separator + dirName;
    }

    private String getAppHomeDirPath() {
        return getDownloadsDirPath() + File.separator + mContext.getPackageName();
    }

    private String getDownloadsDirPath() {
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }
}
