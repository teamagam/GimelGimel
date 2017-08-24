package com.teamagam.gimelgimel.data.dynamicLayers;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DynamicLayersDataRepository implements DynamicLayersRepository {

  private final DynamicLayerDao mDao;
  private final DynamicLayersEntityMapper mMapper;

  private final SubjectRepository<DynamicLayer> mDynamicLayerSubject;

  @Inject
  public DynamicLayersDataRepository(DynamicLayerDao dao, DynamicLayersEntityMapper mapper) {
    mDao = dao;
    mMapper = mapper;
    mDynamicLayerSubject = SubjectRepository.createSimpleSubject();
  }

  @Override
  public void put(DynamicLayer dynamicLayer) {
    mDao.insertDynamicLayer(mMapper.mapToEntity(dynamicLayer));
    mDynamicLayerSubject.add(dynamicLayer);
  }

  @Override
  public DynamicLayer getById(String id) {
    if (!contains(id)) {
      throw new RuntimeException(String.format("DynamicLayer with id %s does not exist.", id));
    }
    return mMapper.mapToDomain(mDao.getLatestDynamicLayerById(id));
  }

  @Override
  public boolean contains(String id) {
    DynamicLayerEntity entity = mDao.getLatestDynamicLayerById(id);
    return entity != null;
  }

  @Override
  public Observable<DynamicLayer> getObservable() {
    return Flowable.fromIterable(mDao.getLatestDynamicLayers())
        .filter(dle -> dle.id != null)
        .map(mMapper::mapToDomain).toObservable().mergeWith(mDynamicLayerSubject.getObservable());
  }
}
