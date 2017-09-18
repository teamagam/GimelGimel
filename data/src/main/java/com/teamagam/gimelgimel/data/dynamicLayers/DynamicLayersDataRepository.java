package com.teamagam.gimelgimel.data.dynamicLayers;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.base.repository.IdentifiedResourceDataRespository;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DynamicLayersDataRepository extends IdentifiedResourceDataRespository<DynamicLayer>
    implements DynamicLayersRepository {

  @Inject
  public DynamicLayersDataRepository(DynamicLayerDao dao, DynamicLayersEntityMapper mapper) {
    super(new DynamicLayerDatabaseDelegator(dao, mapper));
  }

  private static class DynamicLayerDatabaseDelegator implements DatabaseDelegator<DynamicLayer> {

    private final DynamicLayersEntityMapper mMapper;
    private final DynamicLayerDao mDao;

    private DynamicLayerDatabaseDelegator(DynamicLayerDao dao, DynamicLayersEntityMapper mapper) {
      mMapper = mapper;
      mDao = dao;
    }

    @Override
    public void insert(DynamicLayer data) {
      DynamicLayerEntity dynamicLayerEntity = mMapper.mapToEntity(data);
      mDao.insertDynamicLayer(dynamicLayerEntity);
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
    public Iterable<DynamicLayer> getAll() {
      List<DynamicLayerEntity> latestDynamicLayers = mDao.getLatestDynamicLayers();
      return Lists.transform(latestDynamicLayers, mMapper::mapToDomain);
    }
  }
}
