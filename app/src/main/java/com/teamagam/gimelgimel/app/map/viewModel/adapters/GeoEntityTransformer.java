package com.teamagam.gimelgimel.app.map.viewModel.adapters;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.map.GetMapEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;

import javax.inject.Inject;

import rx.Subscriber;

/**
 *
 */
@PerActivity
public class GeoEntityTransformer {

    @Inject
    SymbolTransformer mSymbolizer;

    @Inject
    GetMapEntityInteractorFactory getEntityInteractorFactory;

    @Inject
    public GeoEntityTransformer() {

    }

    public void transform(final String geoEntityId, final Subscriber<Entity> subscriber) {
        getEntityInteractorFactory.create(
                new SimpleSubscriber<GeoEntity>() {
                    @Override
                    public void onNext(GeoEntity geoEntity) {
                        subscriber.onNext(transform(geoEntity));
                    }
                },
                geoEntityId);
    }

    public Entity transform(GeoEntity geoEntity){
        return new GeoEntitySymbolizeVisitor().transform(geoEntity);
    }

    private class GeoEntitySymbolizeVisitor implements IGeoEntityVisitor {

        Entity mEntity;

        private Entity transform(GeoEntity geoEntity) {
            geoEntity.accept(this);
            return mEntity;
        }

        @Override
        public void visit(PointEntity point) {
            PointGeometry pg = point.getGeometry();

            PointGeometryApp pointGeometry = new PointGeometryApp(pg.getLatitude(), pg.getLongitude(),
                    pg.getAltitude());

            SymbolApp transform = mSymbolizer.transform(point.getSymbol());

            mEntity = new Point.Builder()
                    .setId(point.getId())
                    .setGeometry(pointGeometry)
                    .setSymbol(transform)
                    .setStringType(point.getSymbol().getType())
                    .setText(point.getText())
                    .build();
        }

        @Override
        public void visit(ImageEntity entity) {

            PointGeometry pg = entity.getGeometry();

            PointGeometryApp pointGeometry = new PointGeometryApp(pg.getLatitude(), pg.getLongitude(),
                    pg.getAltitude());

            SymbolApp transform = mSymbolizer.transform(new PointSymbol("Image"));

            mEntity = new Point.Builder()
                    .setId(entity.getId())
                    .setGeometry(pointGeometry)
                    .setSymbol(transform)
                    .setText(entity.getText())
                    .build();
        }

    }
}
