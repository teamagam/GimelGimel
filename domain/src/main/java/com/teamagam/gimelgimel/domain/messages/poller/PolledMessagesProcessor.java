package com.teamagam.gimelgimel.domain.messages.poller;


import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageSensor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Process messages by putting them to the messages repository
 */
@Singleton
public class PolledMessagesProcessor implements IPolledMessagesProcessor {

    private static final Logger sLogger = LoggerFactory.create(
            PolledMessagesProcessor.class.getSimpleName());

    private MessagesRepository mMessagesRepository;
    private UserPreferencesRepository mPrefs;
    private UsersLocationRepository mUsersLocationRepository;
    private SensorsRepository mSensorsRepository;
    private MessageProcessorVisitor mMessageProcessorVisitor;
    private DisplayedEntitiesRepository mDisplayedEntitiesRepository;

    @Inject
    public PolledMessagesProcessor(
            MessagesRepository messagesRepository,
            UserPreferencesRepository prefs,
            UsersLocationRepository usersLocationRepository,
            SensorsRepository sensorsRepository,
            DisplayedEntitiesRepository displayedEntitiesRepository) {
        mMessagesRepository = messagesRepository;
        mPrefs = prefs;
        mUsersLocationRepository = usersLocationRepository;
        mSensorsRepository = sensorsRepository;
        mDisplayedEntitiesRepository = displayedEntitiesRepository;
        mMessageProcessorVisitor = new MessageProcessorVisitor();
    }

    @Override
    public void process(Collection<Message> polledMessages) {
        if (polledMessages == null) {
            throw new IllegalArgumentException("polledMessages cannot be null");
        }

        sLogger.d("MessagePolling service processing " + polledMessages.size() + " new messages");

        for (Message msg : polledMessages) {
            processMessage(msg);
        }
    }

    private void processMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        message.accept(mMessageProcessorVisitor);
    }

    private class MessageProcessorVisitor implements IMessageVisitor {

        @Override
        public void visit(MessageGeo message) {
            addToMessagesRepository(message);
            displayGeoEntity(message.getGeoEntity());
        }

        @Override
        public void visit(MessageText message) {
            addToMessagesRepository(message);
        }

        @Override
        public void visit(MessageImage message) {
            addToMessagesRepository(message);
            displayGeoEntity(message.getImageMetadata().getGeoEntity());
        }

        @Override
        public void visit(MessageUserLocation message) {
            if (!isFromSelf(message)) {
                mUsersLocationRepository.add(message.getSenderId(), message.getLocationSample());
            }
        }

        @Override
        public void visit(MessageSensor message) {
            mSensorsRepository.addSensor(message.getSensorMetadata());
        }

        private void addToMessagesRepository(Message message) {
            mMessagesRepository.putMessage(message);
        }

        private void displayGeoEntity(GeoEntity geoEntity) {
            mDisplayedEntitiesRepository.show(geoEntity);
        }

        private boolean isFromSelf(Message message) {
            return message.getSenderId().equals(
                    mPrefs.getPreference(Constants.USERNAME_PREFRENCE_KEY));
        }
    }
}
