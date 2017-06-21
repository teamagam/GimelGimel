package com.teamagam.gimelgimel.data.message.repository.cache.room.converters;

import android.text.TextUtils;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import java.net.URL;
import java.util.Date;
import java.util.EnumSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class ConverterTest extends BaseTest {

  private static final String DELIMITER = ",";
  private String mFeatureListString;
  private EnumSet<ChatMessageEntity.Feature> mFeatures;

  @Before
  public void setUp() throws Exception {
    mFeatureListString = "TEXT,IMAGE";
    mFeatures = EnumSet.noneOf(ChatMessageEntity.Feature.class);
    mFeatures.add(ChatMessageEntity.Feature.TEXT);
    mFeatures.add(ChatMessageEntity.Feature.IMAGE);

    PowerMockito.mockStatic(TextUtils.class);
    BDDMockito.given(TextUtils.join(DELIMITER, mFeatures)).willReturn(mFeatureListString);
  }

  @Test
  public void convertDate_expectTicks() throws Exception {
    // Arrange
    long expected = 636289654156321489L;
    Date d = new Date(expected);

    // Act
    long result = DateConverter.dateToTimestamp(d);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void convertTicks_expectValidDate() throws Exception {
    // Arrange
    long ticks = 636289654156321489L;
    Date expectedDate = new Date(ticks);

    // Act
    Date resultDate = DateConverter.fromTimestamp(ticks);

    // Assert
    assertEquals(expectedDate, resultDate);
  }

  @Test
  public void convertNullDate_expectNull() throws Exception {
    Long result = DateConverter.dateToTimestamp(null);

    assertNull(result);
  }

  @Test
  public void convertValidStringToUrl_expectNotNull() throws Exception {
    // Arrange
    String validUrl = "http://www.example.com/?file=2387128371283";

    // Act
    URL url = UrlConverter.fromString(validUrl);

    // Assert
    assertNotNull(url);
  }

  @Test
  public void convertInvalidStringToUrl_expectNull() throws Exception {
    // Arrange
    String invalidUrl = "invalidUrl";

    // Act
    URL url = UrlConverter.fromString(invalidUrl);

    // Assert
    assertNull(url);
  }

  @Test
  public void testFeatureListConversion() throws Exception {
    // Act
    String featuresString = FeatureListConverter.featuresToString(mFeatures);

    // Assert
    assertEquals(mFeatureListString, featuresString);
  }

  @Test
  public void testStringToFeatureListConversion() throws Exception {
    // Act
    EnumSet<ChatMessageEntity.Feature> features =
        FeatureListConverter.fromString(mFeatureListString);

    // Assert
    assertArrayEquals(mFeatures.toArray(), features.toArray());
  }
}