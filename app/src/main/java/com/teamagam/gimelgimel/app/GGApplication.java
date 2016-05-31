package com.teamagam.gimelgimel.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.utils.BasicStringSecurity;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;

public class GGApplication extends Application {

    protected static final String TAG = "GGApplication";

    private SecuredPreferenceUtil mPrefs;
    //          TODO: clean
    private char[] mPrefSecureKey = ("GGApplicationSecuredKey!!!").toCharArray();

    public SecuredPreferenceUtil getPrefs() {
        if (mPrefs == null) {
            // Set up a preferences manager (with basic security)
            mPrefs = new SecuredPreferenceUtil(getResources(),
                    PreferenceManager.getDefaultSharedPreferences(this),
                    new BasicStringSecurity(mPrefSecureKey));
        }

        return mPrefs;
    }
}