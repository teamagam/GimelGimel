package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.entity.AlertPresentation;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import java.util.Date;

@AutoFactory
public class InformNewAlertsInteractor extends BaseSingleDisplayInteractor {

  private final AlertsRepository mAlertsRepository;
  private final InformedAlertsRepository mInformedAlertsRepository;
  private final Displayer mDisplayer;
  private ObjectMessageMapper mObjectMessageMapper;
  private MessagesRepository mMessagesRepository;

  InformNewAlertsInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided AlertsRepository alertsRepository,
      @Provided InformedAlertsRepository informedAlertsRepository,
      Displayer displayer,
      ObjectMessageMapper objectMessageMapper,
      MessagesRepository messagesRepository) {
    super(threadExecutor, postExecutionThread);
    mAlertsRepository = alertsRepository;
    mInformedAlertsRepository = informedAlertsRepository;
    mDisplayer = displayer;
    mObjectMessageMapper = objectMessageMapper;
    mMessagesRepository = messagesRepository;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

    return factory.create(mAlertsRepository.getAlertsObservable(),
        alertObservable -> alertObservable.filter(this::shouldInform)
            .map(alert -> new AlertPresentation(alert, getTextFromAlert(alert))),
        mDisplayer::display);
  }

  private String getTextFromAlert(Alert alert) {
    String messageId = mObjectMessageMapper.getMessageId(alert.getId());
    ChatMessage chatMessage = mMessagesRepository.getMessage(messageId);
    return chatMessage.getFeatureByType(TextFeature.class).getText();
  }

  private boolean shouldInform(Alert alert) {
    return isAfterLatestInformedDate(alert);
  }

  private boolean isAfterLatestInformedDate(Alert alert) {
    Date latestInformedDate = mInformedAlertsRepository.getLatestInformedDate();
    Date alertDate = new Date(alert.getTime());

    return alertDate.after(latestInformedDate);
  }

  public interface Displayer {
    void display(AlertPresentation alertPresentation);
  }
}
