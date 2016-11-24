package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public class MyLocationEntity extends AbsGeoEntity {

    private MyLocationSymbol mMyLocationSymbol;
    private PointGeometry mPointGeometry;

    public MyLocationEntity(String id,
                            String text,
                            MyLocationSymbol mMyLocationSymbol,
                            PointGeometry mPointGeometry) {
        super(id, text);
        this.mMyLocationSymbol = mMyLocationSymbol;
        this.mPointGeometry = mPointGeometry;
    }

    @Override
    public PointGeometry getGeometry() {
        return mPointGeometry;
    }

    @Override
    public Symbol getSymbol() {
        return mMyLocationSymbol;
    }

    @Override
    public void accept(IGeoEntityVisitor visitor) {
        visitor.visit(this);
    }
}
