package com.teamagam.gimelgimel.data.message.repository.cache.room.Converters;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.teamagam.gimelgimel.data.common.GsonFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public class PointGeometryConverter {
  private static Gson sGson;

  static {
    sGson = GsonFactory.createMessagingGson();
  }

  @TypeConverter
  public static String pointGeometryToJson(PointGeometry pointGeometry) {
    return sGson.toJson(pointGeometry);
  }

  @TypeConverter
  public static PointGeometry fromJson(String json) {
    return sGson.fromJson(json, PointGeometry.class);
  }
}
