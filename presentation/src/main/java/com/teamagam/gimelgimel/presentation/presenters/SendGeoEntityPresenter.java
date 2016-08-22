package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.geometries.GeometryInteractor;
import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.geometries.entities.Symbol;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;

public class SendGeoEntityPresenter extends AbstractPresenter {

    private GeometryInteractor mGeometryInteractor;
    private View mView;

    public SendGeoEntityPresenter(GeometryInteractor geometryInteractor, View view) {
        mGeometryInteractor = geometryInteractor;
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
        MessageGeo message = new MessageGeo(senderId, geoEntity, messageText);

        mGeometryInteractor.addUserGeoEntity(message, new GeoSubscriber());
    }

    /**
     * Temp implementation of GeoEntity
     * @param id
     * @param geometry
     * @return
     */
    private GeoEntity createGeoEntity(String id, Geometry geometry, String type) {
        return new GeoEntity() {

            @Override
            public String getId() {
                return id;
            }

            @Override
            public Geometry getGeometry() {
                return geometry;
            }

            @Override
            public Symbol getSymbol() {
                return new Symbol() {
                    @Override
                    public void setSymbolProperty(String prop) {

                    }

                    @Override
                    public String getSymbolProperty() {
                        return null;
                    }
                };
            }

            @Override
            public void updateGeometry(Geometry geo) {

            }

            @Override
            public void updateSymbol(Symbol symbol) {

            }
        };
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
