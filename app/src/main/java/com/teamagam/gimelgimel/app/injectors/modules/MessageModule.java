package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;
import com.teamagam.gimelgimel.data.message.repository.DataNetworkStateReceiverListener;
import com.teamagam.gimelgimel.data.message.repository.DataOutGoingMessageQueueSender;
import com.teamagam.gimelgimel.data.message.repository.DataOutGoingMessagesQueue;
import com.teamagam.gimelgimel.data.message.repository.MessageSenderData;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.OutGoingMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.OutGoingMessagesEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.data.notifications.MessageNotificationsSubject;
import com.teamagam.gimelgimel.data.response.adapters.ServerDataMapper;
import com.teamagam.gimelgimel.data.user.repository.DataNetworkStateRepository;
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
  OutGoingMessagesQueue provideOutGoingMessagesQueue(DataOutGoingMessagesQueue dataOutGoingMessagesQueue) {
    return dataOutGoingMessagesQueue;
  }

  @Provides
  @Singleton
  DataOutGoingMessagesQueue provideDataOutGoingMessagesQueue(OutGoingMessagesDao outGoingMessagesDao,
      OutGoingMessagesEntityMapper messagesEntityMapper) {
    return new DataOutGoingMessagesQueue(outGoingMessagesDao, messagesEntityMapper);
  }

  @Provides
  @Singleton
  MessageSender provideMessageSender(MessageSenderData messageSenderData) {
    return messageSenderData;
  }

  @Provides
  @Singleton
  MessageSenderData provideMessageSenderData(CloudMessagesSource cloudMessagesSource,
      ServerDataMapper serverDataMapper) {
    return new MessageSenderData(cloudMessagesSource, serverDataMapper);
  }

  @Provides
  @Singleton
  NetworkStateRepository provideNetworkStateRepository(DataNetworkStateRepository dataNetworkStateRepository) {
    return dataNetworkStateRepository;
  }

  @Provides
  @Singleton
  DataNetworkStateRepository provideNetworkStateDataRepository(NetworkStateReceiverListener networkStateReceiverListener) {
    return new DataNetworkStateRepository(networkStateReceiverListener);
  }

  @Provides
  @Singleton
  NetworkStateReceiverListener provideNetworkStateReceiverListener(DataNetworkStateReceiverListener dataNetworkStateReceiverListener) {
    return dataNetworkStateReceiverListener;
  }

  @Provides
  @Singleton
  DataNetworkStateReceiverListener provideNetworkStateReceiverListenerImpl(Context context) {
    return new DataNetworkStateReceiverListener(context);
  }

  @Provides
  @Singleton
  OutGoingMessageQueueSender provideOutGoingMessageQueueSender(DataOutGoingMessageQueueSender dataOutGoingMessageQueueSender) {
    return dataOutGoingMessageQueueSender;
  }

  @Provides
  @Singleton
  DataOutGoingMessageQueueSender provideDataOutGoingMessageQueueSender(MessageNotifications messageNotification,
      MessageSender messageSender,
      OutGoingMessagesQueue outGoingMessagesQueue) {
    return new DataOutGoingMessageQueueSender(messageNotification, messageSender,
        outGoingMessagesQueue);
  }
}