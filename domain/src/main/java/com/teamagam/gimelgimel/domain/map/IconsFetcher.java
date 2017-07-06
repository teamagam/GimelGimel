package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.map.entities.icons.ServerIcon;

public interface IconsFetcher {
  Iterable<ServerIcon> fetchIcons();
}
