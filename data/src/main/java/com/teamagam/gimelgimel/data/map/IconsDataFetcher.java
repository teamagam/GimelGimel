package com.teamagam.gimelgimel.data.map;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.response.entity.ServerIconResponse;
import com.teamagam.gimelgimel.data.response.rest.IconsAPI;
import com.teamagam.gimelgimel.domain.map.IconsFetcher;
import com.teamagam.gimelgimel.domain.map.entities.icons.ServerIcon;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IconsDataFetcher implements IconsFetcher {

  private IconsAPI mIconsAPI;

  @Inject
  public IconsDataFetcher(IconsAPI iconsAPI) {
    mIconsAPI = iconsAPI;
  }

  @Override
  public Iterable<ServerIcon> fetchIcons() throws IOException {
    return Lists.transform(getIconsFromServer(), this::transform);
  }

  private List<ServerIconResponse> getIconsFromServer() throws IOException {
    return mIconsAPI.getServerIconsCall().execute().body();
  }

  private ServerIcon transform(ServerIconResponse response) {
    return new ServerIcon(response.getId(), response.getUrl(), response.getDisplayNameEng(),
        response.getDisplayNameHeb());
  }
}