package com.teamagam.gimelgimel.data.message.repository.cache.room.Converters;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import java.util.EnumSet;

public class FeatureListConverter {

  private static final String DELIMITER = ",";

  @TypeConverter
  public static String featuresToString(EnumSet<ChatMessageEntity.Feature> list) {
    return TextUtils.join(DELIMITER, list);
  }

  @TypeConverter
  public static EnumSet<ChatMessageEntity.Feature> fromString(String value) {
    String[] featureStrings = value.split(DELIMITER);
    EnumSet<ChatMessageEntity.Feature> set = EnumSet.noneOf(ChatMessageEntity.Feature.class);

    for (String featureString : featureStrings) {
      set.add(ChatMessageEntity.Feature.valueOf(featureString.toUpperCase()));
    }

    return set;
  }
}
