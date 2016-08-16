package com.teamagam.gimelgimel.app.injectors.modules;


import com.teamagam.gimelgimel.presentation.scopes.PerFragment;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

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

}