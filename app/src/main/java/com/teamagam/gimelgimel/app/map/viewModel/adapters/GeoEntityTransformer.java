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
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.MyLocationEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;

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

    public Entity transform(GeoEntity geoEntity) {
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
            mEntity = createBasicPointBuilder(point)
                    .setStringType(point.getSymbol().getType())
                    .build();
        }

        @Override
        public void visit(ImageEntity entity) {
            transformPointEntity(entity);
        }

        @Override
        public void visit(UserEntity entity) {
            transformPointEntity(entity);
        }

        @Override
        public void visit(MyLocationEntity entity) {
            transformPointEntity(entity);
        }

        private void transformPointEntity(GeoEntity entity) {
            mEntity = createBasicPointBuilder(entity)
                    .build();
        }

        private Point.Builder createBasicPointBuilder(GeoEntity entity) {
            PointGeometry pg = (PointGeometry) entity.getGeometry();
            PointGeometryApp pga = PointGeometryApp.create(pg);
            SymbolApp symbolApp = mSymbolizer.transform(entity.getSymbol());

            return new Point.Builder()
                    .setId(entity.getId())
                    .setGeometry(pga)
                    .setSymbol(symbolApp)
                    .setText(entity.getText());
        }
    }
}
