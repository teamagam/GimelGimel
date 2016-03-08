package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

/**
 * Created by Bar on 29-Feb-16.
 */
public class Polyline extends MultipleLocationsEntity {

    private PolylineSymbol mPolylineSymbol;

    //TODO: enable instantiation via some builder-pattern that manages ids
    public Polyline(String id, MultiPointGeometry pointsGeometry) {
        this(id, pointsGeometry, PolylineSymbol.DEFAULT);
    }

    public Polyline(String id,
                    MultiPointGeometry pointsGeometry,
                    PolylineSymbol polylineSymbol) {
        super(id, pointsGeometry);
        mPolylineSymbol = polylineSymbol;
    }

    @Override
    public Symbol getSymbol() {
        return mPolylineSymbol;
    }

    @Override
    public void updateSymbol(Symbol symbol) {
        if (!(symbol instanceof PolylineSymbol)) {
            throw new UnsupportedOperationException(
                    "Given symbol is not supported for entities of type " + Polyline.class.getSimpleName());
        }

        mPolylineSymbol = (PolylineSymbol) symbol;
        mEntityChangedListener.OnEntityChanged(this);
    }

    @Override
    public void accept(IEntitiesVisitor visitor) {
        visitor.visit(this);
    }
}
