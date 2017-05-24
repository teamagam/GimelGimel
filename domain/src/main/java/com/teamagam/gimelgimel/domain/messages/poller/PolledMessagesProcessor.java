package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.alerts.ProcessIncomingAlertMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.layers.ProcessNewVectorLayerInteractorFactory;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.AddPolledMessageToRepositoryInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeoAlert;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeoImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageSensor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.MessageVectorLayer;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Process messages by putting them to the messages repository
 */
@Singleton
public class PolledMessagesProcessor implements IPolledMessagesProcessor {

  private static final Logger sLogger =
      LoggerFactory.create(PolledMessagesProcessor.class.getSimpleName());

  private PreferencesUtils mPreferencesUtils;
  private UsersLocationRepository mUsersLocationRepository;
  private SensorsRepository mSensorsRepository;
  private MessageProcessorVisitor mMessageProcessorVisitor;
  private GeoEntitiesRepository mGeoEntitiesRepository;
  private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private ObjectMessageMapper mEntityMessageMapper;
  private ObjectMessageMapper mAlertMessageMapper;
  private AddPolledMessageToRepositoryInteractorFactory
      mAddPolledMessageToRepositoryInteractorFactory;
  private ProcessIncomingAlertMessageInteractorFactory
      mProcessIncomingAlertMessageInteractorFactory;
  private ProcessNewVectorLayerInteractorFactory mProcessNewVectorLayerInteractorFactory;

  @Inject
  public PolledMessagesProcessor(PreferencesUtils preferencesUtils,
      UsersLocationRepository usersLocationRepository, SensorsRepository sensorsRepository,
      GeoEntitiesRepository geoEntitiesRepository,
      DisplayedEntitiesRepository displayedEntitiesRepository,
      @Named("Entity")
          ObjectMessageMapper entityMessageMapper,
      @Named("Alert")
          ObjectMessageMapper alertMessageMapper,
      AddPolledMessageToRepositoryInteractorFactory addPolledMessageToRepositoryInteractorFactory,
      ProcessNewVectorLayerInteractorFactory processNewVectorLayerInteractorFactory,
      ProcessIncomingAlertMessageInteractorFactory processIncomingAlertMessageInteractorFactory) {
    mPreferencesUtils = preferencesUtils;
    mUsersLocationRepository = usersLocationRepository;
    mSensorsRepository = sensorsRepository;
    mGeoEntitiesRepository = geoEntitiesRepository;
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mEntityMessageMapper = entityMessageMapper;
    mAlertMessageMapper = alertMessageMapper;
    mAddPolledMessageToRepositoryInteractorFactory = addPolledMessageToRepositoryInteractorFactory;
    mProcessNewVectorLayerInteractorFactory = processNewVectorLayerInteractorFactory;
    mProcessIncomingAlertMessageInteractorFactory = processIncomingAlertMessageInteractorFactory;
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
      mapEntityToMessage(message, message.getGeoEntity());
      displayGeoEntity(message.getGeoEntity());
    }

    @Override
    public void visit(MessageText message) {
      addToMessagesRepository(message);
    }

    @Override
    public void visit(MessageImage message) {
      addToMessagesRepository(message);
      if (message instanceof MessageGeoImage) {
        MessageGeoImage messageGeoImage = (MessageGeoImage) message;
        mapEntityToMessage(messageGeoImage, messageGeoImage.getGeoEntity());
        displayGeoEntity(messageGeoImage.getGeoEntity());
      }
    }

    @Override
    public void visit(MessageUserLocation message) {
      if (!mPreferencesUtils.isMessageFromSelf(message)) {
        mUsersLocationRepository.add(message.getSenderId(), message.getLocationSample());
      }
    }

    @Override
    public void visit(MessageSensor message) {
      mSensorsRepository.addSensor(message.getSensorMetadata());
      mapEntityToMessage(message, message.getGeoEntity());
    }

    @Override
    public void visit(MessageAlert messageAlert) {
      mProcessIncomingAlertMessageInteractorFactory.create(messageAlert).execute();
      mapAlertToMessage(messageAlert, messageAlert.getAlert());

      if (messageAlert instanceof MessageGeoAlert) {
        MessageGeoAlert geoAlert = (MessageGeoAlert) messageAlert;
        mapEntityToMessage(geoAlert, geoAlert.getGeoEntity());
      }
    }

    @Override
    public void visit(MessageVectorLayer message) {
      mProcessNewVectorLayerInteractorFactory.create(message.getVectorLayer(), message.getUrl())
          .execute();
    }

    private void addToMessagesRepository(Message message) {
      mAddPolledMessageToRepositoryInteractorFactory.create(message).execute();
    }

    private void mapEntityToMessage(Message message, GeoEntity geoEntity) {
      String messageId = message.getMessageId();
      String geoEntityId = geoEntity.getId();

      mEntityMessageMapper.addMapping(messageId, geoEntityId);
    }

    private void mapAlertToMessage(Message message, Alert alert) {
      String messageId = message.getMessageId();
      String alertId = alert.getId();

      mAlertMessageMapper.addMapping(messageId, alertId);
    }

    private void displayGeoEntity(GeoEntity geoEntity) {
      mGeoEntitiesRepository.add(geoEntity);
      mDisplayedEntitiesRepository.show(geoEntity);
    }
  }
}