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

package com.teamagam.geogson.core.gson;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.teamagam.geogson.core.model.Feature;
import com.teamagam.geogson.core.model.FeatureCollection;
import com.teamagam.geogson.core.model.Geometry;
import com.teamagam.geogson.core.model.GeometryCollection;
import com.teamagam.geogson.core.model.LineString;
import com.teamagam.geogson.core.model.LinearRing;
import com.teamagam.geogson.core.model.MultiLineString;
import com.teamagam.geogson.core.model.MultiPoint;
import com.teamagam.geogson.core.model.MultiPolygon;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.Polygon;
import com.teamagam.geogson.core.model.positions.AreaPositions;
import com.teamagam.geogson.core.model.positions.LinearPositions;
import com.teamagam.geogson.core.model.positions.MultiDimensionalPositions;
import com.teamagam.geogson.core.model.positions.Positions;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.geogson.core.util.ChainableOptional;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The Gson TypeAdapterFactory responsible to serialize/de-serialize all the {@link Geometry}, {@link Feature}
 * and {@link FeatureCollection} instances.
 */
public class GeometryAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (Geometry.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new GeometryAdapter(gson);
        } else if (Positions.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new PositionsAdapter();
        } else if (Feature.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new FeatureAdapter(gson);
        } else if (FeatureCollection.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) new FeatureCollectionAdapter(gson);
        } else {
            return null;
        }
    }

    private static class GeometryAdapter extends TypeAdapter<Geometry> {

        private final Gson gson;

        private GeometryAdapter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public void write(JsonWriter out, Geometry value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginObject();

                out.name("type").value(value.type().getValue());
                if (value.type() == Geometry.Type.GEOMETRY_COLLECTION) {
                    out.name("geometries"); //$NON-NLS-1$
                    out.beginArray();
                    GeometryCollection geometries = (GeometryCollection) value;
                    for (Geometry<?> geometry : geometries.getGeometries()) {
                        this.gson.getAdapter(Geometry.class).write(out, geometry);
                    }
                    out.endArray();
                } else {
                    out.name("coordinates");
                    gson.getAdapter(Positions.class).write(out, value.positions());
                }
                out.endObject();
            }
        }

        @Override
        public Geometry<?> read(JsonReader in) throws IOException {

            Geometry<?> geometry = null;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
            } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
                in.beginObject();

                String type = null;
                Positions positions = null;
                Geometry<?> geometries = null;

                while (in.hasNext()) {
                    String name = in.nextName();
                    if ("type".equals(name)) {
                        type = in.nextString();
                    } else if ("coordinates".equals(name)) {
                        positions = readPosition(in);
                    } else if ("geometries".equals(name)) {
                        geometries = readGeometries(in);
                    } else {
                        in.skipValue();
                    }
                }

                geometry = buildGeometry(type, positions, geometries);

                in.endObject();

            } else {
                throw new IllegalArgumentException("The given json is not a valid Geometry: " + in.peek());
            }

            return geometry;
        }

        private Positions readPosition(JsonReader in) throws IOException {
            return this.gson.getAdapter(Positions.class).read(in);
        }

        private Geometry<?> readGeometries(JsonReader in) throws IOException {
            Geometry<?> parsed;

            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                parsed = null;
            } else if (peek == JsonToken.BEGIN_ARRAY) {
                parsed = parseGeometries(in);
            } else {
                throw new IllegalArgumentException("The json must be an array or null: " + in.peek());
            }

            return parsed;
        }

        private Geometry<?> parseGeometries(JsonReader in) throws IOException {

            Optional<Geometry<?>> parsed = Optional.absent();

            if (in.peek() != JsonToken.BEGIN_ARRAY) {
                throw new IllegalArgumentException("The given json is not a valid GeometryCollection");
            }

            in.beginArray();
            if (in.peek() == JsonToken.BEGIN_OBJECT) {
                ArrayList<Geometry<?>> geometries = new ArrayList<Geometry<?>>();
                while (in.hasNext()) {
                    @SuppressWarnings("rawtypes")
                    Geometry geometry = this.gson.getAdapter(Geometry.class).read(in);
                    geometries.add(geometry);
                }
                parsed = Optional.of(GeometryCollection.of(geometries));
            }

            in.endArray();

            return parsed.orNull();
        }

        private Geometry<?> buildGeometry(final String type, Positions positions, Geometry<?> geometries) {

            // Take care, the order is important!
            return ChainableOptional
                    .of(buildGeometryCollection(type, geometries))
                    .or(buildMultiPolygon(type, positions))
                    .or(buildPolygon(type, positions))
                    .or(buildLinearRing(type, positions))
                    .or(buildMultiLineString(type, positions))
                    .or(buildLineString(type, positions))
                    .or(buildMultiPoint(type, positions))
                    .or(buildPoint(type, positions))
                    .orFinally(throwUnsupportedType(type));
        }

        private Supplier<Optional<? extends Geometry<?>>> buildPoint(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry<?>>>() {
                @Override
                public Optional<Geometry<?>> get() {
                    Optional<Geometry<?>> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.POINT.getValue())) {
                        mayGeometry = Optional.of(Point.from(((SinglePosition) coordinates).coordinates()));
                    }

                    return mayGeometry;
                }
            };


        }

        private Supplier<Optional<? extends Geometry<?>>> buildMultiPoint(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry<?>>>() {
                @Override
                public Optional<Geometry<?>> get() {
                    Optional<Geometry<?>> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.MULTI_POINT.getValue())) {
                        mayGeometry = Optional.of(new MultiPoint((LinearPositions) coordinates));
                    }

                    return mayGeometry;
                }
            };

        }

        private Supplier<Optional<? extends Geometry<?>>> buildMultiLineString(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry<?>>>() {
                @Override
                public Optional<Geometry<?>> get() {
                    Optional<Geometry<?>> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.MULTI_LINE_STRING.getValue())) {
                        mayGeometry = Optional.of(new MultiLineString((AreaPositions) coordinates));
                    }

                    return mayGeometry;
                }
            };
        }

        private Supplier<Optional<? extends Geometry<?>>> buildLineString(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry<?>>>() {
                @Override
                public Optional<Geometry<?>> get() {
                    Optional<Geometry<?>> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.LINE_STRING.getValue())) {
                        mayGeometry = Optional.of(new LineString((LinearPositions) coordinates));
                    }

                    return mayGeometry;
                }
            };
        }

        private Supplier<Optional<? extends Geometry<?>>> buildLinearRing(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry<?>>>() {
                @Override
                public Optional<Geometry<?>> get() {
                    Optional<Geometry<?>> mayGeometry = Optional.absent();

                    if (type.equalsIgnoreCase(Geometry.Type.LINEAR_RING.getValue())) {
                        LinearPositions linearPositions = (LinearPositions) coordinates;
                        if (linearPositions.isClosed()) {
                            mayGeometry = Optional.of(new LinearRing(linearPositions));
                        }
                    }

                    return mayGeometry;
                }
            };


        }

        private Supplier<Optional<? extends Geometry<?>>> buildPolygon(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry<?>>>() {
                @Override
                public Optional<Geometry<?>> get() {
                    Optional<Geometry<?>> mayGeometry = Optional.absent();

                    if (Geometry.Type.POLYGON.getValue().equalsIgnoreCase(type)) {
                        mayGeometry = Optional.of(new Polygon((AreaPositions) coordinates));
                    }

                    return mayGeometry;
                }
            };


        }

        private Supplier<Optional<? extends Geometry<?>>> buildMultiPolygon(final String type, final Positions coordinates) {

            return new Supplier<Optional<? extends Geometry<?>>>() {
                @Override
                public Optional<Geometry<?>> get() {
                    Optional<Geometry<?>> mayGeometry = Optional.absent();
                    if (Geometry.Type.MULTI_POLYGON.getValue().equalsIgnoreCase(type)) {
                        mayGeometry = Optional.of(new MultiPolygon((MultiDimensionalPositions) coordinates));
                    }

                    return mayGeometry;
                }
            };
        }

        private Supplier<Optional<? extends Geometry<?>>> buildGeometryCollection(final String type, final Geometry<?> geometries) {

            return new Supplier<Optional<? extends Geometry<?>>>() {
                @Override
                public Optional<Geometry<?>> get() {
                    Optional<Geometry<?>> mayGeometry = Optional.absent();
                    if (Geometry.Type.GEOMETRY_COLLECTION.getValue().equalsIgnoreCase(type)) {
                        mayGeometry = Optional.of(geometries);
                    }

                    return mayGeometry;
                }
            };

        }

        private Supplier<Geometry<?>> throwUnsupportedType(final String type) {
            return new Supplier<Geometry<?>>() {
                @Override
                public Geometry<?> get() {
                    throw new IllegalArgumentException("Cannot build a geometry for type: " + type);
                }
            };
        }


    }
}
