package com.teamagam.gimelgimel.domain.icons.repository;

import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.icons.entities.ServerIcon;
import java.util.List;

public interface IconsRepository {
  void put(ServerIcon icon);

  List<Icon> getAvailableIcons();

  Icon get(String id);
}
