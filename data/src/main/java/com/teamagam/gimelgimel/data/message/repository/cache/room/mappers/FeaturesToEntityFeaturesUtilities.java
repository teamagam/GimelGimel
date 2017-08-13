package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.AlertFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ImageFeatureEntity;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import javax.inject.Inject;

public class FeaturesToEntityFeaturesUtilities {
  private GeoFeatureEntityMapper mGeoFeatureEntityMapper;

  @Inject
  public FeaturesToEntityFeaturesUtilities(GeoFeatureEntityMapper geoFeatureEntityMapper) {
    mGeoFeatureEntityMapper = geoFeatureEntityMapper;
  }

  public GeoFeatureEntity getGeoFeatureEntity(GeoFeature geoEntity) {
    return mGeoFeatureEntityMapper.mapToEntity(geoEntity.getGeoEntity());
  }

  public ImageFeatureEntity getImageFeatureEntity(ImageFeature feature) {
    ImageFeatureEntity imageFeatureEntity = new ImageFeatureEntity();

    imageFeatureEntity.localUrl = feature.getLocalUrl();
    imageFeatureEntity.remoteUrl = feature.getRemoteUrl();
    imageFeatureEntity.source = feature.getSource();
    imageFeatureEntity.time = feature.getTime();

    return imageFeatureEntity;
  }

  public AlertFeatureEntity getAlertFeatureEntity(AlertFeature feature) {
    Alert alert = feature.getAlert();
    AlertFeatureEntity alertEntity = new AlertFeatureEntity();

    alertEntity.alertId = alert.getId();
    alertEntity.severity = alert.getSeverity();
    alertEntity.source = alert.getSource();
    alertEntity.text = alert.getText();
    alertEntity.time = alert.getTime();
    alertEntity.type = alert.getType().ordinal();

    return alertEntity;
  }
}
