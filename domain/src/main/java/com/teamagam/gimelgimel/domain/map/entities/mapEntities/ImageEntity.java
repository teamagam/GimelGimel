package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

/**
 * Created on 11/6/2016.
 */

public class ImageEntity extends AbsGeoEntity {

    private PointGeometry mPointGeometry;
    private ImageSymbol mImageSymbol;

    public ImageEntity(String id, String text, PointGeometry pointGeometry) {
        super(id, text);
        mPointGeometry = pointGeometry;
        mImageSymbol = new ImageSymbol();
    }

    @Override
    public PointGeometry getGeometry() {
        return mPointGeometry;
    }

    @Override
    public Symbol getSymbol() {
        return mImageSymbol;
    }

    @Override
    public void accept(IGeoEntityVisitor visitor) {
        visitor.visit(this);
    }

}
