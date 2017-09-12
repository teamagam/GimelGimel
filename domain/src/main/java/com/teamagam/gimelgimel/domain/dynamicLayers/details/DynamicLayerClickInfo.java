package com.teamagam.gimelgimel.domain.dynamicLayers.details;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;

public class DynamicLayerClickInfo {
  private final DynamicLayer mDynamicLayer;
  private final DynamicEntity mDynamicEntity;

  public DynamicLayerClickInfo(DynamicLayer dynamicLayer, DynamicEntity dynamicEntity) {
    mDynamicLayer = dynamicLayer;
    mDynamicEntity = dynamicEntity;
  }

  public DynamicLayer getDynamicLayer() {
    return mDynamicLayer;
  }

  public DynamicEntity getDynamicEntity() {
    return mDynamicEntity;
  }
}
