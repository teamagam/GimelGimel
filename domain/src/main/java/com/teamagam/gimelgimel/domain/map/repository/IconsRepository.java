package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.icons.Icon;
import com.teamagam.gimelgimel.domain.map.entities.icons.ServerIcon;
import java.util.List;

public interface IconsRepository {
  void put(ServerIcon icon);

  List<Icon> getAvailableIcons();

  Icon get(String id);
}
