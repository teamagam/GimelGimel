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

import rx.Observable;

/**
 * Polls messages from remote GGMessagingAPI resource and applies a
 * {@link IPolledMessagesProcessor} process method on polled messages.
 * <p/>
 * Uses preferences to read and update synchronization date for filtered requests
 */
@Singleton
public class MessageLongPoller implements IMessagePoller {

    private final int NO_NEW_MESSAGES = -1;
    private GGMessagingAPI mMessagingApi;

    private MessageDataMapper mMessageDataMapper;
    private IPolledMessagesProcessor mProcessor;

    @Inject
    UserPreferencesRepository mPrefs;

    @Inject
    public MessageLongPoller(GGMessagingAPI messagingAPI,
                             MessageDataMapper messageDataMapper,
                             IPolledMessagesProcessor polledMessagesProcessor) {
        mMessagingApi = messagingAPI;
        mMessageDataMapper = messageDataMapper;
        mProcessor = polledMessagesProcessor;
    }

    @Override
    public Observable poll() {
        //get latest synchronized date from shared prefs
        long synchronizedDateMs = mPrefs.getLong(Constants.LATEST_MESSAGE_DATE_KEY);

        //            sLogger.d("Updating latest synchronization date (ms) to : " + newSynchronizationDate);
        return poll(synchronizedDateMs)
                .map(this::getMaximumDate)
                .filter(newSynchronizationDate -> newSynchronizationDate != NO_NEW_MESSAGES)
                .doOnNext(newSynchronizationDate ->
                        mPrefs.setPreference(Constants.LATEST_MESSAGE_DATE_KEY, newSynchronizationDate))
                .onErrorResumeNext(throwable -> {
                            if (throwable instanceof RetrofitException &&
                                    throwable.getCause() instanceof SocketTimeoutException) {
                                return Observable.just(synchronizedDateMs);
                            } else {
                                //IOException
                                return Observable.error(new ConnectionException());
                            }
                        }
                );
    }

    /**
     * Polls for new messages and process them
     *
     * @param synchronizedDateMs - latest synchronization date in ms
     * @return - latest message date in ms
     */
    private Observable<List<Message>> poll(long synchronizedDateMs) {
//        sLogger.d("Polling for new messages with synchronization date (ms): " + synchronizedDateMs);

        return getMessagesAsynchronously(synchronizedDateMs)
                .doOnNext(mProcessor::process);
    }

    /**
     * Synchronously gets messages from server with date filter
     *
     * @param minDateFilter - the date (in ms) filter to be used
     * @return messages with date gte fromDateAsMs
     */
    private Observable<List<Message>> getMessagesAsynchronously(long minDateFilter) {
        return mMessagingApi
                .getMessagesFromDate(minDateFilter)
                .map(mMessageDataMapper::transform);
//        .retry()
    }

    private long getMaximumDate(Collection<Message> messages) {
        if (messages.isEmpty()) {
//            sLogger.d("No new messages available");
            return NO_NEW_MESSAGES;
        } else {
            Message maximumMessageDateMessage = getMaximumDateMessage(messages);
            return maximumMessageDateMessage.getCreatedAt().getTime();
        }
    }

    private Message getMaximumDateMessage(Collection<Message> messages) {
        return Collections.max(messages, (lhs, rhs) -> lhs.getCreatedAt().compareTo(rhs.getCreatedAt()));
    }
}
