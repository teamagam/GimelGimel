package com.teamagam.gimelgimel.app.network.services.message_polling;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LogWrapper;
import com.teamagam.gimelgimel.app.common.logging.LogWrapperFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.network.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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
public class MessagePoller implements IMessagePoller {

    private static final LogWrapper LOGGER = LogWrapperFactory.create(MessagePoller.class);

    private GGMessagingAPI mMessagingApi;
    private IPolledMessagesProcessor mProcessor;
    private PreferenceUtil mPreferenceUtil;

    public MessagePoller(GGMessagingAPI messagingAPI,
                         IPolledMessagesProcessor polledMessagesProcessor,
                         PreferenceUtil preferenceUtil) {
        mMessagingApi = messagingAPI;
        mProcessor = polledMessagesProcessor;
        mPreferenceUtil = preferenceUtil;
    }

    @Override
    public void poll() {
        //get latest synchronized date from shared prefs
        long synchronizedDateMs = mPreferenceUtil.getLong(
                R.string.pref_latest_received_message_date_in_ms, 0);

        long newSynchronizationDate = poll(synchronizedDateMs);

        if (newSynchronizationDate > synchronizedDateMs) {
            LOGGER.d("Updating latest synchronization date (ms) to : " + newSynchronizationDate);

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
    private long poll(long synchronizedDateMs) {
        LOGGER.d("Polling for new messages with synchronization date (ms): " + synchronizedDateMs);

        Collection<Message> messages = getMessagesSynchronously(synchronizedDateMs);

        if (messages == null || messages.size() == 0) {
            LOGGER.d("No new messages available");
            return synchronizedDateMs;
        }

        mProcessor.process(messages);

        Message maximumMessageDateMessage = getMaximumDateMessage(messages);
        long newSynchronizedDateMs = maximumMessageDateMessage.getCreatedAt().getTime();

        return newSynchronizedDateMs;
    }

    /**
     * Synchronously gets messages from server with date filter
     *
     * @param minDateFilter - the date (in ms) filter to be used
     * @return messages with date gte fromDateAsMs
     */
    private Collection<Message> getMessagesSynchronously(long minDateFilter) {
        //Construct remote API call
        Call<List<Message>> messagesCall;

        if (minDateFilter == 0) {
            messagesCall = mMessagingApi.getMessages();
        } else {
            messagesCall = mMessagingApi.getMessagesFromDate(minDateFilter);
        }

        List<Message> messages = null;
        try {
            //Synchronous execution of remote API call
            //Retries request (called "follow-up request") on timeout failures
            messages = messagesCall.execute().body();
        } catch (SocketTimeoutException e) {
            LOGGER.w("Socket Timeout reached  ");
        } catch (UnknownHostException e) {
            LOGGER.e(e.getMessage());
        } catch (Exception e) {
            //A ProtocolError is thrown when more than 20 follow-ups are made
            LOGGER.e("Error with message polling ", e);
        }

        return messages;
    }

    private Message getMaximumDateMessage(Collection<Message> messages) {
        Message m = Collections.max(messages, new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return lhs.getCreatedAt().compareTo(rhs.getCreatedAt());
            }
        });

        return m;
    }
}
