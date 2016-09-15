package com.teamagam.gimelgimel.app.injectors.modules;


import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.presentation.interfaces.PresenterSharedPreferences;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

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
    @PerFragment
    SendMessageInteractor provideSendMessageUseCase(
            MessagesRepository messagesRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        return new SendMessageInteractor(messagesRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerFragment
    PresenterSharedPreferences providePresenterSharedPreferences(final SecuredPreferenceUtil prefs) {
        return new PresenterSharedPreferences() {
            @Override
            public String getSenderName() {
                return prefs.getString(R.string.user_name_text_key);
            }
        };
    }
}