package com.teamagam.gimelgimel.data.message.repository.cache.room.converters;

import android.arch.persistence.room.TypeConverter;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlConverter {

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
