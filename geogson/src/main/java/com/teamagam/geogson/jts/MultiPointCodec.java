package com.teamagam.geogson.jts;

import com.google.common.collect.FluentIterable;
import com.teamagam.geogson.core.model.MultiPoint;
import com.teamagam.geogson.core.model.Point;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import static com.google.common.collect.Iterables.transform;

/**
 * A {@link com.github.filosganga.geogson.codec.Codec} for {@link com.vividsolutions.jts.geom.MultiPoint}
 * and
 * {@link MultiPoint}.
 */
public class MultiPointCodec
    extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiPoint, MultiPoint> {

  /**
   * Create a codec for a {@link com.vividsolutions.jts.geom.MultiPoint JTS
   * MultiPoint} with a given {@link GeometryFactory}
   *
   * @param geometryFactory a {@link GeometryFactory} defining a PrecisionModel and a SRID
   */
  public MultiPointCodec(GeometryFactory geometryFactory) {
    super(geometryFactory);
  }

  @Override
  public MultiPoint toGeometry(com.vividsolutions.jts.geom.MultiPoint src) {
    return MultiPoint.of(transform(JtsPointIterable.of(src), fromJtsPointFn()));
  }

  @Override
  public com.vividsolutions.jts.geom.MultiPoint fromGeometry(MultiPoint src) {
    return this.geometryFactory.createMultiPoint(FluentIterable.from(src.points())
        .transform(Point.coordinatesFn())
        .transform(toJtsCoordinateFn())
        .toArray(Coordinate.class));
  }
}
