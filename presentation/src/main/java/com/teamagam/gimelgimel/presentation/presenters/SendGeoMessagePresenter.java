package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.geometries.SendGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.geometries.entities.BaseGeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.geometries.entities.Symbol;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import javax.inject.Inject;

@PerFragment
public class SendGeoMessagePresenter extends AbstractPresenter {

    private SendGeoMessageInteractor mGeometryInteractor;
    private View mView;

    @Inject
    public SendGeoMessagePresenter(SendGeoMessageInteractor interactor) {
        mGeometryInteractor = interactor;
    }

    public void setView(View view) {
        mView = view;
    }

    @Override
    public void resume() {
        mView.hideProgress();
    }

    @Override
    public void pause() {
        mView.hideProgress();
    }

    @Override
    public void stop() {
        mView.hideProgress();
    }

    @Override
    public void destroy() {
        mView.hideProgress();
    }

    public void sendGeoMessage(String senderId, String messageText, double latitude,
                               double longitude, double altitude, String type) {

        PointGeometry geometry = new PointGeometry(latitude, longitude, altitude);
        GeoEntity geoEntity = createGeoEntity(senderId + messageText + type, geometry, type);
        MessageGeo message = new MessageGeo(senderId, geoEntity, messageText, type);

        mGeometryInteractor.sendGeoMessageEntity(message, new GeoSubscriber());
    }

    /**
     * Temp implementation of GeoEntity
     * @param id
     * @param geometry
     * @return
     */
    private GeoEntity createGeoEntity(String id, Geometry geometry, String type) {
        Symbol symbol = createSymbolFromType(type);

        return new BaseGeoEntity(id, geometry, symbol);
    }

    private Symbol createSymbolFromType(String type) {
        // TODO: define symbols models and create them by the type
        return null;
    }

    private class GeoSubscriber extends BaseSubscriber<MessageGeo> {
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(MessageGeo message) {
            super.onNext(message);

            mView.placeEntityOnMap(message.getGeoEntity());
        }
    }

    public interface View extends BaseView {
        void placeEntityOnMap(GeoEntity entity);
    }
}
