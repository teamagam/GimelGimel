package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import java.util.Arrays;
import javax.inject.Named;
import rx.Observable;

@AutoFactory
public class SelectEntityByMessageInteractor extends BaseDataInteractor {

  private final ObjectMessageMapper mEntityMessageMapper;
  private final SelectEntityInteractorFactory mSelectEntityInteractorFactory;
  private final SelectMessageInteractorFactory mSelectMessageInteractorFactory;
  private final String mMessageId;

  public SelectEntityByMessageInteractor(
      @Provided
          ThreadExecutor threadExecutor,
      @Provided
      @Named("Entity")
          ObjectMessageMapper entityMessageMapper,
      @Provided
          com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory selectEntityInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory selectMessageInteractorFactory,
      String messageId) {
    super(threadExecutor);
    mEntityMessageMapper = entityMessageMapper;
    mSelectEntityInteractorFactory = selectEntityInteractorFactory;
    mSelectMessageInteractorFactory = selectMessageInteractorFactory;
    mMessageId = messageId;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
      DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Arrays.asList(buildSelectMessageRequest(factory), buildSelectEntityRequest(factory));
  }

  private DataSubscriptionRequest buildSelectMessageRequest(
      DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mMessageId),
        messageIdObservable -> messageIdObservable.doOnNext(
            m -> mSelectMessageInteractorFactory.create(m).execute()));
  }

  private DataSubscriptionRequest buildSelectEntityRequest(
      DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mMessageId),
        messageIdObservable -> messageIdObservable.map(mEntityMessageMapper::getObjectId)
            .filter(e -> e != null)
            .doOnNext(e -> mSelectEntityInteractorFactory.create(e).execute())

    );
  }
}
