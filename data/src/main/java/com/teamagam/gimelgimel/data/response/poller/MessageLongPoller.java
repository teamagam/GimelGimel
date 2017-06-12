package com.teamagam.gimelgimel.data.response.poller;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.response.adapters.ServerDataMapper;
import com.teamagam.gimelgimel.data.response.entity.AlertMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.GeometryMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.TextMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.UserLocationResponse;
import com.teamagam.gimelgimel.data.response.entity.VectorLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.UnknownResponse;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;
import com.teamagam.gimelgimel.data.response.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.response.rest.exceptions.RetrofitException;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.messages.poller.IMessagePoller;
import com.teamagam.gimelgimel.domain.messages.poller.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Polls messages from remote GGMessagingAPI resource and applies a
 * {@link IPolledMessagesProcessor} process method on polled messages.
 * <p/>
 * Uses preferences to read and update synchronization date for filtered requests
 */
@Singleton
public class MessageLongPoller implements IMessagePoller {
  private static Logger sLogger = LoggerFactory.create(MessageLongPoller.class.getSimpleName());

  private final int NO_NEW_MESSAGES = -1;
  @Inject
  UserPreferencesRepository mPrefs;
  private GGMessagingAPI mMessagingApi;
  private ServerDataMapper mServerDataMapper;
  private IPolledMessagesProcessor mProcessor;
  private ResponseProcessorVisitor mDataProcessorVisitor;

  @Inject
  public MessageLongPoller(GGMessagingAPI messagingAPI,
      ServerDataMapper serverDataMapper,
      IPolledMessagesProcessor polledMessagesProcessor) {
    mMessagingApi = messagingAPI;
    mServerDataMapper = serverDataMapper;
    mProcessor = polledMessagesProcessor;
    mDataProcessorVisitor = new ResponseProcessorVisitor();
  }

  @Override
  public Observable poll() {
    long synchronizedDateMs = mPrefs.getLong(Constants.LATEST_MESSAGE_DATE_KEY);

    return poll(synchronizedDateMs).toList()
        .map(this::getMaximumDate)
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
  private Observable<ServerResponse> poll(long synchronizedDateMs) {
    return getMessagesAsynchronously(synchronizedDateMs).flatMapIterable(i -> i)
        .doOnNext(m -> m.accept(mDataProcessorVisitor));
  }

  /**
   * Synchronously gets messages from server with date filter
   *
   * @param minDateFilter - the date (in ms) filter to be used
   * @return messages with date gte fromDateAsMs
   */
  private Observable<List<ServerResponse>> getMessagesAsynchronously(long minDateFilter) {
    return mMessagingApi.getMessagesFromDate(minDateFilter);
  }

  private long getMaximumDate(Collection<ServerResponse> messages) {
    if (messages.isEmpty()) {
      return NO_NEW_MESSAGES;
    } else {
      ServerResponse maximumMessageDateMessage = getMaximumDateMessage(messages);
      return maximumMessageDateMessage.getCreatedAt().getTime();
    }
  }

  private ServerResponse getMaximumDateMessage(Collection<ServerResponse> messages) {
    return Collections.max(messages,
        (lhs, rhs) -> lhs.getCreatedAt().compareTo(rhs.getCreatedAt()));
  }

  private class ResponseProcessorVisitor implements ResponseVisitor {
    @Override
    public void visit(UnknownResponse message) {
      sLogger.w("Unknown message received with id: " + message.getMessageId());
    }

    @Override
    public void visit(UserLocationResponse message) {
      mProcessor.process(mServerDataMapper.transform(message));
    }

    @Override
    public void visit(GeometryMessageResponse message) {
      mProcessor.process(mServerDataMapper.transform(message));
    }

    @Override
    public void visit(TextMessageResponse message) {
      mProcessor.process(mServerDataMapper.transform(message));
    }

    @Override
    public void visit(ImageMessageResponse message) {
      mProcessor.process(mServerDataMapper.transform(message));
    }

    @Override
    public void visit(VectorLayerResponse message) {
      mProcessor.process(mServerDataMapper.transform(message));
    }

    @Override
    public void visit(AlertMessageResponse message) {
      mProcessor.process(mServerDataMapper.transform(message));
    }
  }
}
