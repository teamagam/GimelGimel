package com.teamagam.gimelgimel.data.map;

import android.support.annotation.NonNull;
import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.response.entity.ServerIconResponse;
import com.teamagam.gimelgimel.data.response.rest.IconsAPI;
import com.teamagam.gimelgimel.domain.map.IconsFetcher;
import com.teamagam.gimelgimel.domain.map.entities.icons.ServerIcon;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;

@Singleton
public class IconsDataFetcher implements IconsFetcher {

  private IconsAPI mIconsAPI;

  @Inject
  public IconsDataFetcher(IconsAPI iconsAPI) {
    mIconsAPI = iconsAPI;
  }

  //TODO: make a strategy that will take action with there is a problem getting the icons from the server.

  @Override
  public Iterable<ServerIcon> fetchIcons() {
    Call<List<ServerIconResponse>> serverIcons = mIconsAPI.getServerIcons();
    try {
      return getIcons(serverIcons);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @NonNull
  private Iterable<ServerIcon> getIcons(Call<List<ServerIconResponse>> serverIcons)
      throws IOException {
    return Lists.transform(serverIcons.execute().body(), this::transform);
  }

  private ServerIcon transform(ServerIconResponse response) {
    return new ServerIcon(response.getId(), response.getUrl(), response.getDisplayNameEng(),
        response.getDisplayNameHeb());
  }
}