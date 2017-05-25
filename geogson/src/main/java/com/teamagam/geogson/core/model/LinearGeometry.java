/*
 * Copyright 2013 Filippo De Luca - me@filippodeluca.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teamagam.geogson.core.model;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.teamagam.geogson.core.model.positions.LinearPositions;
import com.teamagam.geogson.core.model.positions.SinglePosition;

/**
 * An abstract Geometry that is composed by a sequence of points.
 */
public abstract class LinearGeometry extends AbstractGeometry<LinearPositions> {

  private static final long serialVersionUID = 1L;

  protected LinearGeometry(LinearPositions positions) {
    super(positions);
  }

  /**
   * Guava Function that converts to MultiPoint.
   *
   * @return Guava Function instance to convert to MultiPoint.
   */
  public static <T extends LinearGeometry> Function<T, MultiPoint> toMultiPointFn() {
    return new Function<T, MultiPoint>() {
      @Override
      public MultiPoint apply(T input) {
        return input.toMultiPoint();
      }
    };
  }

  /**
   * Guava Function that converts to LineString.
   *
   * @return Guava Function instance to convert to LineString.
   */
  public static <T extends LinearGeometry> Function<T, LineString> toLineStringFn() {
    return new Function<T, LineString>() {
      @Override
      public LineString apply(T input) {
        return input.toLineString();
      }
    };
  }

  /**
   * Guava Function that converts to LinearRing.
   *
   * @return Guava Function instance to convert to LinearRing.
   */
  public static <T extends LinearGeometry> Function<T, LinearRing> toLinearRingFn() {
    return new Function<T, LinearRing>() {
      @Override
      public LinearRing apply(T input) {
        return input.toLinearRing();
      }
    };
  }

  /**
   * Converts to a MultiPoint.
   *
   * @return MultiPoint
   */
  public MultiPoint toMultiPoint() {
    return new MultiPoint(positions());
  }

  /**
   * Converts to a LineString.
   *
   * @return LineString
   */
  public LineString toLineString() {
    return new LineString(positions());
  }

  /**
   * Convert to a LinearRing.
   *
   * @return LinearRing
   */
  public LinearRing toLinearRing() {
    return new LinearRing(positions());
  }

  /**
   * Returns the points composing this Geometry.
   *
   * @return {@code Iterable<Point>} a Guava lazy Iterable.
   */
  public Iterable<Point> points() {
    return FluentIterable.from(positions().children())
        .transform(SinglePosition.coordinatesFn())
        .transform(new Function<Coordinates, Point>() {
          @Override
          public Point apply(Coordinates input) {
            return Point.from(input);
          }
        });
  }
}
