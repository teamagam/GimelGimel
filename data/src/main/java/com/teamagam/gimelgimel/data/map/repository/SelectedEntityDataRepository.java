package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;
import javax.inject.Inject;

public class SelectedEntityDataRepository implements SelectedEntityRepository {

  private String mGeoEntityId;
  private boolean mIsSelected;

  @Inject
  public SelectedEntityDataRepository() {
    mGeoEntityId = null;
    mIsSelected = false;
  }

  @Override
  public String getSelectedEntityId() {
    return mGeoEntityId;
  }

  @Override
  public boolean isSelected() {
    return mIsSelected;
  }

  @Override
  public void setSelected(String entityId) {
    mGeoEntityId = entityId;
    mIsSelected = true;
  }

  @Override
  public void deselect() {
    mGeoEntityId = null;
    mIsSelected = false;
  }
}