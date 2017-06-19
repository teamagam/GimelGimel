package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.alerts.AddAlertToRepositoryInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.AddMessageToRepositoryInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PolledMessagesProcessor implements IPolledMessagesProcessor {

  private PreferencesUtils mPreferencesUtils;
  private VectorLayersRepository mVectorLayersRepository;
  private UsersLocationRepository mUsersLocationRepository;
  private GeoEntitiesRepository mGeoEntitiesRepository;
  private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private ObjectMessageMapper mEntityMessageMapper;
  private ObjectMessageMapper mAlertMessageMapper;
  private AddMessageToRepositoryInteractorFactory mAddMessageToRepositoryInteractorFactory;
  private AddAlertToRepositoryInteractorFactory mAddAlertRepositoryInteractorFactory;

  @Inject
  public PolledMessagesProcessor(PreferencesUtils preferencesUtils,
      VectorLayersRepository vectorLayersRepository,
      UsersLocationRepository usersLocationRepository,
      GeoEntitiesRepository geoEntitiesRepository,
      DisplayedEntitiesRepository displayedEntitiesRepository,
      @Named("Entity") ObjectMessageMapper entityMessageMapper,
      @Named("Alert") ObjectMessageMapper alertMessageMapper,
      AddMessageToRepositoryInteractorFactory addMessageToRepositoryInteractorFactory,
      AddAlertToRepositoryInteractorFactory addAlertRepositoryInteractorFactory) {
    mPreferencesUtils = preferencesUtils;
    mVectorLayersRepository = vectorLayersRepository;
    mUsersLocationRepository = usersLocationRepository;
    mGeoEntitiesRepository = geoEntitiesRepository;
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mEntityMessageMapper = entityMessageMapper;
    mAlertMessageMapper = alertMessageMapper;
    mAddMessageToRepositoryInteractorFactory = addMessageToRepositoryInteractorFactory;
    mAddAlertRepositoryInteractorFactory = addAlertRepositoryInteractorFactory;
  }

  @Override
  public void process(ChatMessage message) {
    if (message == null) {
      throw new IllegalArgumentException("polledMessages cannot be null");
    }

    MessageProcessorVisitor visitor = new MessageProcessorVisitor(message);
    message.accept(visitor);
  }

  @Override
  public void process(VectorLayer vectorLayer) {
    if (!mVectorLayersRepository.isOutdatedVectorLayer(vectorLayer)) {
      mVectorLayersRepository.put(vectorLayer);
    }
  }

  @Override
  public void process(UserLocation userLocation) {
    if (!mPreferencesUtils.isSelf(userLocation.getUser())) {
      mUsersLocationRepository.add(userLocation);
    }
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

      mAddAlertRepositoryInteractorFactory.create(feature.getAlert());
    }

    private void addToMessagesRepository(ChatMessage message) {
      mAddMessageToRepositoryInteractorFactory.create(message).execute();
    }

    private void mapEntityToMessage(ChatMessage message, GeoEntity geoEntity) {
      String messageId = message.getMessageId();
      String geoEntityId = geoEntity.getId();

      mEntityMessageMapper.addMapping(messageId, geoEntityId);
    }

    private void mapAlertToMessage(ChatMessage message, AlertFeature alertFeature) {
      String messageId = message.getMessageId();
      String alertId = alertFeature.getAlert().getId();

      mAlertMessageMapper.addMapping(messageId, alertId);
    }

    private void displayGeoEntity(GeoEntity geoEntity) {
      mGeoEntitiesRepository.add(geoEntity);
      mDisplayedEntitiesRepository.show(geoEntity);
    }
  }
}