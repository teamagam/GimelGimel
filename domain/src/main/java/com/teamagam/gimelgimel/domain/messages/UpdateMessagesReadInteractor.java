package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ConfirmMessageRead;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.Date;

@AutoFactory
public class UpdateMessagesReadInteractor extends BaseDataInteractor {

  private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;
  private final MessagesRepository mMessagesRepository;
  private final Date mDate;
  private final ConfirmMessageRead mConfirm;

  public UpdateMessagesReadInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided UnreadMessagesCountRepository unreadMessagesCountRepository,
      @Provided MessagesRepository messagesRepository,
      Date date,
      String senderId,
      String messageId) {
    super(threadExecutor);
    mUnreadMessagesCountRepository = unreadMessagesCountRepository;
    mMessagesRepository = messagesRepository;
    mDate = date;
    mConfirm = new ConfirmMessageRead(senderId, messageId);
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest readAllMessages = factory.create(Observable.just(mDate),
        dateObservable -> dateObservable.filter(this::isNewerThanLastVisit)
            .doOnNext(mUnreadMessagesCountRepository::updateLastVisit)
            .doOnNext(x -> mMessagesRepository.informReadMessage(mConfirm)));

    return Collections.singletonList(readAllMessages);
  }

  private boolean isNewerThanLastVisit(Date date) {
    return date.after(mUnreadMessagesCountRepository.getLastVisitTimestamp());
  }
}
