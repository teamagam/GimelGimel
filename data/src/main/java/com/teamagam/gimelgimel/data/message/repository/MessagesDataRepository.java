package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.repository.InMemory.InMemoryMessagesCache;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created on 8/10/2016.
 * TODO: complete text
 */
@Singleton
public class MessagesDataRepository implements MessagesRepository {

    @Inject
    MessageDataMapper mMessageDataMapper;

    @Inject
    CloudMessagesSource mSource;

    @Inject
    InMemoryMessagesCache mCache;

    private int mNumReadMessages;

    private PublishSubject<Message> mMessagesSubject;
    private PublishSubject<Message> mSelectedSubject;
    private PublishSubject<Integer> mNumReadSubject;

    private Observable<Message> mSharedObservable;
    private Observable<Message> mSelectedObservable;
    private Observable<Integer> mNumReadObservable;

    @Inject
    public MessagesDataRepository() {
        mMessagesSubject = PublishSubject.create();
        mSharedObservable = mMessagesSubject.share();

        mSelectedSubject = PublishSubject.create();
        mSelectedObservable = mSelectedSubject.replay(1).share();

        mNumReadSubject = PublishSubject.create();
        mNumReadObservable = mNumReadSubject.replay(1).share();
    }

    @Override
    public Observable<Message> getMessages() {
        return mSource.getMessages()
                .flatMapIterable(messages -> messages)
                .map(mMessageDataMapper::transform);
    }

    @Override
    public Observable<Message> getSyncMessagesObservable() {
        return mSharedObservable;
    }

    @Override
    public Observable<Message> getSyncSelectedMessageObservable() {
        return mSelectedObservable;
    }

    @Override
    public Observable<Integer> getSyncNumReadObservable() {
        return mNumReadObservable;
    }

    @Override
    public void putMessage(Message message) {
        mCache.addMessage(message);
        mMessagesSubject.onNext(message);
    }

    @Override
    public Observable<Message> sendMessage(Message message) {
        return mSource.sendMessage(mMessageDataMapper.transformToData(message))
                .map(mMessageDataMapper::transform);
    }

    @Override
    public void selectMessage(Message message){
        Observable.just(message)
                .doOnNext(mCache::selectMessage)
                .doOnNext(mSelectedSubject::onNext)
                .doOnNext((m) -> updateNumReadMessage())
                .subscribe();
    }

    private void updateNumReadMessage(){
        mNumReadSubject.onNext(mNumReadMessages);
    }

}
