package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.data.message.repository.MessageSenderData;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.data.notifications.MessageNotificationsSubject;
import com.teamagam.gimelgimel.data.response.adapters.ServerDataMapper;
import com.teamagam.gimelgimel.domain.messages.MessageSender;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Dagger module that provides message related collaborators.
 */
@Module
public class MessageModule {

  @Provides
  @Singleton
  MessageNotifications provideMessageNotifications(MessageNotificationsSubject msgNotifications) {
    return msgNotifications;
  }

  @Provides
  @Singleton
  OutGoingMessagesQueue provideOutGoingMessagesQueue() {
    return new OutGoingMessagesQueue();
  }

  @Provides
  @Singleton
  MessageSender provideMessageSender(CloudMessagesSource cloudMessagesSource,
      ServerDataMapper serverDataMapper) {
    return new MessageSenderData(cloudMessagesSource, serverDataMapper);
  }
}
