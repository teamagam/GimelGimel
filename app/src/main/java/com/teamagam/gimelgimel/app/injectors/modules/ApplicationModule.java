package com.teamagam.gimelgimel.app.injectors.modules;


import android.content.Context;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.rx.schedulers.DataThread;
import com.teamagam.gimelgimel.app.common.rx.schedulers.UIThread;
import com.teamagam.gimelgimel.data.message.repository.MessagesDataRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.images.SendImageMessageInteractor;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

    private final GGApplication mApplication;

    public ApplicationModule(GGApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    MessagesRepository provideUserRepository(MessagesDataRepository messageRepo) {
        return messageRepo;
    }

    @Provides
    @Singleton
    SendImageMessagePresenter provideSendImageMessagePresenter(SendImageMessageInteractor interactor){
        return new SendImageMessagePresenter(interactor);
    }

    @Provides
    @Singleton
    SendImageMessageInteractor provideSendImageMessageInteractor(ThreadExecutor threadExecutor,
                                                                 PostExecutionThread postExecutionThread,
                                                                 ImagesRepository imagesRepository,
                                                                 MessagesRepository messagesRepository) {
        return new SendImageMessageInteractor(threadExecutor, postExecutionThread,
                imagesRepository, messagesRepository);
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(DataThread dataThread) {
        return dataThread;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread thread) {
        return thread;
    }

}
