package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.data.notifications.MessageNotificationsSubject;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides message related collaborators.
 */
@Module
@PerActivity
public class MessageModule {

    @Provides
    @PerActivity
    MessageNotifications provideMessageNotifications(MessageNotificationsSubject
                                                             msgNotifications) {
        return msgNotifications;
    }
}
