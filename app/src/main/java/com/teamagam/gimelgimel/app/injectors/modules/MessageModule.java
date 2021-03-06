package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.app.message.DataAlertMessageTextFormatter;
import com.teamagam.gimelgimel.data.message.repository.DataMessageSender;
import com.teamagam.gimelgimel.data.message.repository.DataNetworkStateReceiverListener;
import com.teamagam.gimelgimel.data.message.repository.DataOutGoingMessageQueueSender;
import com.teamagam.gimelgimel.data.message.repository.DataOutGoingMessagesQueue;
import com.teamagam.gimelgimel.data.message.repository.search.DataMessagesDaoSearcher;
import com.teamagam.gimelgimel.data.notifications.MessageNotificationsSubject;
import com.teamagam.gimelgimel.domain.messages.AlertMessageTextFormatter;
import com.teamagam.gimelgimel.domain.messages.MessageSender;
import com.teamagam.gimelgimel.domain.messages.NetworkStateReceiverListener;
import com.teamagam.gimelgimel.domain.messages.OutGoingMessageQueueSender;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.messages.search.MessagesTextSearcher;
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
  MessageSender provideMessageSender(DataMessageSender dataMessageSender) {
    return dataMessageSender;
  }

  @Provides
  @Singleton
  NetworkStateReceiverListener provideNetworkStateReceiverListener(DataNetworkStateReceiverListener dataNetworkStateReceiverListener) {
    return dataNetworkStateReceiverListener;
  }

  @Provides
  @Singleton
  OutGoingMessageQueueSender provideOutGoingMessageQueueSender(DataOutGoingMessageQueueSender dataOutGoingMessageQueueSender) {
    return dataOutGoingMessageQueueSender;
  }

  @Provides
  @Singleton
  MessagesTextSearcher provideMessagesTextSearcher(DataMessagesDaoSearcher dataMessagesDaoSearcher) {
    return dataMessagesDaoSearcher;
  }

  @Provides
  @Singleton
  AlertMessageTextFormatter provideAlertMessageTextCreator(DataAlertMessageTextFormatter alertMessageTextCreatorData) {
    return alertMessageTextCreatorData;
  }
}