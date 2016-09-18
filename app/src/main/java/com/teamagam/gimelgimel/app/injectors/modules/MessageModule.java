package com.teamagam.gimelgimel.app.injectors.modules;


import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;
import com.teamagam.gimelgimel.domain.messages.interfaces.UserPreferences;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides message related collaborators.
 */
@Module
public class MessageModule {

    public MessageModule() {
    }

    @Provides
    @PerActivity
    UserPreferences providePresenterSharedPreferences(final SecuredPreferenceUtil prefs) {
        return new UserPreferences() {
            @Override
            public String getSenderName() {
                return prefs.getString(R.string.user_name_text_key);
            }
        };
    }
}