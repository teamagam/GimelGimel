package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
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

import javax.inject.Inject;

/**
 * LatLong message view-model
 */
public class GeoMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailGeoFragment> {

    private GeoEntityTransformer mGeoEntityTransformer;
    private MessageAppMapper mMessageAppMapper;
    private PointGeometryApp mPoint;
    private MessageGeoApp mMessage;
    private String mType;
    private String mText;

    @Inject
    public GeoMessageDetailViewModel(
            Context context,
            DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory,
            GoToLocationMapInteractorFactory gotoFactory,
            DrawMessageOnMapInteractorFactory drawFactory,
            GeoEntityTransformer geoEntityTransformer,
            MessageAppMapper messageAppMapper) {
        super(context, selectedMessageInteractorFactory, gotoFactory, drawFactory);

        mGeoEntityTransformer = geoEntityTransformer;
        mMessageAppMapper = messageAppMapper;
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

    @Override
    protected MessageApp getMessage() {
        return mMessage;
    }

    @Override
    protected DisplaySelectedMessageInteractor.Displayer createDisplayer() {
        return new DisplaySelectedMessageInteractor.Displayer() {
            @Override
            public void display(Message message) {
                mMessage = (MessageGeoApp) mMessageAppMapper.transformToModel(message);

                GeoEntity geoEntity = mMessage.getContent().getGeoEntity();
                Entity transform = mGeoEntityTransformer.transform(geoEntity);

                mPoint = (PointGeometryApp) transform.getGeometry();
                mType = ((PointSymbol) geoEntity.getSymbol()).getType();
                mText = geoEntity.getText();

                notifyChange();
            }
        };
    }
}
