package com.teamagam.gimelgimel.data.timeplay;

import com.teamagam.gimelgimel.domain.timeplay.GeoTimespanCalculator;
import java.util.Date;

public class AggregationTimespanCalculator implements GeoTimespanCalculator {

  public static final Date ZERO_DATE = new Date(0);

  private GeoTimespanCalculator[] mCalculators;

  public AggregationTimespanCalculator(GeoTimespanCalculator... calculators) {
    mCalculators = calculators;
  }

  @Override
  public Date getMinimumGeoItemDate() {
    Date min = null;
    for (GeoTimespanCalculator calculator : mCalculators) {
      Date current = calculator.getMinimumGeoItemDate();
      if (isBefore(current, min)) {
        min = current;
      }
    }
    return getDateOrDefault(min);
  }

  @Override
  public Date getMaximumGeoItemDate() {
    Date max = ZERO_DATE;
    for (GeoTimespanCalculator calculator : mCalculators) {
      Date current = calculator.getMaximumGeoItemDate();
      if (isAfter(current, max)) {
        max = current;
      }
    }
    return max;
  }

  private boolean isBefore(Date date, Date other) {
    if (date == null) {
      return false;
    }
    if (other == null) {
      return true;
    }
    return date.before(other);
  }

  private Date getDateOrDefault(Date min) {
    if (min == null) {
      return ZERO_DATE;
    }
    return min;
  }

  private boolean isAfter(Date date, Date other) {
    if (date == null) {
      return false;
    }
    if (other == null) {
      return true;
    }
    return date.after(other);
  }
}
