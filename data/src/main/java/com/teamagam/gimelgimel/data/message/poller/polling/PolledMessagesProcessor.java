package com.teamagam.gimelgimel.data.message.poller.polling;


import com.teamagam.gimelgimel.data.user.repository.PreferencesProvider;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import java.util.Collection;

import javax.inject.Inject;

/**
 * Broadcast messages with injected {@link IMessageBroadcaster}
 */
public class PolledMessagesProcessor implements IPolledMessagesProcessor {

//    private static final Logger sLogger = LoggerFactory.create(
//            PolledMessagesProcessor.class);

    private MessagesRepository mMessagesRepository;
    private PreferencesProvider mPrefs;

    @Inject
    public PolledMessagesProcessor(MessagesRepository messagesRepository,
                                   PreferencesProvider prefs) {
        mMessagesRepository = messagesRepository;
        mPrefs = prefs;
    }

    @Override
    public void process(Collection<Message> polledMessages) {
        if (polledMessages == null) {
            throw new IllegalArgumentException("polledMessages cannot be null");
        }

//        sLogger.d("MessagePolling service processing " + polledMessages.size() + " new messages");

        for (Message msg : polledMessages) {
            processMessage(msg);
        }
    }

    private void processMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        //Do not broadcast messages from self
        if (message.getSenderId().equals(mPrefs.getSenderId())) {
            return;
        }

//        sLogger.v("Broadcasting message with ID: " + message.getMessageId());

        mMessagesRepository.putMessage(message);
    }

    private boolean isUserLocationMessage(Message message) {
        return message instanceof MessageUserLocation;
    }
}
