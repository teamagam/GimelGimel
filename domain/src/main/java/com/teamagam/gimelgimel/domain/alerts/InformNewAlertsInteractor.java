package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import java.util.Date;

@AutoFactory
public class InformNewAlertsInteractor extends BaseSingleDisplayInteractor {

  private final MessagesRepository mMessagesRepository;
  private final InformedAlertsRepository mInformedAlertsRepository;
  private final Displayer mDisplayer;

  InformNewAlertsInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided MessagesRepository messagesRepository,
      @Provided InformedAlertsRepository informedAlertsRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mMessagesRepository = messagesRepository;
    mInformedAlertsRepository = informedAlertsRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

    return factory.create(mMessagesRepository.getMessagesObservable(),
        alertObservable -> alertObservable.filter(this::shouldInform), mDisplayer::display);
  }

  private boolean shouldInform(AlertFeature alert) {
    return isAfterLatestInformedDate(alert);
  }

  private boolean isAfterLatestInformedDate(AlertFeature alert) {
    Date latestInformedDate = mInformedAlertsRepository.getLatestInformedDate();
    Date alertDate = new Date(alert.getTime());

    return alertDate.after(latestInformedDate);
  }

  public interface Displayer {
    void display(ChatMessage alert);
  }
}
