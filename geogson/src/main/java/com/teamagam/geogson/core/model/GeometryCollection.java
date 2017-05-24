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

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.teamagam.geogson.core.model.positions.Positions;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import java.io.Serializable;

/**
 * Collection of {@link Geometry} holding an {@link Iterable} being a
 * {@link Geometry}.
 * <p>
 * GeoJson reference: @see http://geojson.org/geojson-spec.html#geometry-collection
 */
public class GeometryCollection implements Geometry<Positions>, Serializable {

  /**
   * internal Version for Serialization starting with 1. <b>Increment by 1 at
   * every change!</b>
   */
  private static final long serialVersionUID = 1L;

  /**
   * Geometries of this {@link GeometryCollection}
   */
  private final Iterable<Geometry<?>> geometries;

  /**
   * Constructor creating a {@link GeometryCollection} out of
   * {@link Iterable} {@link Geometry Geometries}.
   *
   * @param geometries Geometries of this {@link GeometryCollection}
   */
  private GeometryCollection(Iterable<Geometry<?>> geometries) {
    this.geometries = geometries;
  }

  public static GeometryCollection of(Iterable<Geometry<?>> geometries) {
    return new GeometryCollection(geometries);
  }

  /**
   * Get the {@link Iterable} {@link Geometry Geometries} of this
   * {@link GeometryCollection}
   *
   * @return all {@link Geometry Geometries} of this {@link Iterable} (e.g.
   * Collection)
   */
  public Iterable<Geometry<?>> getGeometries() {
    return this.geometries;
  }

  @Override
  public Type type() {
    return Type.GEOMETRY_COLLECTION;
  }

  @Override
  public Positions positions() {
    Positions positions = new SinglePosition(null);
    for (Geometry<?> geometry : this.geometries) {
      positions.merge(geometry.positions());
    }
    return positions;
  }

  @Override
  public int size() {
    return Iterables.size(geometries);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getClass(), this.geometries);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final GeometryCollection other = (GeometryCollection) obj;
    return Objects.equal(this.geometries, other.geometries);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("geometries", this.geometries) //$NON-NLS-1$
        .toString();
  }
}
