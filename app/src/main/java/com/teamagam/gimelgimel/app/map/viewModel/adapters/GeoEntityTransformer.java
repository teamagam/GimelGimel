package com.teamagam.gimelgimel.app.map.viewModel.adapters;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.map.model.symbols.PointSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;

import javax.inject.Inject;

/**
 *
 */
@PerActivity
public class GeoEntityTransformer {

    @Inject
    public GeoEntityTransformer() {
    }

    public Entity transform(GeoEntity geoEntity) {
        com.teamagam.gimelgimel.domain.map.entities.PointGeometry point = (com.teamagam.gimelgimel.domain.map.entities.PointGeometry) geoEntity.getGeometry();
        PointGeometry pointGeometry = new PointGeometry(point.getLatitude(), point.getLongitude(),
                point.getAltitude());
        PointSymbol symbol = new PointTextSymbol("#aaffff00", "ab", 48);
        return new Point.Builder()
                .setId(geoEntity.getId())
                .setGeometry(pointGeometry )
                .setSymbol(symbol)
                .build();
    }
}
