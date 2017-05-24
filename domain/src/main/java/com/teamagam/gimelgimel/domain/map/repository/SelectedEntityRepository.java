package com.teamagam.gimelgimel.domain.map.repository;

public interface SelectedEntityRepository {

  String getSelectedEntityId();

  void setSelected(String entityId);
}