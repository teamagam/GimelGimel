package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;
import com.teamagam.gimelgimel.data.message.repository.MessageSenderData;
import com.teamagam.gimelgimel.data.message.repository.NetworkStateReceiverListenerImpl;
import com.teamagam.gimelgimel.data.message.repository.OutGoingMessageQueueSenderImpl;
import com.teamagam.gimelgimel.data.message.repository.OutGoingMessagesDataQueue;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.data.notifications.MessageNotificationsSubject;
import com.teamagam.gimelgimel.data.response.adapters.ServerDataMapper;
import com.teamagam.gimelgimel.data.user.repository.NetworkStateRepositoryImpl;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.messages.MessageSender;
import com.teamagam.gimelgimel.domain.messages.NetworkStateReceiverListener;
import com.teamagam.gimelgimel.domain.messages.OutGoingMessageQueueSender;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.messages.repository.NetworkStateRepository;
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
    return new OutGoingMessagesDataQueue();
  }

  @Provides
  @Singleton
  MessageSender provideMessageSender(CloudMessagesSource cloudMessagesSource,
      ServerDataMapper serverDataMapper) {
    return new MessageSenderData(cloudMessagesSource, serverDataMapper);
  }

  @Provides
  @Singleton
  NetworkStateRepository provideNetworkStateRepository() {
    return new NetworkStateRepositoryImpl();
  }

  @Provides
  @Singleton
  NetworkStateReceiverListener provideNetworkStateReceiverListener(Context context) {
    return new NetworkStateReceiverListenerImpl(context);
  }

  @Provides
  @Singleton
  OutGoingMessageQueueSender provideOutGoingMessageQueueSender(MessageNotifications messageNotification,
      MessageSender messageSender,
      OutGoingMessagesQueue outGoingMessagesQueue,
      RetryWithDelay retryWithDelay) {
    return new OutGoingMessageQueueSenderImpl(messageNotification, messageSender,
        outGoingMessagesQueue, retryWithDelay);
  }
}