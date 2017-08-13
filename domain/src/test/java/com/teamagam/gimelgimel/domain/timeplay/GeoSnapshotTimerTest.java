package com.teamagam.gimelgimel.domain.timeplay;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GeoSnapshotTimerTest {

  private static final long MINIMUM_TIME_MILLIS = 100;
  private static final long MAXIMUM_TIME_MILLIS = 1100;
  private GeoTimespanCalculator mGeoTimespanCalculatorMock;

  private GeoSnapshotTimer createTimer(long initialTimestamp, int intervalCount) {
    return new GeoSnapshotTimer(mGeoTimespanCalculatorMock, intervalCount, initialTimestamp);
  }

  private GeoSnapshotTimer createTimerWithMinInitialTimestamp(int intervalCount) {
    return createTimer(MINIMUM_TIME_MILLIS, intervalCount);
  }

  @Before
  public void setUp() throws Exception {
    mGeoTimespanCalculatorMock = mock(GeoTimespanCalculator.class);
    when(mGeoTimespanCalculatorMock.getMinimumGeoItemDate()).thenReturn(
        new Date(MINIMUM_TIME_MILLIS));
    when(mGeoTimespanCalculatorMock.getMaximumGeoItemDate()).thenReturn(
        new Date(MAXIMUM_TIME_MILLIS));
  }

  @Test
  public void zeroIntervals_returnInitialTime() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = new GeoSnapshotTimer(mGeoTimespanCalculatorMock, 0, 500);

    //Act
    long nextSnapshotTime = timer.getNextSnapshotTime();

    //Assert
    assertThat(nextSnapshotTime, is(500L));
  }

  @Test
  public void compliesToInitialTimestamp() throws Exception {
    //Arrange
    long initialTimestamp = 700;
    GeoSnapshotTimer timer = createTimer(initialTimestamp, 2);

    //Act
    long nextSnapshotTime = timer.getNextSnapshotTime();

    //Assert
    assertThat(nextSnapshotTime, is(initialTimestamp));
  }

  @Test
  public void initialTimestamp_periodicalBetweenMinAndMax() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimer(700, 4);

    //Act
    long[] res = new long[5];
    for (int i = 0; i < 5; i++) {
      res[i] = timer.getNextSnapshotTime();
    }

    //Assert
    assertThat(res, is(new long[] { 700, 950, 1100, 100, 350 }));
  }

  @Test
  public void onInitialTimestampLessThanMin_shouldRoundToMinTime() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimer(0, 5);

    //Act
    long nextSnapshotTime = timer.getNextSnapshotTime();

    //Assert
    assertThat(nextSnapshotTime, is(timer.getMinTime()));
  }

  @Test
  public void fromMinToMax() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimerWithMinInitialTimestamp(1);

    //Act
    long time1 = timer.getNextSnapshotTime();
    long time2 = timer.getNextSnapshotTime();

    //Assert
    assertThat(time1, is(MINIMUM_TIME_MILLIS));
    assertThat(time2, is(MAXIMUM_TIME_MILLIS));
  }

  @Test
  public void periodical() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimerWithMinInitialTimestamp(5);
    long[] results = new long[12];

    //Act
    for (int i = 0; i < results.length; i++) {
      results[i] = timer.getNextSnapshotTime();
    }

    //Assert
    assertThat(results, is(new long[] {
        100, 300, 500, 700, 900, 1100, 100, 300, 500, 700, 900, 1100
    }));
  }

  @Test
  public void hitMaxTimeWhenIntervalCountDoesntDivideMax() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimerWithMinInitialTimestamp(2);

    //Act
    timer.getNextSnapshotTime();
    timer.getNextSnapshotTime();
    long lastSnapshot = timer.getNextSnapshotTime();

    //Assert
    assertThat(lastSnapshot, is(MAXIMUM_TIME_MILLIS));
  }

  @Test
  public void getMax() throws Exception {
    assertThat(MAXIMUM_TIME_MILLIS, is(createTimerWithMinInitialTimestamp(1).getMaxTime()));
  }

  @Test
  public void getMin() throws Exception {
    assertThat(MINIMUM_TIME_MILLIS, is(createTimerWithMinInitialTimestamp(1).getMinTime()));
  }

  @Test
  public void getMax_cachesResult() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimerWithMinInitialTimestamp(1);

    //Act
    timer.getMaxTime();
    timer.getMaxTime();

    //Assert
    verify(mGeoTimespanCalculatorMock).getMaximumGeoItemDate();
  }

  @Test
  public void getMin_cachesResult() throws Exception {
    //Arrange
    GeoSnapshotTimer timer = createTimerWithMinInitialTimestamp(1);

    //Act
    timer.getMinTime();
    timer.getMinTime();

    //Assert
    verify(mGeoTimespanCalculatorMock).getMinimumGeoItemDate();
  }
}