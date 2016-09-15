package com.teamagam.gimelgimel.app.injectors.modules;


import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.geometries.SendGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.geometries.repository.GeoEntityRepository;
import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.interfaces.UserPreferences;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
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
    SendMessageInteractor provideSendMessageUseCase(
            MessagesRepository messagesRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        return new SendMessageInteractor(messagesRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SendGeoMessageInteractor provideSendGeoMessageUseCase(
            MessagesRepository messagesRepository, GeoEntityRepository geoEntityRepository,
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, UserPreferences prefs) {
        return new SendGeoMessageInteractor(threadExecutor, postExecutionThread, geoEntityRepository,
                messagesRepository, prefs);
    }

    @Provides
    @PerActivity
    UserPreferences providePresenterSharedPreferences(final SecuredPreferenceUtil prefs){
        return new UserPreferences() {
            @Override
            public String getSenderName() {
                return prefs.getString(R.string.user_name_text_key);
            }
        };
    }

}