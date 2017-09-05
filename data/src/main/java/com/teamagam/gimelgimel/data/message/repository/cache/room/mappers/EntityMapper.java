package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

public interface EntityMapper<DOMAIN, ENTITY> {
  DOMAIN mapToDomain(ENTITY entity);

  ENTITY mapToEntity(DOMAIN entity);
}
