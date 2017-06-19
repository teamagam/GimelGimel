package com.teamagam.gimelgimel.data.message.repository.cache.room.Converters;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.teamagam.gimelgimel.data.common.GsonFactory;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlConverter {

  private static Gson sGson;

  static {
    sGson = GsonFactory.createMessagingGson();
  }

  @TypeConverter
  public static String urlToString(URL url) {
    return url.toString();
  }

  @TypeConverter
  public static URL fromString(String value) {
    try {
      return new URL(value);
    } catch (MalformedURLException e) {
      return null;
    }
  }
}
