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
 */
@Singleton
public class MessagesDataRepository implements MessagesRepository {

    private final Observable<Integer> mNumUnreadMessagesObservable;
    @Inject
    MessageDataMapper mMessageDataMapper;

    private final CloudMessagesSource mSource;
    private final InMemoryMessagesCache mCache;
    private final SelectedMessageRepository mSelectedRepo;
    private final ReadMessagesRepository mReadRepo;

    private final PublishSubject<Integer> mNumUnreadMessagesSubject;

    private int mNumMessages;
    private int mNumReadMessage;

    @Inject
    public MessagesDataRepository(CloudMessagesSource cloudMessagesSource,
                                  InMemoryMessagesCache inMemoryMessagesCache,
                                  SelectedMessageRepository selectedMessageRepository,
                                  ReadMessagesRepository readMessagesRepository) {
        mSource = cloudMessagesSource;
        mCache = inMemoryMessagesCache;
        mSelectedRepo = selectedMessageRepository;
        mReadRepo = readMessagesRepository;

        mNumUnreadMessagesSubject = PublishSubject.create();
        mNumUnreadMessagesObservable = mNumUnreadMessagesSubject.share().replay(1).autoConnect();
        mNumUnreadMessagesObservable.subscribe();
        mNumUnreadMessagesSubject.onNext(0);

        setupEmitUnreadCountChanges();
    }

    @Override
    public Observable<Message> getMessagesObservable() {
        return mCache.getMessagesObservable();
    }

    @Override
    public Observable<Message> getSelectedMessageObservable() {
        return mSelectedRepo.getSelectedMessageObservable();
    }

    @Override
    public Observable<Message> getReadMessagesObservable() {
        return mReadRepo.getReadMessagesObservable();
    }

    @Override
    public Observable<Integer> getNumUnreadMessagesObservable() {
        return mNumUnreadMessagesObservable;
    }

    @Override
    public void putMessage(Message message) {
        mCache.addMessage(message);
    }

    @Override
    public Observable<Message> sendMessage(Message message) {
        return mSource.sendMessage(mMessageDataMapper.transformToData(message))
                .map(mMessageDataMapper::transform);
    }

    @Override
    public Observable<Message> getMessage(String messageId) {
        return Observable.just(messageId)
                .map(mCache::getMessageById);
    }

    @Override
    public void selectMessage(Message message) {
        mSelectedRepo.select(message);
    }

    @Override
    public void markMessageRead(Message message) {
        mReadRepo.read(message);
    }

    private void setupEmitUnreadCountChanges() {
        mCache.getNumMessagesObservable()
                .doOnNext(count -> {
                    mNumMessages = count;
                    publishUnreadMessagesCount();
                })
                .subscribe();

        mReadRepo.getNumReadMessagesObservable()
                .doOnNext(count -> {
                    mNumReadMessage = count;
                    publishUnreadMessagesCount();
                })
                .subscribe();
    }

    private void publishUnreadMessagesCount() {
        mNumUnreadMessagesSubject.onNext(mNumMessages - mNumReadMessage);
    }
}
