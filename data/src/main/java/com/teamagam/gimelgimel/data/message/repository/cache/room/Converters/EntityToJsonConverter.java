package com.teamagam.gimelgimel.data.message.repository.cache.room.Converters;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.teamagam.gimelgimel.data.common.GsonFactory;

public abstract class EntityToJsonConverter<T> {

  private Class<? extends T> mClazz;
  private Gson mGson;

  public EntityToJsonConverter(Class<? extends T> clazz) {
    mClazz = clazz;
    mGson = GsonFactory.getMessagingGson();
  }

  @TypeConverter
  public String geoFeatureEntityToJson(T source) {
    return mGson.toJson(source);
  }

  @TypeConverter
  public T fromJson(String json) {
    return mGson.fromJson(json, mClazz);
  }
}
