package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailGeoFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.domain.map.DrawMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

/**
 * LatLong message view-model
 */
@AutoFactory
public class GeoMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailGeoFragment> {

    private GeoEntityTransformer mGeoEntityTransformer;
    private MessageAppMapper mMessageAppMapper;
    private PointGeometryApp mPoint;
    private String mType;
    private String mText;

    public GeoMessageDetailViewModel(
            @Provided Context context,
            @Provided DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory,
            @Provided GoToLocationMapInteractorFactory gotoFactory,
            @Provided DrawMessageOnMapInteractorFactory drawFactory,
            @Provided GeoEntityTransformer geoEntityTransformer,
            @Provided MessageAppMapper messageAppMapper) {
        super(context, selectedMessageInteractorFactory, gotoFactory, drawFactory);

        mGeoEntityTransformer = geoEntityTransformer;
        mMessageAppMapper = messageAppMapper;
    }

    @Override
    public void start() {
        super.start();

        createInteractor();
        mDisplaySelectedMessageInteractor.execute();
    }

    public PointGeometryApp getPointGeometry() {
        return mPoint;
    }

    public String getText() {
        return mText;
    }

    public String getLocationType() {
        return mType;
    }

    public void goToLocation() {
        super.gotoLocationClicked(mPoint);
    }

    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

    private void createInteractor() {
        mDisplaySelectedMessageInteractor = mDisplaySelectedMessageInteractorFactory.create(
                new DisplaySelectedMessageInteractor.Displayer() {
                    @Override
                    public void display(Message message) {
                        mMessage = mMessageAppMapper.transformToModel(message);

                        GeoEntity geoEntity = ((MessageGeoApp) mMessage).getContent().getGeoEntity();
                        Entity transform = mGeoEntityTransformer.transform(geoEntity);

                        mPoint = (PointGeometryApp) transform.getGeometry();
                        mType = ((PointSymbol) geoEntity.getSymbol()).getType();
                        mText = geoEntity.getText();

                        notifyChange();
                    }
                });
    }
}
