package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.map.entities.icons.ServerIcon;
import java.io.IOException;

public interface IconsFetcher {
  Iterable<ServerIcon> fetchIcons() throws IOException;
}
