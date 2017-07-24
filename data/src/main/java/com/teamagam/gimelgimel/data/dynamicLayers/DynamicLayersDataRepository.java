package com.teamagam.gimelgimel.data.dynamicLayers;

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

  // ****** When changing this class please uncomment the test class in ******
  // ****** order to maintain it while applying changes to this class   ******

  private final DynamicLayerDao mDao;
  private final DynamicLayersEntityMapper mMapper;

  @Inject
  public DynamicLayersDataRepository(DynamicLayerDao dao, DynamicLayersEntityMapper mapper) {
    mDao = dao;
    mMapper = mapper;
  }

  @Override
  public void put(DynamicLayer dynamicLayer) {
    mDao.insertDynamicLayer(mMapper.mapToEntity(dynamicLayer));
  }

  @Override
  public DynamicLayer getById(String id) {
    if (!contains(id)) {
      throw new RuntimeException(String.format("DynamicLayer with id %s does not exist.", id));
    }
    return mMapper.mapToDomain(mDao.getDynamicLayerById(id));
  }

  @Override
  public boolean contains(String id) {
    DynamicLayerEntity entity = mDao.getDynamicLayerById(id);
    return entity != null;
  }

  @Override
  public Observable<DynamicLayer> getObservable() {
    return Flowable.fromIterable(mDao.getAllDynamicLayers())
        .mergeWith(mDao.getLatestDynamicLayer()).filter(dle -> dle.id != null)
        .map(mMapper::mapToDomain)
        .distinct(DynamicLayer::getId)
        .toObservable();
  }
}
