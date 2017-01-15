package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.repository.InMemory.InMemoryMessagesCache;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created on 8/10/2016.
 */
@Singleton
public class MessagesDataRepository implements MessagesRepository {

    @Inject
    MessageDataMapper mMessageDataMapper;

    private final CloudMessagesSource mSource;
    private final InMemoryMessagesCache mCache;
    private final SelectedMessageRepository mSelectedRepo;
    private final ReadMessagesRepository mReadRepo;

    private final ReplayRepository<Integer> mNumUnreadMessagesInnerRepo;
    private final ReplayRepository<Date> mLastVisitTimestampInnerRepo;
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

        mLastVisitTimestampInnerRepo = ReplayRepository.createReplayCount(1);
        mNumUnreadMessagesInnerRepo = ReplayRepository.createReplayCount(1);
        mNumUnreadMessagesInnerRepo.add(0);

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
        return mNumUnreadMessagesInnerRepo.getObservable();
    }

    @Override
    public Observable<Date> getLastVisitTimestamp() {
        return mLastVisitTimestampInnerRepo.getObservable();
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

    @Override
    public void readAllUntil(Date date) {
        mLastVisitTimestampInnerRepo.add(date);
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
        mNumUnreadMessagesInnerRepo.add(mNumMessages - mNumReadMessage);
    }
}
