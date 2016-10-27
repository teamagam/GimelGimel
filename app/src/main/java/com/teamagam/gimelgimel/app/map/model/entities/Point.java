package com.teamagam.gimelgimel.app.map.model.entities;

import com.teamagam.gimelgimel.app.map.model.entities.visitors.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.map.model.geometries.GeometryApp;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PointSymbolApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;

/**
 * An entity class representing a point
 */
public class Point extends AbsEntity {

    private static final String sPointPrefix = "point";
    private PointGeometryApp mPointGeometry;
    private PointSymbolApp mPointSymbol;

    protected Point(String id, PointGeometryApp pointGeometry) {
        this(id, pointGeometry, PointTextSymbol.DEFAULT);
    }

    protected Point(String id, PointGeometryApp pointGeometry, PointSymbolApp pointSymbol) {
        super(id);
        mPointGeometry = pointGeometry;
        mPointSymbol = pointSymbol;
    }

    @Override
    public GeometryApp getGeometry() {
        return mPointGeometry;
    }

    @Override
    public SymbolApp getSymbol() {
        return mPointSymbol;
    }

    @Override
    public void updateGeometry(GeometryApp geo) {
        if (!(geo instanceof PointGeometryApp)) {
            throw new UnsupportedOperationException(
                    "Given geometry is not supported for entities of type " + Point.class.getSimpleName());
        }

        mPointGeometry = (PointGeometryApp) geo;
        fireEntityChanged();
    }

    @Override
    public void updateSymbol(SymbolApp symbol) {
        if (!(symbol instanceof PointSymbolApp)) {
            throw new UnsupportedOperationException(
                    "Given symbol is not supported for entities of type " + Point.class.getSimpleName());
        }

        mPointSymbol = (PointSymbolApp) symbol;
        fireEntityChanged();
    }

    @Override
    public void accept(IEntitiesVisitor visitor) {
        visitor.visit(this);
    }

    public static class Builder extends EntityBuilder<Builder, Point> {

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        protected String getIdPrefix() {
            return sPointPrefix;
        }

        @Override
        public Point build() {
            return new Point(mId, (PointGeometryApp) mGeometry, (PointSymbolApp) mSymbol);
        }
    }
}
