package com.teamagam.gimelgimel.app.injectors.modules;


import com.teamagam.gimelgimel.app.injectors.scopes.PerFragment;
import com.teamagam.gimelgimel.domain.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.interactors.SendMessage;
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
    SendMessage provideSendMessageUseCase(
            MessagesRepository messagesRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        return new SendMessage(messagesRepository, threadExecutor, postExecutionThread);
    }

}