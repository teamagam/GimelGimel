package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailGeoFragment;
import com.teamagam.gimelgimel.domain.map.DrawMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;

/**
 * LatLong message view-model
 */
@AutoFactory
public class GeoMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailGeoFragment> {

    private PointGeometryApp mPoint;
    private String mType;
    private String mText;

    public GeoMessageDetailViewModel(
            @Provided Context context,
            @Provided GoToLocationMapInteractorFactory gotoFactory,
            @Provided DrawMessageOnMapInteractorFactory drawFactory,
            @Provided GeoEntityTransformer transformer,
            MessageApp messageApp) {
        super(context, gotoFactory, drawFactory, messageApp);

        GeoEntityTransformer mTransformer = transformer;
        GeoEntity geoEntity = ((MessageGeoApp) messageApp).getContent().getGeoEntity();
        Entity transform = mTransformer.transform(geoEntity);

        mPoint = (PointGeometryApp) transform.getGeometry();
        mType = ((PointSymbol) geoEntity.getSymbol()).getType();
        mText = geoEntity.getText();
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
}
