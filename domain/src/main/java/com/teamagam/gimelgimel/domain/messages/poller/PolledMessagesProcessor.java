package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.layers.ProcessNewVectorLayerInteractorFactory;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.AddPolledMessageToRepositoryInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
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
  private GeoEntitiesRepository mGeoEntitiesRepository;
  private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private ObjectMessageMapper mEntityMessageMapper;
  private ObjectMessageMapper mAlertMessageMapper;
  private AddPolledMessageToRepositoryInteractorFactory
      mAddPolledMessageToRepositoryInteractorFactory;
  private ProcessNewVectorLayerInteractorFactory mProcessNewVectorLayerInteractorFactory;

  @Inject
  public PolledMessagesProcessor(PreferencesUtils preferencesUtils,
      UsersLocationRepository usersLocationRepository,
      GeoEntitiesRepository geoEntitiesRepository,
      DisplayedEntitiesRepository displayedEntitiesRepository,
      @Named("Entity") ObjectMessageMapper entityMessageMapper,
      @Named("Alert") ObjectMessageMapper alertMessageMapper,
      AddPolledMessageToRepositoryInteractorFactory addPolledMessageToRepositoryInteractorFactory,
      ProcessNewVectorLayerInteractorFactory processNewVectorLayerInteractorFactory) {
    mPreferencesUtils = preferencesUtils;
    mUsersLocationRepository = usersLocationRepository;
    mGeoEntitiesRepository = geoEntitiesRepository;
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mEntityMessageMapper = entityMessageMapper;
    mAlertMessageMapper = alertMessageMapper;
    mAddPolledMessageToRepositoryInteractorFactory = addPolledMessageToRepositoryInteractorFactory;
    mProcessNewVectorLayerInteractorFactory = processNewVectorLayerInteractorFactory;
  }

  @Override
  public void process(Collection<ChatMessage> polledMessages) {
    if (polledMessages == null) {
      throw new IllegalArgumentException("polledMessages cannot be null");
    }

    sLogger.d("MessagePolling service processing " + polledMessages.size() + " new messages");

    for (ChatMessage msg : polledMessages) {
      processMessage(msg);
    }
  }

  private void processMessage(ChatMessage message) {
    if (message == null) {
      throw new IllegalArgumentException("Message cannot be null");
    }

    MessageProcessorVisitor visitor = new MessageProcessorVisitor(message);
    message.accept(visitor);
  }

  private class MessageProcessorVisitor implements IMessageFeatureVisitor {

    private ChatMessage mMessage;

    public MessageProcessorVisitor(ChatMessage message) {
      mMessage = message;

      addToMessagesRepository(mMessage);
    }

    @Override
    public void visit(TextFeature feature) {
    }

    @Override
    public void visit(GeoFeature feature) {
      mapEntityToMessage(mMessage, feature.getGeoEntity());
      displayGeoEntity(feature.getGeoEntity());
    }

    @Override
    public void visit(ImageFeature feature) {

    }

    @Override
    public void visit(AlertFeature feature) {
      mapAlertToMessage(mMessage, feature);
    }

    // TODO User Location
    /*@Override
    public void visit(MessageUserLocation message) {
      if (!mPreferencesUtils.isMessageFromSelf(message)) {
        mUsersLocationRepository.add(message.getSenderId(), message.getLocationSample());
      }
    }

    // TODO: Vector layer
    @Override
    public void visit(MessageVectorLayer message) {
      mProcessNewVectorLayerInteractorFactory.create(message.getVectorLayer(), message.getUrl())
          .execute();
    }*/

    private void addToMessagesRepository(ChatMessage message) {
      mAddPolledMessageToRepositoryInteractorFactory.create(message).execute();
    }

    private void mapEntityToMessage(ChatMessage message, GeoEntity geoEntity) {
      String messageId = message.getMessageId();
      String geoEntityId = geoEntity.getId();

      mEntityMessageMapper.addMapping(messageId, geoEntityId);
    }

    private void mapAlertToMessage(ChatMessage message, AlertFeature alert) {
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