package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;

/**
 * Created on 11/6/2016.
 * TODO: complete text
 */

public class ImageEntity extends AbsGeoEntity {

    private PointGeometry mPointGeometry;

    public ImageEntity(String id, PointGeometry pointGeometry) {
        super(id);
        mPointGeometry = pointGeometry;
    }

    @Override
    public PointGeometry getGeometry() {
        return mPointGeometry;
    }

    @Override
    public void accept(IGeoEntityVisitor visitor) {
        visitor.visit(this);
    }

}
