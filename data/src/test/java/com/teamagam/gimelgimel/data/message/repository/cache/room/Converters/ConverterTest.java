package com.teamagam.gimelgimel.data.message.repository.cache.room.Converters;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import java.util.Date;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConverterTest extends BaseTest {

  private static final int EQUALS_VALUE = 0;

  @Test
  public void convertDate_expectTicks() throws Exception {
    // Arrange
    long expected = 636289654156321489L;
    Date d = new Date(expected);

    // Act
    long result = DateConverter.dateToTimestamp(d);

    // Assert
    assertEquals(result, expected);
  }

  @Test
  public void convertTicks_expectValidDate() throws Exception {
    // Arrange
    long ticks = 636289654156321489L;
    Date expected = new Date(ticks);

    // Act
    Date result = DateConverter.fromTimestamp(ticks);

    // Assert
    assertEquals(EQUALS_VALUE, result.compareTo(expected));
  }

  @Test
  public void convertNullDate_expectNull() throws Exception {
    Long result = DateConverter.dateToTimestamp(null);

    assertNull(result);
  }

  @Test
  public void convertValidStringToUrl_expectExceptionNotThrown() throws Exception {
    // Arrange
    String validUrl = "http://www.example.com/?file=2387128371283";

    // Act
    UrlConverter.fromString(validUrl);
  }
}