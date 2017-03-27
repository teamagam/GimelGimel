package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.repository.NewMessageIndicationRepository;

import java.util.Collections;
import java.util.Date;

import rx.Observable;

@AutoFactory
public class UpdateNewMessageIndicationDate extends BaseDataInteractor {

    private final NewMessageIndicationRepository mNewMessageIndicationRepository;
    private final Date mNewIndicationDate;

    public UpdateNewMessageIndicationDate(
            @Provided ThreadExecutor threadExecutor,
            @Provided NewMessageIndicationRepository newMessageIndicationRepository,
            Date newIndicationDate) {
        super(threadExecutor);
        mNewMessageIndicationRepository = newMessageIndicationRepository;
        mNewIndicationDate = newIndicationDate;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest<?> request = factory.create(
                Observable.just(mNewIndicationDate),
                dateObservable ->
                        dateObservable
                                .filter(date -> date.after(mNewMessageIndicationRepository.get()))
                                .doOnNext(mNewMessageIndicationRepository::set));

        return Collections.singletonList(request);
    }
}