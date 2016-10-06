package com.teamagam.gimelgimel.data.message.poller.polling;

import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.user.repository.PreferencesProvider;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
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

    private GGMessagingAPI mMessagingApi;

    private MessageDataMapper mMessageDataMapper;
    private IPolledMessagesProcessor mProcessor;

    @Inject
    PreferencesProvider mPrefs;

    @Inject
    public MessageLongPoller(GGMessagingAPI messagingAPI,
                             MessageDataMapper messageDataMapper,
                             IPolledMessagesProcessor polledMessagesProcessor) {
        mMessagingApi = messagingAPI;
        mMessageDataMapper = messageDataMapper;
        mProcessor = polledMessagesProcessor;
    }

    @Override
    public void poll() throws IMessagePoller.ConnectionException {
        //get latest synchronized date from shared prefs
        long synchronizedDateMs = mPrefs.getLatestMessageDate();

        Observable<Long> observable = poll(synchronizedDateMs);

        observable.doOnNext(newSynchronizationDate -> {
            if (newSynchronizationDate > synchronizedDateMs) {
//            sLogger.d("Updating latest synchronization date (ms) to : " + newSynchronizationDate);

                mPrefs.updateLatestMessageDate(newSynchronizationDate);
            }
        }).subscribe();
    }

    /**
     * Polls for new messages and process them
     *
     * @param synchronizedDateMs - latest synchronization date in ms
     * @return - latest message date in ms
     */
    private Observable<Long> poll(long synchronizedDateMs) throws ConnectionException {
//        sLogger.d("Polling for new messages with synchronization date (ms): " + synchronizedDateMs);

        return getMessagesAsynchronously(synchronizedDateMs)
                .doOnNext(mProcessor::process)
                .map(messages -> getMaximumDateMessage(messages, synchronizedDateMs));
    }

    /**
     * Synchronously gets messages from server with date filter
     *
     * @param minDateFilter - the date (in ms) filter to be used
     * @return messages with date gte fromDateAsMs
     */
    private Observable<List<Message>> getMessagesAsynchronously(long minDateFilter)
            throws ConnectionException {
        return mMessagingApi
                .getMessagesFromDate(minDateFilter)
                .map(mMessageDataMapper::transform)
                .onErrorReturn(this::handleError);
//        .retry()
    }

    private List<Message> handleError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
//            sLogger.w("Socket timeout reached");
            return new ArrayList<>();
        }
//        } else {
//            throw new ConnectionException();
//        }
        return null;
    }

    private long getMaximumDateMessage(Collection<Message> messages, long defaultTime) {
        if (messages.isEmpty()) {
//            sLogger.d("No new messages available");
            return defaultTime;
        } else {
            Message maximumMessageDateMessage = getMaximumDateMessage(messages);
            return maximumMessageDateMessage.getCreatedAt().getTime();
        }
    }

    private Message getMaximumDateMessage(Collection<Message> messages) {
        return Collections.max(messages, (lhs, rhs) -> lhs.getCreatedAt().compareTo(rhs.getCreatedAt()));
    }
}
