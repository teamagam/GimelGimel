package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.VectorLayersEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.VectorLayerDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.VectorLayerEntity;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VectorLayersDataRepository implements VectorLayersRepository {

  private VectorLayerDao mDao;
  private VectorLayersEntityMapper mMapper;

  @Inject
  public VectorLayersDataRepository(VectorLayerDao dao, VectorLayersEntityMapper mapper) {
    mDao = dao;
    mMapper = mapper;
  }

  @Override
  public Observable<VectorLayer> getVectorLayersObservable() {
    return mDao.getVectorLayers()
        .flatMapIterable(x -> x)
        .map(mMapper::convertToDomain)
        .toObservable();
  }

  @Override
  public void put(VectorLayer vectorLayer) {
    mDao.insertVectorLayer(mMapper.convertToEntity(vectorLayer));
  }

  @Override
  public VectorLayer get(String id) {
    VectorLayerEntity entity = mDao.getVectorLayerById(id);
    if (entity == null) {
      return null;
    }

    return mMapper.convertToDomain(entity);
  }

  @Override
  public boolean contains(String id) {
    return get(id) != null;
  }

  @Override
  public boolean isOutdatedVectorLayer(VectorLayer vectorLayer) {
    VectorLayer cachedVectorLayer = get(vectorLayer.getId());

    return cachedVectorLayer != null && cachedVectorLayer.getVersion() >= vectorLayer.getVersion();
  }
}
