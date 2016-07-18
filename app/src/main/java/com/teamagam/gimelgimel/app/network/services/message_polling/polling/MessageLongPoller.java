package com.teamagam.gimelgimel.app.network.services.message_polling.polling;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.network.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;

/**
 * Polls messages from remote GGMessagingAPI resource and applies a
 * {@link IPolledMessagesProcessor} process method on polled messages.
 * <p/>
 * Uses preferences to read and update synchronization date for filtered requests
 */
public class MessageLongPoller implements com.teamagam.gimelgimel.app.network.services.message_polling.poller.IMessagePoller {

    private static final Logger sLogger = LoggerFactory.create(MessageLongPoller.class);

    private GGMessagingAPI mMessagingApi;
    private com.teamagam.gimelgimel.app.network.services.message_polling.poller.IPolledMessagesProcessor mProcessor;
    private PreferenceUtil mPreferenceUtil;

    public MessageLongPoller(GGMessagingAPI messagingAPI,
                             com.teamagam.gimelgimel.app.network.services.message_polling.poller.IPolledMessagesProcessor polledMessagesProcessor,
                             PreferenceUtil preferenceUtil) {
        mMessagingApi = messagingAPI;
        mProcessor = polledMessagesProcessor;
        mPreferenceUtil = preferenceUtil;
    }

    @Override
    public void poll() throws ConnectionException {
        //get latest synchronized date from shared prefs
        long synchronizedDateMs = mPreferenceUtil.getLong(
                R.string.pref_latest_received_message_date_in_ms, 0);

        long newSynchronizationDate = poll(synchronizedDateMs);

        if (newSynchronizationDate > synchronizedDateMs) {
            sLogger.d("Updating latest synchronization date (ms) to : " + newSynchronizationDate);

            mPreferenceUtil.commitLong(R.string.pref_latest_received_message_date_in_ms,
                    newSynchronizationDate);
        }
    }

    /**
     * Polls for new messages and process them
     *
     * @param synchronizedDateMs - latest synchronization date in ms
     * @return - latest message date in ms
     */
    private long poll(long synchronizedDateMs) throws ConnectionException {
        sLogger.d("Polling for new messages with synchronization date (ms): " + synchronizedDateMs);

        Collection<Message> messages = getMessagesSynchronously(synchronizedDateMs);

        if (messages.isEmpty()) {
            sLogger.d("No new messages available");
            return synchronizedDateMs;
        }

        mProcessor.process(messages);

        Message maximumMessageDateMessage = getMaximumDateMessage(messages);

        return maximumMessageDateMessage.getCreatedAt().getTime();
    }

    /**
     * Synchronously gets messages from server with date filter
     *
     * @param minDateFilter - the date (in ms) filter to be used
     * @return messages with date gte fromDateAsMs
     */
    private Collection<Message> getMessagesSynchronously(long minDateFilter)
            throws ConnectionException {
        Call<List<Message>> messagesCall = buildMessageRequestCall(minDateFilter);
        return executeCall(messagesCall);
    }

    private Call<List<Message>> buildMessageRequestCall(long minDateFilter) {
        Call<List<Message>> messagesCall;
        if (minDateFilter == 0) {
            messagesCall = mMessagingApi.getMessages();
        } else {
            messagesCall = mMessagingApi.getMessagesFromDate(minDateFilter);
        }
        return messagesCall;
    }

    private List<Message> executeCall(Call<List<Message>> messagesCall) throws ConnectionException {
        try {
            return messagesCall.execute().body();
        } catch (SocketTimeoutException e) {
            sLogger.w("Socket timeout reached");
            return new ArrayList<>();
        } catch (IOException e) {
            throw new ConnectionException();
        }
    }

    private Message getMaximumDateMessage(Collection<Message> messages) {
        return Collections.max(messages, new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return lhs.getCreatedAt().compareTo(rhs.getCreatedAt());
            }
        });
    }
}
