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
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.Date;

@AutoFactory
public class DisplayMessagesInteractor extends BaseDisplayInteractor {

  private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private final NewMessageIndicationRepository mNewMessageIndicationRepository;
  private final Displayer mDisplayer;
  private final MessagesRepository mMessagesRepository;
  private final PreferencesUtils mPreferencesUtils;

  DisplayMessagesInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided MessagesRepository messagesRepository,
      @Provided PreferencesUtils preferencesUtils,
      @Provided DisplayedEntitiesRepository displayedEntitiesRepository,
      @Provided NewMessageIndicationRepository newMessageIndicationRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mMessagesRepository = messagesRepository;
    mPreferencesUtils = preferencesUtils;
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mNewMessageIndicationRepository = newMessageIndicationRepository;
    mDisplayer = displayer;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

    DisplaySubscriptionRequest displayMessages =
        factory.create(mMessagesRepository.getMessagesObservable(), this::transformToPresentation,
            mDisplayer::show);

    return Collections.singletonList(displayMessages);
  }

  private Observable<MessagePresentation> transformToPresentation(Observable<ChatMessage> messageObservable) {
    return messageObservable.map(this::createMessagePresentation);
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

  public interface Displayer {
    void show(MessagePresentation message);
  }
}
