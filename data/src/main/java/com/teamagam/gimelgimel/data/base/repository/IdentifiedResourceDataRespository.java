package com.teamagam.gimelgimel.data.base.repository;

import com.teamagam.gimelgimel.domain.base.repository.IdentifiedData;
import com.teamagam.gimelgimel.domain.base.repository.IdentifiedResourceRepository;
import io.reactivex.Observable;

public class IdentifiedResourceDataRespository<T extends IdentifiedData>
    implements IdentifiedResourceRepository<T> {

  private final DatabaseDelegator<T> mDatabaseDelegator;
  private final SubjectRepository<T> mSubjectRepository;

  public IdentifiedResourceDataRespository(DatabaseDelegator<T> databaseDelegator) {
    mDatabaseDelegator = databaseDelegator;
    mSubjectRepository = SubjectRepository.createSimpleSubject();
  }

  @Override
  public void put(T resource) {
    mDatabaseDelegator.insert(resource);
    mSubjectRepository.add(resource);
  }

  @Override
  public T getById(String id) {
    return mDatabaseDelegator.getById(id);
  }

  @Override
  public boolean contains(String id) {
    return mDatabaseDelegator.contains(id);
  }

  @Override
  public Observable<T> getObservable() {
    return Observable.fromIterable(mDatabaseDelegator.getAll())
        .mergeWith(mSubjectRepository.getObservable());
  }

  public interface DatabaseDelegator<T> {
    void insert(T data);

    T getById(String id);

    boolean contains(String id);

    Iterable<T> getAll();
  }
}
