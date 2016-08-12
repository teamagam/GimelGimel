package com.teamagam.gimelgimel.app.injectors.modules;


import android.content.Context;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.data.repository.MessagesDataRepository;
import com.teamagam.gimelgimel.domain.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

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
    ThreadExecutor provideThreadExecutor() {
        return new ThreadExecutor() {
            @Override
            public void execute(Runnable command) {
                new Thread(command).start();
            }
        };
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread() {
        return new PostExecutionThread() {
            @Override
            public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }
        };
    }

}
