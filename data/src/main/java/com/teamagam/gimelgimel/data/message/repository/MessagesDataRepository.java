package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.repository.InMemory.InMemoryMessagesCache;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class MessagesDataRepository implements MessagesRepository, UnreadMessagesCountRepository {

    private static final String LAST_VISIT_TIMESTAMP = "last_visit_timestamp";
    private static final long BEGINNING_OF_MODERN_AGE_MILLISECONDS = 0;

    @Inject
    MessageDataMapper mMessageDataMapper;
    private final CloudMessagesSource mSource;

    private final InMemoryMessagesCache mCache;
    private final SelectedMessageRepository mSelectedRepo;
    private final UserPreferencesRepository mUserPreferencesRepository;

    private final ReplayRepository<Date> mLastVisitTimestampInnerRepo;
    private final ReplayRepository<Integer> mNumUnreadMessagesInnerRepo;

    private Date mLastVisitTimestamp;
    private int mNumUnreadMessages;

    @Inject
    public MessagesDataRepository(CloudMessagesSource cloudMessagesSource,
                                  InMemoryMessagesCache inMemoryMessagesCache,
                                  SelectedMessageRepository selectedMessageRepository,
                                  UserPreferencesRepository userPreferencesRepository) {
        mSource = cloudMessagesSource;
        mCache = inMemoryMessagesCache;
        mSelectedRepo = selectedMessageRepository;
        mUserPreferencesRepository = userPreferencesRepository;

        mLastVisitTimestampInnerRepo = ReplayRepository.createReplayCount(1);
        initLastVisitTimestamp();
        mNumUnreadMessagesInnerRepo = ReplayRepository.createReplayCount(1);
        resetNumUnreadMessages();
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
    public Observable<Integer> getNumUnreadMessagesObservable() {
        return mNumUnreadMessagesInnerRepo.getObservable();
    }

    @Override
    public void addNewUnreadMessage() {
        mNumUnreadMessagesInnerRepo.add(++mNumUnreadMessages);
    }

    @Override
    public Observable<Date> getLastVisitTimestampObservable() {
        return mLastVisitTimestampInnerRepo.getObservable();
    }

    @Override
    public Date getLastVisitTimestamp() {
        return mLastVisitTimestamp;
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
    public void readAllUntil(Date date) {
        updateLastVisitTimestamp(date);
        updateUserPreferencesRepository();
        resetNumUnreadMessages();
    }

    private void resetNumUnreadMessages() {
        mNumUnreadMessages = 0;
        mNumUnreadMessagesInnerRepo.add(mNumUnreadMessages);
    }

    private void initLastVisitTimestamp() {
        if (!mUserPreferencesRepository.contains(LAST_VISIT_TIMESTAMP)) {
            mLastVisitTimestamp = new Date(BEGINNING_OF_MODERN_AGE_MILLISECONDS);
            updateUserPreferencesRepository();
        }
        updateLastVisitTimestamp(new Date(mUserPreferencesRepository.getLong(LAST_VISIT_TIMESTAMP)));
    }

    private void updateLastVisitTimestamp(Date date) {
        mLastVisitTimestamp = date;
        mLastVisitTimestampInnerRepo.add(mLastVisitTimestamp);
    }

    private void updateUserPreferencesRepository() {
        mUserPreferencesRepository.setPreference(
                LAST_VISIT_TIMESTAMP, mLastVisitTimestamp.getTime());
    }

}
