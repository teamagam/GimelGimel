package com.teamagam.gimelgimel.data.map.repository;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.IconsDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ServerIconEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.ServerIconsEntityMapper;
import com.teamagam.gimelgimel.domain.map.entities.icons.Icon;
import com.teamagam.gimelgimel.domain.map.entities.icons.ServerIcon;
import com.teamagam.gimelgimel.domain.map.repository.IconsRepository;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IconsDataRepository implements IconsRepository {

  IconsDao mDao;
  ServerIconsEntityMapper mMapper;

  @Inject
  public IconsDataRepository(IconsDao dao, ServerIconsEntityMapper mapper) {
    mDao = dao;
    mMapper = mapper;
  }

  @Override
  public void put(ServerIcon serverIcon) {
    mDao.insertServerIcon(mMapper.mapToEntity(serverIcon));
  }

  @Override
  public List<Icon> getAvailableIcons() {
    return Lists.transform(mDao.getAllServerIcons(), this::iconFromServerIconEntity);
  }

  @Override
  public Icon get(String id) {
    ServerIconEntity entity = mDao.getServerIconById(id);
    if (entity == null) {
      return null;
    }

    return iconFromServerIconEntity(entity);
  }

  private Icon iconFromServerIconEntity(ServerIconEntity entity) {
    return createIconFrom(mMapper.mapToDomain(entity));
  }

  private Icon createIconFrom(ServerIcon serverIcon) {
    String displayName;
    if (isDeviceInEnglishMode()) {
      displayName = serverIcon.getDisplayNameEng();
    } else {
      displayName = serverIcon.getDisplayNameHeb();
    }
    return new Icon(serverIcon.getId(), serverIcon.getUrl(), displayName);
  }

  private boolean isDeviceInEnglishMode() {
    return Locale.getDefault() == Locale.ENGLISH;
  }
}
