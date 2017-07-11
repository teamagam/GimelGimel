package com.teamagam.gimelgimel.domain.icons;

import com.teamagam.gimelgimel.domain.icons.entities.ServerIcon;
import java.io.IOException;

public interface IconsFetcher {
  Iterable<ServerIcon> fetchIcons() throws IOException;
}
