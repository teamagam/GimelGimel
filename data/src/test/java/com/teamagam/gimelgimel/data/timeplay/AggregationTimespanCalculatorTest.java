package com.teamagam.gimelgimel.data.timeplay;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.timeplay.GeoTimespanCalculator;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class AggregationTimespanCalculatorTest extends BaseTest {

  private AggregationTimespanCalculator mAggregationTimespanCalculator;
  private GeoTimespanCalculator mCalculator1;
  private GeoTimespanCalculator mCalculator2;

  @Before
  public void setUp() throws Exception {
    mCalculator1 = Mockito.mock(GeoTimespanCalculator.class);
    mCalculator2 = Mockito.mock(GeoTimespanCalculator.class);
    mAggregationTimespanCalculator = new AggregationTimespanCalculator(mCalculator1, mCalculator2);
  }

  @Test
  public void onEmptyDb_returnMaxDateZero() throws Exception {
    //Act
    Date maximumGeoItemDate = mAggregationTimespanCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(maximumGeoItemDate, is(new Date(0)));
  }

  @Test
  public void onEmptyDb_returnMinDateZero() throws Exception {
    //Act
    Date maximumGeoItemDate = mAggregationTimespanCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(maximumGeoItemDate, is(new Date(0)));
  }

  @Test
  public void getMaximum() throws Exception {
    //Arrange
    Date actualMaximum = new Date(30);
    when(mCalculator1.getMaximumGeoItemDate()).thenReturn(actualMaximum);
    when(mCalculator2.getMaximumGeoItemDate()).thenReturn(new Date(actualMaximum.getTime() - 1));

    //Act
    Date result = mAggregationTimespanCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(result, is(actualMaximum));
  }

  @Test
  public void getMinimum() throws Exception {
    //Arrange
    Date actualMin = new Date(20);
    when(mCalculator1.getMinimumGeoItemDate()).thenReturn(actualMin);
    when(mCalculator2.getMinimumGeoItemDate()).thenReturn(new Date(actualMin.getTime() + 1));

    //Act
    Date result = mAggregationTimespanCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(result, is(actualMin));
  }

  @Test
  public void tieForMinimum() throws Exception {
    //Arrange
    Date min = new Date(10);
    when(mCalculator1.getMinimumGeoItemDate()).thenReturn(min);
    when(mCalculator2.getMinimumGeoItemDate()).thenReturn(min);

    //Act
    Date result = mAggregationTimespanCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(result, is(min));
  }

  @Test
  public void tieForMaximum() throws Exception {
    //Arrange
    Date max = new Date(10);
    when(mCalculator1.getMaximumGeoItemDate()).thenReturn(max);
    when(mCalculator2.getMaximumGeoItemDate()).thenReturn(max);

    //Act
    Date result = mAggregationTimespanCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(result, is(max));
  }
}