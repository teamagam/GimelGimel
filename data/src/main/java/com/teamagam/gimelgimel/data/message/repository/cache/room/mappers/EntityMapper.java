package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

public interface EntityMapper<T, R> {
  T convertToDomain(R entity);

  R convertToEntity(T entity);
}
