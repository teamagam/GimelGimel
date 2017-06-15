package com.teamagam.gimelgimel.data.response.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.NavigableSet;
import java.util.TreeSet;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UnreadMessagesCountDataRepository implements UnreadMessagesCountRepository {

  private static final String LAST_VISIT_TIMESTAMP = "last_visit_timestamp";
  private static final long BEGINNING_OF_MODERN_AGE_MILLISECONDS = 0;

  private final UserPreferencesRepository mUserPreferencesRepository;

  private final SubjectRepository<Date> mLastVisitTimestampInnerRepo;
  private final SubjectRepository<Integer> mNumUnreadMessagesInnerRepo;

  private final NavigableSet<Date> mUnreadMessagesDates;

  private Date mLastVisitTimestamp;

  @Inject
  public UnreadMessagesCountDataRepository(UserPreferencesRepository userPreferencesRepository) {
    mUserPreferencesRepository = userPreferencesRepository;

    mLastVisitTimestampInnerRepo = SubjectRepository.createReplayCount(1);
    initLastVisitTimestamp();
    mNumUnreadMessagesInnerRepo = SubjectRepository.createReplayCount(1);
    resetNumUnreadMessages();

    mUnreadMessagesDates = new TreeSet<>();
  }

  @Override
  public Observable<Integer> getNumUnreadMessagesObservable() {
    return mNumUnreadMessagesInnerRepo.getObservable();
  }

  @Override
  public synchronized void addNewUnreadMessage(Date messageDate) {
    mUnreadMessagesDates.add(messageDate);
    mNumUnreadMessagesInnerRepo.add(mUnreadMessagesDates.size());
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
  public synchronized void updateLastVisit(Date date) {
    updateLastVisitTimestamp(date);
    updateUserPreferencesRepository();
    updateUnreadCount(date);
  }

  private void resetNumUnreadMessages() {
    mNumUnreadMessagesInnerRepo.add(0);
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
    mUserPreferencesRepository.setPreference(LAST_VISIT_TIMESTAMP, mLastVisitTimestamp.getTime());
  }

  private void updateUnreadCount(Date date) {
    Collection<Date> toBeRemoved = getToRemoveDates(date);
    mUnreadMessagesDates.removeAll(toBeRemoved);
    mNumUnreadMessagesInnerRepo.add(mUnreadMessagesDates.size());
  }

  private Collection<Date> getToRemoveDates(Date date) {
    return new ArrayList<>(mUnreadMessagesDates.headSet(date, true));
  }
}