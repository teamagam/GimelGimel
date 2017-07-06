package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.map.entities.icons.ServerIcon;

interface IconsFetcher {
  Iterable<ServerIcon> fetchIcons();
}
