package com.teamagam.gimelgimel.app.injectors.modules;


import com.teamagam.gimelgimel.data.repository.MessagesDataRepository;
import com.teamagam.gimelgimel.domain.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.interactors.SendMessage;
import com.teamagam.gimelgimel.domain.messages.entities.Message;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Dagger module that provides message related collaborators.
 */
@Module
public class MessageModule {

    private Message mMessage;

    public MessageModule() {
    }

    public MessageModule(Message message){
        mMessage = message;
    }

//    @Provides
//    @Singleton
//    SendMessage provideSendMessageUseCase(
//            MessagesRepository messagesRepository, ThreadExecutor threadExecutor,
//            PostExecutionThread postExecutionThread) {
//        return new SendMessage(mMessage, messagesRepository, threadExecutor, postExecutionThread);
//    }

    @Provides
    @Singleton
    SendMessage provideSendMessageUseCase() {
        MessagesDataRepository messagesRepository = new MessagesDataRepository();
        ThreadExecutor threadExecutor = new ThreadExecutor() {
            @Override
            public void execute(Runnable command) {
                new Thread(command).start();
            }
        };
        PostExecutionThread postExecutionThread = new PostExecutionThread() {
            @Override
            public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }
        };

        return new SendMessage(mMessage, messagesRepository, threadExecutor, postExecutionThread);
    }
}