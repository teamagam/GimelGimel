package com.teamagam.gimelgimel.app;

import  android.app.Application;
import android.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.utils.BasicStringSecurity;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;

public class GGApplication extends Application {

    private SecuredPreferenceUtil mPrefs;
    private char[] mPrefSecureKey = ("GGApplicationSecuredKey!!!").toCharArray();


    @Override
    public void onCreate() {
        super.onCreate();

        LoggerFactory.init(this);


        // Initialize the fresco plugin.
        // Should be here instead of each activity
        Fresco.initialize(this);

    }

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