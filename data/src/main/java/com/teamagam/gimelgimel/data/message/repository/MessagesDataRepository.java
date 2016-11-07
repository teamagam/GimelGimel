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

    private int mNumUnreadMessages;

    private PublishSubject<Message> mMessagesSubject;
    private PublishSubject<Message> mSelectedSubject;
    private PublishSubject<Integer> mNumUnreadSubject;

    private Observable<Message> mSharedObservable;
    private Observable<Message> mSelectedObservable;
    private Observable<Integer> mNumReadObservable;

    @Inject
    public MessagesDataRepository() {
        mMessagesSubject = PublishSubject.create();
        mSharedObservable = mMessagesSubject.share();

        mSelectedSubject = PublishSubject.create();
        mSelectedObservable = mSelectedSubject.replay(1).autoConnect();

        mNumUnreadSubject = PublishSubject.create();
        mNumReadObservable = mNumUnreadSubject.replay(1).autoConnect();;
    }

    @Override
    public Observable<Message> getMessages() {
        return mCache.getMessages()
                .flatMapIterable(messages -> messages);
//                .map(mMessageDataMapper::transform);
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
        updateNumUnreadMessages(1);
    }

    @Override
    public Observable<Message> sendMessage(Message message) {
        return mSource.sendMessage(mMessageDataMapper.transformToData(message))
                .map(mMessageDataMapper::transform);
    }

    @Override
    public Observable<Message> getMessageById(String messageId) {
        return Observable.just(messageId)
                .map(mCache::getMessageById);
    }

    @Override
    public void selectMessage(Message message){
        mCache.selectMessage(message);
        mSelectedSubject.onNext(message);
    }

    @Override
    public void markMessageRead(Message message){
        mCache.markMessageRead(message);
        updateNumUnreadMessages(-1);
    }

    @Override
    public Message getSelectedMessage() {
        return mCache.getSelectedMessage();
    }

    private void updateNumUnreadMessages(int num) {
        mNumUnreadMessages += num;
        mNumUnreadSubject.onNext(mNumUnreadMessages);
    }
}
