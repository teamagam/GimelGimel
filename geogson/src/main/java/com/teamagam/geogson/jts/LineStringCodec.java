package com.teamagam.geogson.jts;

import com.teamagam.geogson.core.model.LineString;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * A Codec for {@link com.vividsolutions.jts.geom.LineString} and
 * {@link LineString}.
 */
public class LineStringCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.LineString, LineString> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.LineString JTS
     * LineString} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public LineStringCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public LineString toGeometry(com.vividsolutions.jts.geom.LineString src) {
        return fromJtsLineString(src);
    }

    @Override
    public com.vividsolutions.jts.geom.LineString fromGeometry(LineString src) {
        return toJtsLineString(src);
    }
}
