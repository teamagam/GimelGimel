package com.teamagam.geogson.jts;

import static com.google.common.collect.Iterables.transform;


import com.google.common.collect.FluentIterable;
import com.teamagam.geogson.core.model.MultiLineString;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * A Codec for {@link com.vividsolutions.jts.geom.MultiLineString} and
 * {@link MultiLineString}.
 */
public class MultiLineStringCodec extends AbstractJtsCodec<com.vividsolutions.jts.geom.MultiLineString, MultiLineString> {

    /**
     * Create a codec for a {@link com.vividsolutions.jts.geom.MultiLineString JTS
     * MultiLineString} with a given {@link GeometryFactory}
     *
     * @param geometryFactory
     *          a {@link GeometryFactory} defining a PrecisionModel and a SRID
     */
    public MultiLineStringCodec(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    @Override
    public MultiLineString toGeometry(com.vividsolutions.jts.geom.MultiLineString src) {
        return MultiLineString.of(transform(JtsLineStringIterable.of(src), fromJtsLineStringFn()));
    }

    @Override
    public com.vividsolutions.jts.geom.MultiLineString fromGeometry(MultiLineString src) {
        return this.geometryFactory.createMultiLineString(
                FluentIterable.from(src.lineStrings())
                        .transform(toJtsLineStringFn())
                        .toArray(com.vividsolutions.jts.geom.LineString.class)
        );
    }
}
