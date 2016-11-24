package com.teamagam.gimelgimel.domain.messages.poller;


import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
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
    private UsersLocationRepository mUsersLocationRepository;

    @Inject
    public PolledMessagesProcessor(MessagesRepository messagesRepository,
                                   UserPreferencesRepository prefs, UsersLocationRepository
                                           usersLocationRepository, DisplayedEntitiesRepository displayedEntitiesRepository) {
        mMessagesRepository = messagesRepository;
        mPrefs = prefs;
        mUsersLocationRepository = usersLocationRepository;
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

        // Do not broadcast messages from self
        if (message.getSenderId().equals(mPrefs.getPreference(Constants.USERNAME_PREFRENCE_KEY))) {
            return;
        }

        if (isUserLocationMessage(message)) {
            mUsersLocationRepository.add(message.getSenderId(),
                    ((MessageUserLocation) message).getLocationSample());
        } else {
            mMessagesRepository.putMessage(message);
        }
    }

    private boolean isUserLocationMessage(Message message) {
        return message instanceof MessageUserLocation;
    }
}
