package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailGeoFragment;
import com.teamagam.gimelgimel.domain.map.DrawMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;

/**
 * LatLong message view-model
 */
@AutoFactory
public class GeoMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailGeoFragment> {

    private PointGeometryApp mPoint;
    private String mType;
    private String mText;
    private MessageGeoApp mMessageGeo;

    public GeoMessageDetailViewModel(
            @Provided Context context,
            @Provided GoToLocationMapInteractorFactory gotoFactory,
            @Provided DrawMessageOnMapInteractorFactory drawFactory,
            MessageApp messageApp) {
        super(context, gotoFactory, drawFactory, messageApp);
        mMessageGeo = (MessageGeoApp) messageApp;
        //TODO: resolve geo message geometry & type retrieving
        mPoint = new PointGeometryApp(31, 35, 200);
        mType = "Stub type";
        mText = "Tempish textish";
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
