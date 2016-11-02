package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailGeoFragment;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.map.GetMapEntityInteractor;
import com.teamagam.gimelgimel.domain.map.GetMapEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;

import javax.inject.Inject;

/**
 * LatLong message view-model
 */
public class GeoMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailGeoFragment> {

    @Inject
    GeoEntityTransformer mGeoEntityTransformer;

    @Inject
    GetMapEntityInteractorFactory getMapEntityInteractorFactory;

    private PointGeometryApp mPoint;
    private String mType;
    private GetMapEntityInteractor mGetMapEntityInteractor;

    @Inject
    public GeoMessageDetailViewModel() {
        super();
    }

    public PointGeometryApp getPointGeometry() {
        return getSelectedMessagePointGeometry();
    }

    public String getText() {
        return ((MessageGeoApp) mMessageSelected).getContent().getText();
    }

    public String getLocationType() {
        return mType;
    }

    public void goToLocation() {
        super.gotoLocationClicked(getPointGeometry());
    }

    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

    @Override
    protected String getExpectedMessageType() {
        return MessageApp.GEO;
    }

    @Override
    protected void updateSelectedMessage() {
        mGetMapEntityInteractor = getMapEntityInteractorFactory.create(
                new SimpleSubscriber<GeoEntity>() {
                    @Override
                    public void onNext(GeoEntity geoEntity) {
                        mPoint = (PointGeometryApp) mGeoEntityTransformer.transform(geoEntity).getGeometry();
                        mType = ((PointEntity) geoEntity).getPointSymbol().getType();
                        notifyChange();
                    }
                },
                ((MessageGeoApp) mMessageSelected).getContent().getEntityId());
        mGetMapEntityInteractor.execute();
    }

    @Override
    public void stop() {
        super.stop();
        if (mGetMapEntityInteractor != null) {
            mGetMapEntityInteractor.unsubscribe();
        }
    }

    private PointGeometryApp getSelectedMessagePointGeometry() {
        return mPoint;
    }

}
