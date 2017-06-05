package com.teamagam.gimelgimel.data.message.poller;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.message.rest.exceptions.RetrofitException;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.poller.IMessagePoller;
import com.teamagam.gimelgimel.domain.messages.poller.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;

/**
 * Polls messages from remote GGMessagingAPI resource and applies a
 * {@link IPolledMessagesProcessor} process method on polled messages.
 * <p/>
 * Uses preferences to read and update synchronization date for filtered requests
 */
@Singleton
public class MessageLongPoller implements IMessagePoller {

  private final int NO_NEW_MESSAGES = -1;
  @Inject
  UserPreferencesRepository mPrefs;
  private GGMessagingAPI mMessagingApi;
  private MessageDataMapper mMessageDataMapper;
  private IPolledMessagesProcessor mProcessor;

  @Inject
  public MessageLongPoller(GGMessagingAPI messagingAPI, MessageDataMapper messageDataMapper,
      IPolledMessagesProcessor polledMessagesProcessor) {
    mMessagingApi = messagingAPI;
    mMessageDataMapper = messageDataMapper;
    mProcessor = polledMessagesProcessor;
  }

  @Override
  public Observable poll() {
    long synchronizedDateMs = mPrefs.getLong(Constants.LATEST_MESSAGE_DATE_KEY);

    return poll(synchronizedDateMs).map(this::getMaximumDate)
        .filter(newSynchronizationDate -> newSynchronizationDate != NO_NEW_MESSAGES)
        .doOnNext(newSynchronizationDate -> mPrefs.setPreference(Constants.LATEST_MESSAGE_DATE_KEY,
            newSynchronizationDate))
        .onErrorResumeNext(throwable -> {
          if (isPollingException(throwable)) {
            return Observable.just(synchronizedDateMs);
          } else {
            return Observable.error(throwable);
          }
        })
        .onBackpressureBuffer();
  }

  private boolean isPollingException(Throwable throwable) {
    return (throwable instanceof RetrofitException
        && throwable.getCause() instanceof SocketTimeoutException)
        || throwable instanceof SocketTimeoutException;
  }

  /**
   * Polls for new messages and process them
   *
   * @param synchronizedDateMs - latest synchronization date in ms
   * @return - latest message date in ms
   */
  private Observable<List<Message>> poll(long synchronizedDateMs) {
    return getMessagesAsynchronously(synchronizedDateMs).doOnNext(mProcessor::process);
  }

  /**
   * Synchronously gets messages from server with date filter
   *
   * @param minDateFilter - the date (in ms) filter to be used
   * @return messages with date gte fromDateAsMs
   */
  private Observable<List<Message>> getMessagesAsynchronously(long minDateFilter) {
    return mMessagingApi.getMessagesFromDate(minDateFilter).map(mMessageDataMapper::transform);
  }

  private long getMaximumDate(Collection<Message> messages) {
    if (messages.isEmpty()) {
      return NO_NEW_MESSAGES;
    } else {
      Message maximumMessageDateMessage = getMaximumDateMessage(messages);
      return maximumMessageDateMessage.getCreatedAt().getTime();
    }
  }

  private Message getMaximumDateMessage(Collection<Message> messages) {
    return Collections.max(messages,
        (lhs, rhs) -> lhs.getCreatedAt().compareTo(rhs.getCreatedAt()));
  }
}
