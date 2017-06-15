package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import io.reactivex.Observable;
import java.util.Collections;
import javax.inject.Named;

@AutoFactory
public class SelectMessageByEntityInteractor extends BaseDataInteractor {

  private final ObjectMessageMapper mEntityMessageMapper;
  private final SelectedEntityRepository mSelectedEntityRepository;
  private final SelectEntityInteractorFactory mSelectEntityInteractorFactory;
  private final SelectMessageInteractorFactory mSelectMessageInteractorFactory;
  private final String mEntityId;

  public SelectMessageByEntityInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided @Named("Entity") ObjectMessageMapper entityMessageMapper,
      @Provided SelectedEntityRepository selectedEntityRepository,
      @Provided SelectEntityInteractorFactory selectEntityInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory selectMessageInteractorFactory,
      String entityId) {
    super(threadExecutor);
    mEntityMessageMapper = entityMessageMapper;
    mSelectedEntityRepository = selectedEntityRepository;
    mSelectEntityInteractorFactory = selectEntityInteractorFactory;
    mSelectMessageInteractorFactory = selectMessageInteractorFactory;
    mEntityId = entityId;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(buildSelectMessageRequest(factory));
  }

  private DataSubscriptionRequest buildSelectMessageRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mEntityId),
        entityIdObservable -> entityIdObservable.doOnNext(
            e -> mSelectEntityInteractorFactory.create(e).execute())
            .filter(e -> !isReselection(e))
            .map(mEntityMessageMapper::getMessageId)
            .filter(m -> m != null)
            .doOnNext(m -> mSelectMessageInteractorFactory.create(m).execute()));
  }

  private boolean isReselection(String geoEntityId) {
    return geoEntityId.equals(mSelectedEntityRepository.getSelectedEntityId());
  }
}
