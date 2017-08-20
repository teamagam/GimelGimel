package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.NewMessageIndicationRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import java.util.Arrays;
import java.util.Date;
import javax.inject.Named;

@AutoFactory
public class DisplayMessagesInteractor extends BaseDisplayInteractor {

  private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private final ObjectMessageMapper mGeoEntityToMessageMapper;
  private final NewMessageIndicationRepository mNewMessageIndicationRepository;
  private final Displayer mDisplayer;
  private final MessagesRepository mMessagesRepository;
  private final PreferencesUtils mPreferencesUtils;

  DisplayMessagesInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided @Named("Entity") ObjectMessageMapper geoEntityToMessageMapper,
      @Provided MessagesRepository messagesRepository,
      @Provided PreferencesUtils preferencesUtils,
      @Provided DisplayedEntitiesRepository displayedEntitiesRepository,
      @Provided NewMessageIndicationRepository newMessageIndicationRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mGeoEntityToMessageMapper = geoEntityToMessageMapper;
    mMessagesRepository = messagesRepository;
    mPreferencesUtils = preferencesUtils;
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mNewMessageIndicationRepository = newMessageIndicationRepository;
    mDisplayer = displayer;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    DisplaySubscriptionRequest displayMessages =
        factory.create(mMessagesRepository.getMessagesObservable(),
            (messageObservable) -> messageObservable.map(this::createMessagePresentation),
            mDisplayer::show);

    DisplaySubscriptionRequest syncDisplayedGeoEntities =
        factory.create(mDisplayedEntitiesRepository.getObservable(),
            genObservable -> genObservable.map(this::getMessageId)
                .map(mMessagesRepository::getMessage)
                .map(this::createMessagePresentation), mDisplayer::show);

    return Arrays.asList(displayMessages, syncDisplayedGeoEntities);
  }

  private MessagePresentation createMessagePresentation(ChatMessage message) {
    boolean isShownOnMap = isShownOnMap(message);
    boolean isFromSelf = mPreferencesUtils.isSelf(message.getSenderId());
    boolean isNotified = isBefore(message.getCreatedAt(), mNewMessageIndicationRepository.get());
    boolean isSelected = isSelected(message);

    return new MessagePresentation.Builder(message).setIsFromSelf(isFromSelf)
        .setIsShownOnMap(isShownOnMap)
        .setIsNotified(isNotified)
        .setIsSelected(isSelected)
        .build();
  }

  private boolean isShownOnMap(ChatMessage message) {
    if (!message.contains(GeoFeature.class)) {
      return false;
    }

    GeoFeature geoFeature = message.getFeatureByType(GeoFeature.class);
    return mDisplayedEntitiesRepository.isShown(geoFeature.getGeoEntity());
  }

  private boolean isBefore(Date date, Date other) {
    return date.compareTo(other) <= 0;
  }

  private boolean isSelected(ChatMessage message) {
    ChatMessage selectedMessage = mMessagesRepository.getSelectedMessage();

    return selectedMessage != null && message.getMessageId().equals(selectedMessage.getMessageId());
  }

  private String getMessageId(GeoEntityNotification gen) {
    return mGeoEntityToMessageMapper.getMessageId(gen.getGeoEntity().getId());
  }

  public interface Displayer {
    void show(MessagePresentation message);
  }
}
