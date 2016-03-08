package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

/**
 * Created by Bar on 29-Feb-16.
 */
public class Polygon extends MultipleLocationsEntity {

    private PolygonSymbol mPolygonSymbol;

    //TODO: enable instantiation via some builder-pattern that manages ids
    public Polygon(String id, MultiPointGeometry pointsGeometry) {
        this(id, pointsGeometry, PolygonSymbol.DEFAULT);
    }

    public Polygon(String id,
                   MultiPointGeometry pointsGeometry,
                   PolygonSymbol polygonSymbol) {
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
        mEntityChangedListener.OnEntityChanged(this);
    }

    @Override
    public void accept(IEntitiesVisitor visitor) {
        visitor.visit(this);
    }
}
