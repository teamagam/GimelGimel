package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.data.notifications.MessageNotificationsSubject;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides message related collaborators.
 */
@Module
public class MessageModule {

    @Provides
    @Singleton
    MessageNotifications provideMessageNotifications(MessageNotificationsSubject
                                                             msgNotifications) {
        return msgNotifications;
    }
}
