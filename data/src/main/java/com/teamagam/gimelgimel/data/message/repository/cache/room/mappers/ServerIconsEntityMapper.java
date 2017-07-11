package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ServerIconEntity;
import com.teamagam.gimelgimel.domain.icons.entities.ServerIcon;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ServerIconsEntityMapper implements EntityMapper<ServerIcon, ServerIconEntity> {

  @Inject
  public ServerIconsEntityMapper() {
  }

  public ServerIcon mapToDomain(ServerIconEntity entity) {
    return new ServerIcon(entity.id, entity.url, entity.displayNameEng, entity.displayNameHeb);
  }

  public ServerIconEntity mapToEntity(ServerIcon serverIcon) {
    ServerIconEntity entity = new ServerIconEntity();
    entity.id = serverIcon.getId();
    entity.url = serverIcon.getUrl();
    entity.displayNameEng = serverIcon.getDisplayNameEng();
    entity.displayNameHeb = serverIcon.getDisplayNameHeb();
    return entity;
  }
}
