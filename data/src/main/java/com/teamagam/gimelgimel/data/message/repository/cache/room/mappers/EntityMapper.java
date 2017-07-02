package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

public interface EntityMapper<T, R> {
  T mapToDomain(R entity);

  R mapToEntity(T entity);
}
