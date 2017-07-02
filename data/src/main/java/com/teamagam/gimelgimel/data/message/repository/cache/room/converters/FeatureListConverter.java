package com.teamagam.gimelgimel.data.message.repository.cache.room.converters;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;
import com.google.common.base.Strings;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import java.util.EnumSet;

public class FeatureListConverter {

  private static final String DELIMITER = ",";

  @TypeConverter
  public static String featuresToString(EnumSet<ChatMessageEntity.Feature> list) {
    return list == null ? null : TextUtils.join(DELIMITER, list);
  }

  @TypeConverter
  public static EnumSet<ChatMessageEntity.Feature> fromString(String value) {
    if (Strings.isNullOrEmpty(value)) {
      return null;
    }

    return createFeatureSet(value);
  }

  private static EnumSet<ChatMessageEntity.Feature>createFeatureSet(String featuresString) {
    String[] features = featuresString.split(DELIMITER);
    EnumSet<ChatMessageEntity.Feature> set = EnumSet.noneOf(ChatMessageEntity.Feature.class);

    for (String feature : features) {
      set.add(ChatMessageEntity.Feature.valueOf(feature.toUpperCase()));
    }

    return set;
  }
}
