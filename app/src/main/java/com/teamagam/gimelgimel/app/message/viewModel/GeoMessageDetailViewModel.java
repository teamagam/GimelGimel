package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailGeoFragment;

import javax.inject.Inject;

/**
 * LatLong message view-model
 */
public class GeoMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailGeoFragment> {

    @Inject
    public GeoMessageDetailViewModel() {
        super();
    }

    public PointGeometryApp getPointGeometry() {
        return getSelectedMessagePointGeometry();
    }

    @Override
    protected String getExpectedMessageType() {
        return MessageApp.GEO;
    }

    private PointGeometryApp getSelectedMessagePointGeometry() {
        return ((MessageGeoApp) mMessageSelected).getContent().getPointGeometry();
    }

    public String getText() {
        return ((MessageGeoApp) mMessageSelected).getContent().getText();
    }

    public String getLocationType() {
        return ((MessageGeoApp) mMessageSelected).getContent().getType();
    }

    public void goToLocation(){
        super.gotoLocationClicked(getPointGeometry());
    }

    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

}
