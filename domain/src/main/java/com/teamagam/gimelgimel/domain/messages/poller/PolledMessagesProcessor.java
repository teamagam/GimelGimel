package com.teamagam.gimelgimel.domain.messages.poller;


import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;
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

//    private static final Logger sLogger = LoggerFactory.create(
//            PolledMessagesProcessor.class);

    private MessagesRepository mMessagesRepository;
    private UserPreferencesRepository mPrefs;
    private UsersLocationRepository mUsersLocationRepository;
    private SensorsRepository mSensorsRepository;
    private MessagesProcessorVisitor mMessagesProcessorVisitor;
    private AlertsRepository mAlertsRepository;

    @Inject
    public PolledMessagesProcessor(MessagesRepository messagesRepository,
                                   UserPreferencesRepository prefs,
                                   UsersLocationRepository usersLocationRepository,
                                   SensorsRepository sensorsRepository,
                                   AlertsRepository alertsRepository) {
        mMessagesRepository = messagesRepository;
        mPrefs = prefs;
        mUsersLocationRepository = usersLocationRepository;
        mSensorsRepository = sensorsRepository;
        mMessagesProcessorVisitor = new MessagesProcessorVisitor();
        mAlertsRepository = alertsRepository;
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

        message.accept(mMessagesProcessorVisitor);
    }

    private class MessagesProcessorVisitor implements IMessageVisitor {

        @Override
        public void visit(MessageGeo message) {
            addToMessagesRepository(message);
        }

        @Override
        public void visit(MessageText message) {
            addToMessagesRepository(message);
        }

        @Override
        public void visit(MessageImage message) {
            addToMessagesRepository(message);
        }

        @Override
        public void visit(MessageUserLocation message) {
            mUsersLocationRepository.add(message.getSenderId(), message.getLocationSample());
        }

        @Override
        public void visit(MessageSensor message) {
            mSensorsRepository.addSensor(message.getSensorMetadata());
        }

        @Override
        public void visit(MessageAlert messageAlert) {
            mAlertsRepository.addAlert(messageAlert.getAlert());
        }

        private void addToMessagesRepository(Message message) {
            mMessagesRepository.putMessage(message);
        }
    }
}
