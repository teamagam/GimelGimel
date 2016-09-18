package com.teamagam.gimelgimel.app.injectors.modules;

import android.preference.PreferenceManager;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.utils.AndroidPreferencesProvider;
import com.teamagam.gimelgimel.app.utils.BasicStringSecurity;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;
import com.teamagam.gimelgimel.data.user.repository.PreferencesProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides preferences which will live during the application lifecycle.
 */
@Module
public class PreferencesModule {

    private final GGApplication mApplication;
    private final char[] mSecureString;

    public PreferencesModule(GGApplication application, char[] secureString) {
        mApplication = application;
        mSecureString = secureString;
    }

    @Provides
    @Singleton
    SecuredPreferenceUtil provideSharedPreferences(){
        SecuredPreferenceUtil mPrefs = new SecuredPreferenceUtil(mApplication.getResources(),
                PreferenceManager.getDefaultSharedPreferences(mApplication),
                new BasicStringSecurity(mSecureString));

        loadDefaultXmlValues(R.xml.pref_general);
        loadDefaultXmlValues(R.xml.pref_mesages);

        return mPrefs;
    }

    @Provides
    @Singleton
    PreferencesProvider providePreferencesProvider() {
        return new AndroidPreferencesProvider(mApplication.getPrefs());
    }

    private void loadDefaultXmlValues(int xmlId) {
        PreferenceManager.setDefaultValues(mApplication, xmlId, false);
    }
}
