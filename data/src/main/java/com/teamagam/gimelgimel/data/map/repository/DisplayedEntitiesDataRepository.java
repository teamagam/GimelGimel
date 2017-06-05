package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;

/**
 * repository to be synced with the app for shown entities on the map
 */
@Singleton
public class DisplayedEntitiesDataRepository implements DisplayedEntitiesRepository {

  private final Map<String, GeoEntity> mDisplayedEntitiesMap;
  private final SubjectRepository<GeoEntityNotification> mInnerRepo;

  @Inject
  DisplayedEntitiesDataRepository() {
    mDisplayedEntitiesMap = new TreeMap<>();
    mInnerRepo = SubjectRepository.createReplayAll();
  }

  @Override
  public Observable<GeoEntityNotification> getObservable() {
    return mInnerRepo.getObservable();
  }

  @Override
  public void show(GeoEntity geoEntity) {
    mDisplayedEntitiesMap.put(geoEntity.getId(), geoEntity);

    GeoEntityNotification addNotification = GeoEntityNotification.createAdd(geoEntity);

    mInnerRepo.add(addNotification);
  }

  @Override
  public void hide(GeoEntity geoEntity) {
    GeoEntity deletedEntity = mDisplayedEntitiesMap.remove(geoEntity.getId());

    if (deletedEntity != null) {
      GeoEntityNotification removeNotification = GeoEntityNotification.createRemove(geoEntity);

      mInnerRepo.add(removeNotification);
    }
  }

  public void update(GeoEntity geoEntity) {
    if (isShown(geoEntity)) {
      mDisplayedEntitiesMap.remove(geoEntity.getId());
      mDisplayedEntitiesMap.put(geoEntity.getId(), geoEntity);
      GeoEntityNotification updateNotification = GeoEntityNotification.createUpdate(geoEntity);

      mInnerRepo.add(updateNotification);
    }
  }

  @Override
  public boolean isShown(GeoEntity geoEntity) {
    return mDisplayedEntitiesMap.containsKey(geoEntity.getId());
  }
}
