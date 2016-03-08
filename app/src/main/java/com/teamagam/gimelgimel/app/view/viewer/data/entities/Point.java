package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.Geometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

/**
 * Created by Bar on 29-Feb-16.
 */
public class Point extends AbsEntity {

    private PointGeometry mPointGeometry;
    private PointSymbol mPointSymbol;

    //TODO: enable instantiation via some builder-pattern that manages ids
    public Point(String id, PointGeometry pointGeometry) {
        this(id, pointGeometry, PointTextSymbol.DEFAULT);
    }

    public Point(String id, PointGeometry pointGeometry, PointSymbol pointSymbol) {
        super(id);
        mPointGeometry = pointGeometry;
        mPointSymbol = pointSymbol;
    }

    @Override
    public Geometry getGeometry() {
        return mPointGeometry;
    }

    @Override
    public Symbol getSymbol() {
        return mPointSymbol;
    }

    @Override
    public void updateGeometry(Geometry geo) {
        if (!(geo instanceof PointGeometry)) {
            throw new UnsupportedOperationException(
                    "Given geometry is not supported for entities of type " + Point.class.getSimpleName());
        }

        mPointGeometry = (PointGeometry) geo;
        mEntityChangedListener.OnEntityChanged(this);
    }

    @Override
    public void updateSymbol(Symbol symbol) {
        if (!(symbol instanceof PointSymbol)) {
            throw new UnsupportedOperationException(
                    "Given symbol is not supported for entities of type " + Point.class.getSimpleName());
        }

        mPointSymbol = (PointSymbol) symbol;
        mEntityChangedListener.OnEntityChanged(this);
    }

    @Override
    public void accept(IEntitiesVisitor visitor) {
        visitor.visit(this);
    }
}
