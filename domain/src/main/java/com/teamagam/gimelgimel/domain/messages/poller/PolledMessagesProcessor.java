package com.teamagam.gimelgimel.domain.messages.poller;


import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Process messages by putting them to the messages repository
 */
@Singleton
public class PolledMessagesProcessor implements IPolledMessagesProcessor {

//    private static final Logger sLogger = LoggerFactory.create(
//            PolledMessagesProcessor.class);

    private MessagesRepository mMessagesRepository;
    private UserPreferencesRepository mPrefs;

    @Inject
    public PolledMessagesProcessor(MessagesRepository messagesRepository,
                                   UserPreferencesRepository prefs) {
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
