package com.teamagam.gimelgimel.app.map.model.entities;

import com.teamagam.gimelgimel.app.map.model.entities.visitors.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.map.model.geometries.MultiPointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;

/**
 * An entity class representing a polygon
 */
public class Polygon extends MultipleLocationsEntity {

    private static final String sPolygonPrefix = "polygon";
    private PolygonSymbol mPolygonSymbol;

    protected Polygon(String id, MultiPointGeometryApp pointsGeometry) {
        this(id, pointsGeometry, PolygonSymbol.DEFAULT);
    }

    protected Polygon(String id, MultiPointGeometryApp pointsGeometry, PolygonSymbol polygonSymbol) {
        super(id, pointsGeometry);
        mPolygonSymbol = polygonSymbol;
    }

    @Override
    public SymbolApp getSymbol() {
        return mPolygonSymbol;
    }

    @Override
    public void updateSymbol(SymbolApp symbol) {
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
            return new Polygon(mId, (MultiPointGeometryApp) mGeometry, (PolygonSymbol) mSymbol);
        }
    }

}
