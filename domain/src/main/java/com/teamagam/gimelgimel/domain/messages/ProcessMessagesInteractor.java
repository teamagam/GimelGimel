package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.alerts.AddAlertToRepositoryInteractorFactory;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Named;

public class ProcessMessagesInteractor extends BaseDataInteractor {

  private MessagesRepository mMessagesRepository;
  private GeoEntitiesRepository mGeoEntitiesRepository;
  private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private ObjectMessageMapper mEntityMessageMapper;
  private ObjectMessageMapper mAlertMessageMapper;
  private AddAlertToRepositoryInteractorFactory mAddAlertToRepositoryInteractorFactory;

  @Inject
  public ProcessMessagesInteractor(ThreadExecutor threadExecutor,
      MessagesRepository messagesRepository,
      GeoEntitiesRepository geoEntitiesRepository,
      DisplayedEntitiesRepository displayedEntitiesRepository,
      @Named("Entity") ObjectMessageMapper entityMessageMapper,
      @Named("Alert") ObjectMessageMapper alertMessageMapper,
      AddAlertToRepositoryInteractorFactory addAlertToRepositoryInteractorFactory) {
    super(threadExecutor);
    mMessagesRepository = messagesRepository;
    mGeoEntitiesRepository = geoEntitiesRepository;
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mEntityMessageMapper = entityMessageMapper;
    mAlertMessageMapper = alertMessageMapper;
    mAddAlertToRepositoryInteractorFactory = addAlertToRepositoryInteractorFactory;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest dataSubscriptionRequest =
        factory.create(mMessagesRepository.getMessagesObservable(),
            messagesObservable -> messagesObservable.doOnNext(this::process));

    return Collections.singletonList(dataSubscriptionRequest);
  }

  private void process(ChatMessage message) {
    MessageProcessorVisitor visitor = new MessageProcessorVisitor(message);
    message.accept(visitor);
  }

  private class MessageProcessorVisitor implements IMessageFeatureVisitor {

    private ChatMessage mMessage;

    public MessageProcessorVisitor(ChatMessage message) {
      mMessage = message;
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
      mAddAlertToRepositoryInteractorFactory.create(feature.getAlert());
    }

    private void mapEntityToMessage(ChatMessage message, GeoEntity geoEntity) {
      String messageId = message.getMessageId();
      String geoEntityId = geoEntity.getId();

      mEntityMessageMapper.addMapping(messageId, geoEntityId);
    }

    private void displayGeoEntity(GeoEntity geoEntity) {
      mGeoEntitiesRepository.add(geoEntity);
      mDisplayedEntitiesRepository.show(geoEntity);
    }

    private void mapAlertToMessage(ChatMessage message, AlertFeature alertFeature) {
      String messageId = message.getMessageId();
      String alertId = alertFeature.getAlert().getId();

      mAlertMessageMapper.addMapping(messageId, alertId);
    }
  }
}
