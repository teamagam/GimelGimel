package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

@AutoFactory
public class DisplaySelectedMessageInteractor extends BaseSingleDisplayInteractor {

  private final Displayer mDisplayer;
  private MessagesRepository mMessagesRepository;

  public DisplaySelectedMessageInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided MessagesRepository messagesRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mMessagesRepository = messagesRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.createSimple(mMessagesRepository.getSelectedMessageObservable(),
        mDisplayer::display);
  }

  public interface Displayer {
    void display(ChatMessage message);
  }
}
