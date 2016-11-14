package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.map.DrawMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GetMapEntityInteractor;
import com.teamagam.gimelgimel.domain.map.GetMapEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

import javax.inject.Inject;

/**
 * Base ViewModel for Image and Geo (Both use map related functions)
 */

public abstract class MessageBaseGeoViewModel<V> extends MessageDetailViewModel<V> {

    @Inject
    GoToLocationMapInteractorFactory goToLocationMapInteractorFactory;

    @Inject
    DrawMessageOnMapInteractorFactory drawMessageOnMapInteractorFactory;

    @Inject
    GeoEntityTransformer mGeoEntityTransformer;

    @Inject
    GetMapEntityInteractorFactory getMapEntityInteractorFactory;

    private GetMapEntityInteractor mGetMapEntityInteractor;
    protected Entity mEntity;

    protected void gotoLocationClicked(PointGeometryApp point) {
        sLogger.userInteraction("goto button clicked");

        goToLocationMapInteractorFactory.create(point.getPointDomain()).execute();

    }

    public PointGeometryApp getPointGeometry() {
        if(mEntity != null) {
            return ((PointGeometryApp) mEntity.getGeometry());
        } else {
            return null;
        }
    }

    protected void showPinOnMapClicked() {
        sLogger.userInteraction("show pin button clicked");

        drawMessageOnMapInteractorFactory.create(mMessageSelected.getMessageId()).execute();

    }

    @Override
    protected void updateSelectedMessage() {
        mGetMapEntityInteractor = getMapEntityInteractorFactory.create(
                new SimpleSubscriber<GeoEntity>() {
                    @Override
                    public void onNext(GeoEntity geoEntity) {
                        mEntity = mGeoEntityTransformer.transform(geoEntity);
                        notifyChange();
                    }
                },
                getEntityId());
        mGetMapEntityInteractor.execute();
    }

    protected abstract String getEntityId();

    @Override
    public void stop() {
        super.stop();
        if (mGetMapEntityInteractor != null) {
            mGetMapEntityInteractor.unsubscribe();
        }
    }

}
