package com.teamagam.gimelgimel.app.map.viewModel.adapters;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.map.model.symbols.Symbol;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;

import javax.inject.Inject;

/**
 *
 */
@PerActivity
public class GeoEntityTransformer {

    @Inject
    SymbolTransformer mSymbolizer;

    @Inject
    public GeoEntityTransformer() {

    }

    public Entity transform(GeoEntity geoEntity) {
        GeoEntitySymbolizeVisitor geoEntitySymbolizeVisitor = new GeoEntitySymbolizeVisitor();
        geoEntity.accept(geoEntitySymbolizeVisitor );
        return geoEntitySymbolizeVisitor.mEntity;
    }

    private class GeoEntitySymbolizeVisitor implements IGeoEntityVisitor {

        Entity mEntity;

        @Override
        public void visit(PointEntity point) {
            com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry pg = point.getGeometry();

            PointGeometry pointGeometry = new PointGeometry(pg.getLatitude(), pg.getLongitude(),
                    pg.getAltitude());

            Symbol transform = mSymbolizer.transform(point.getSymbol());

            mEntity = new Point.Builder()
                    .setId(point.getId())
                    .setGeometry(pointGeometry)
                    .setSymbol(transform)
                    .build();
        }

    }
}
