package com.teamagam.geogson.jts;

import com.teamagam.geogson.core.model.Point;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * A Codec for {@link com.vividsolutions.jts.geom.Point} and
 * {@link Point}.
 */
public class PointCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.Point, Point> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.Point JTS
     * Point} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public PointCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public Point toGeometry(com.vividsolutions.jts.geom.Point src) {
        return fromJtsPoint(src);
    }

    @Override
    public com.vividsolutions.jts.geom.Point fromGeometry(Point src) {
        return this.geometryFactory.createPoint(new Coordinate(src.lon(), src.lat()));
    }

}
