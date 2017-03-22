package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;

import java.util.Arrays;

import rx.Observable;

@AutoFactory
public class SelectMessageByEntityInteractor extends BaseDataInteractor {

    private final EntityMessageMapper mEntityMessageMapper;
    private final SelectedEntityRepository mSelectedEntityRepository;
    private final SelectEntityInteractorFactory mSelectEntityInteractorFactory;
    private final SelectMessageInteractorFactory mSelectMessageInteractorFactory;
    private final String mEntityId;

    public SelectMessageByEntityInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided EntityMessageMapper entityMessageMapper,
            @Provided SelectedEntityRepository selectedEntityRepository,
            @Provided SelectEntityInteractorFactory selectEntityInteractorFactory,
            @Provided com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory selectMessageInteractorFactory,
            String entityId) {
        super(threadExecutor);
        mEntityMessageMapper = entityMessageMapper;
        mSelectedEntityRepository = selectedEntityRepository;
        mSelectEntityInteractorFactory = selectEntityInteractorFactory;
        mSelectMessageInteractorFactory = selectMessageInteractorFactory;
        mEntityId = entityId;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return Arrays.asList(
                buildSelectEntityRequest(factory),
                buildSelectMessageRequest(factory)
        );
    }

    private DataSubscriptionRequest buildSelectMessageRequest(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return factory.create(
                Observable.just(mEntityId),
                entityIdObservable ->
                        entityIdObservable
                                .filter(e -> !isReselection(e))
                                .map(mEntityMessageMapper::getMessageId)
                                .filter(m -> m != null)
                                .doOnNext(m -> mSelectMessageInteractorFactory.create(m).execute())
        );
    }

    private DataSubscriptionRequest buildSelectEntityRequest(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return factory.create(
                Observable.just(mEntityId),
                entityIdObservable ->
                        entityIdObservable
                                .doOnNext(e -> mSelectEntityInteractorFactory.create(e).execute())

        );
    }

    private boolean isReselection(String geoEntityId) {
        return geoEntityId.equals(mSelectedEntityRepository.getSelectedEntityId());
    }
}
