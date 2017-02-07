package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.repository.InMemory.InMemoryMessagesCache;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class MessagesDataRepository implements MessagesRepository {

    private final CloudMessagesSource mSource;
    private final InMemoryMessagesCache mCache;
    private final SelectedMessageRepository mSelectedRepo;
    @Inject
    MessageDataMapper mMessageDataMapper;

    @Inject
    public MessagesDataRepository(CloudMessagesSource cloudMessagesSource,
                                  InMemoryMessagesCache inMemoryMessagesCache,
                                  SelectedMessageRepository selectedMessageRepository) {
        mSource = cloudMessagesSource;
        mCache = inMemoryMessagesCache;
        mSelectedRepo = selectedMessageRepository;
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
}