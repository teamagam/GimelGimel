package com.teamagam.gimelgimel.data.common;

import android.content.Context;
import android.os.Environment;

import com.teamagam.gimelgimel.data.config.Constants;

import java.io.File;

import javax.inject.Inject;

public class ExternalPathProvider {

    public static final String FILES_DIR_NAME = "files";
    public static final String CACHE_DIR_NAME = "cache";

    private final Context mContext;
    private final File mAppHomeDir;

    @Inject
    public ExternalPathProvider(Context context) {
        mContext = context;
        mAppHomeDir = new File(getAppHomeDirPath());
    }

    public File getExternalFilesDir() {
        if (Constants.USE_DOWNLOADS_DIR_AS_HOME) {
            return getExternalDir(FILES_DIR_NAME);
        } else {
            return mContext.getExternalFilesDir(null);
        }
    }

    public File getExternalCacheDir() {
        if (Constants.USE_DOWNLOADS_DIR_AS_HOME) {
            return getExternalDir(CACHE_DIR_NAME);
        } else {
            return mContext.getExternalCacheDir();
        }
    }

    private String getAppHomeDirPath() {
        return getDownloadsDirPath() + File.separator + mContext.getPackageName();
    }

    private String getDownloadsDirPath() {
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    private File getExternalDir(String dirName) {
        File dir = new File(mAppHomeDir + File.separator + dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
