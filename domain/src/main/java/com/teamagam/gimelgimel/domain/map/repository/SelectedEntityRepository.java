package com.teamagam.gimelgimel.domain.map.repository;

public interface SelectedEntityRepository {

  String getSelectedEntityId();

  boolean isSelected();

  void setSelected(String entityId);

  void deselect();
}