package com.teamagam.gimelgimel.data.phases;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.base.repository.IdentifiedResourceDataRespository;
import com.teamagam.gimelgimel.data.phases.room.PhaseLayerDao;
import com.teamagam.gimelgimel.data.phases.room.PhaseLayerEntity;
import com.teamagam.gimelgimel.data.phases.room.PhaseLayerMapper;
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import com.teamagam.gimelgimel.domain.phase.PhaseLayerRepository;
import java.util.List;
import javax.inject.Inject;

public class PhaseLayerDataRepository extends IdentifiedResourceDataRespository<PhaseLayer>
    implements PhaseLayerRepository {

  @Inject
  public PhaseLayerDataRepository(PhaseLayerDao phaseLayerDao, PhaseLayerMapper phaseLayerMapper) {
    super(new PhaseLayerDatabaseDelegator(phaseLayerDao, phaseLayerMapper));
  }

  private static class PhaseLayerDatabaseDelegator implements DatabaseDelegator<PhaseLayer> {

    private final PhaseLayerDao mPhaseLayerDao;
    private final PhaseLayerMapper mPhaseLayerMapper;

    private PhaseLayerDatabaseDelegator(PhaseLayerDao phaseLayerDao,
        PhaseLayerMapper phaseLayerMapper) {
      mPhaseLayerDao = phaseLayerDao;
      mPhaseLayerMapper = phaseLayerMapper;
    }

    @Override
    public void insert(PhaseLayer data) {
      PhaseLayerEntity entity = mPhaseLayerMapper.mapToEntity(data);
      mPhaseLayerDao.insert(entity);
    }

    @Override
    public PhaseLayer getById(String id) {
      PhaseLayerEntity entity = mPhaseLayerDao.getLatestPhaseLayerById(id);
      return mPhaseLayerMapper.mapToDomain(entity);
    }

    @Override
    public boolean contains(String id) {
      return mPhaseLayerDao.contains(id);
    }

    @Override
    public Iterable<PhaseLayer> getAll() {
      List<PhaseLayerEntity> entities = mPhaseLayerDao.getLatestPhaseLayers();
      return Lists.transform(entities, mPhaseLayerMapper::mapToDomain);
    }
  }
}
