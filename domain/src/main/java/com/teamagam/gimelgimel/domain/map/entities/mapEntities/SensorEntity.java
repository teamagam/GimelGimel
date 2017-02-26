package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.SensorSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public class SensorEntity extends AbsGeoEntity {

    private PointGeometry mPointGeometry;
    private SensorSymbol mSensorSymbol;

    public SensorEntity(String id, String text, PointGeometry pointGeometry, boolean isSelected) {
        super(id, text);
        mPointGeometry = pointGeometry;
        mSensorSymbol = new SensorSymbol(isSelected, text);
    }

    @Override
    public PointGeometry getGeometry() {
        return mPointGeometry;
    }

    @Override
    public Symbol getSymbol() {
        return mSensorSymbol;
    }

    @Override
    public void accept(IGeoEntityVisitor visitor) {
        visitor.visit(this);
    }
}