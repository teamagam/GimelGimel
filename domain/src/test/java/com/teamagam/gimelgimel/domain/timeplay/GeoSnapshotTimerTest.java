package com.teamagam.gimelgimel.domain.timeplay;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeoSnapshotTimerTest {

  private static final int MINIMUM_TIME_MILLIS = 0;
  private static final int MAXIMUM_TIME_MILLIS = 1000;
  private GeoTimespanCalculator mGeoTimespanCalculatorMock;

  @Before
  public void setUp() throws Exception {
    mGeoTimespanCalculatorMock = mock(GeoTimespanCalculator.class);
    when(mGeoTimespanCalculatorMock.getMinimumGeoItemDate()).thenReturn(
        new Date(MINIMUM_TIME_MILLIS));
    when(mGeoTimespanCalculatorMock.getMaximumGeoItemDate()).thenReturn(
        new Date(MAXIMUM_TIME_MILLIS));
  }

  @Test
  public void fromMinToMax() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimer(1);

    //Act
    long time1 = timer.getNextSnapshotTime();
    long time2 = timer.getNextSnapshotTime();

    //Assert
    assertThat(time1, is((long) MINIMUM_TIME_MILLIS));
    assertThat(time2, is((long) MAXIMUM_TIME_MILLIS));
  }

  @Test
  public void periodical() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimer(5);
    long[] results = new long[12];

    //Act
    for (int i = 0; i < results.length; i++) {
      results[i] = timer.getNextSnapshotTime();
    }

    //Assert
    assertThat(results, is(new long[] {
        0, 200, 400, 600, 800, 1000, 0, 200, 400, 600, 800, 1000
    }));
  }

  @Test
  public void hitMaxTimeWhenIntervalCountDoesntDivideMax() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimer(2);

    //Act
    timer.getNextSnapshotTime();
    timer.getNextSnapshotTime();
    long lastSnapshot = timer.getNextSnapshotTime();

    //Assert
    assertThat(lastSnapshot, is((long) MAXIMUM_TIME_MILLIS));
  }

  private GeoSnapshotTimer createTimer(int intervalCount) {
    return new GeoSnapshotTimer(mGeoTimespanCalculatorMock, intervalCount);
  }
}