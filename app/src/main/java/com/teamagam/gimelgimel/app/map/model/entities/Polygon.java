package com.teamagam.gimelgimel.app.map.model.entities;

import com.teamagam.gimelgimel.app.map.model.entities.visitors.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.map.model.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.map.model.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.Symbol;

/**
 * An entity class representing a polygon
 */
public class Polygon extends MultipleLocationsEntity {

    private static final String sPolygonPrefix = "polygon";
    private PolygonSymbol mPolygonSymbol;

    protected Polygon(String id, MultiPointGeometry pointsGeometry) {
        this(id, pointsGeometry, PolygonSymbol.DEFAULT);
    }

    protected Polygon(String id, MultiPointGeometry pointsGeometry, PolygonSymbol polygonSymbol) {
        super(id, pointsGeometry);
        mPolygonSymbol = polygonSymbol;
    }

    @Override
    public Symbol getSymbol() {
        return mPolygonSymbol;
    }

    @Override
    public void updateSymbol(Symbol symbol) {
        if (!(symbol instanceof PolygonSymbol)) {
            throw new UnsupportedOperationException(
                    "Given symbol is not supported for entities of type " + Polygon.class.getSimpleName());
        }

        mPolygonSymbol = (PolygonSymbol) symbol;
        fireEntityChanged();
    }

    @Override
    public void accept(IEntitiesVisitor visitor) {
        visitor.visit(this);
    }

    public static class Builder extends MultiPointGeometryBuilder<Builder, Polygon> {

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        protected String getIdPrefix() {
            return sPolygonPrefix;
        }

        @Override
        public Polygon build() {
            return new Polygon(mId, (MultiPointGeometry) mGeometry, (PolygonSymbol) mSymbol);
        }
    }

}
