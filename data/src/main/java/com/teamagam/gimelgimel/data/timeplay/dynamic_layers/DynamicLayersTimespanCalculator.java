package com.teamagam.gimelgimel.data.timeplay.dynamic_layers;

import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.domain.timeplay.GeoTimespanCalculator;
import java.util.Date;
import javax.inject.Inject;

public class DynamicLayersTimespanCalculator implements GeoTimespanCalculator {

  private static final Date ZERO_DATE = new Date(0);
  private final DynamicLayerDao mDynamicLayerDao;

  @Inject
  public DynamicLayersTimespanCalculator(DynamicLayerDao dynamicLayerDao) {
    mDynamicLayerDao = dynamicLayerDao;
  }

  @Override
  public Date getMinimumGeoItemDate() {
    return new Date(mDynamicLayerDao.getEarliestEntityfulLayerTimestamp());
  }

  @Override
  public Date getMaximumGeoItemDate() {
    return new Date(mDynamicLayerDao.getLatestEntityfulLayerTimestamp());
  }
}
