package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;

/**
 * Created on 11/17/2016.
 */

public class UserEntity extends AbsGeoEntity {

    private PointGeometry mPointGeometry;
    private UserSymbol mUserSymbol;

    public UserEntity(String id, String userId, PointGeometry pointGeometry,
                      UserSymbol userSymbol) {
        super(id, userId);
        mPointGeometry = pointGeometry;
        mUserSymbol = userSymbol;
    }

    @Override
    public Geometry getGeometry() {
        return mPointGeometry;
    }

    @Override
    public Symbol getSymbol() {
        return mUserSymbol;
    }

    @Override
    public void accept(IGeoEntityVisitor visitor) {

    }
}
