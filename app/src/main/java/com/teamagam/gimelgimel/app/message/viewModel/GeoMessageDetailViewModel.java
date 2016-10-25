package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.message.model.MessageGeoModel;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailGeoFragment;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import javax.inject.Inject;

/**
 * LatLong message view-model
 */
public class GeoMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailGeoFragment> {

    @Inject
    public GeoMessageDetailViewModel() {
        super();
    }

    public PointGeometry getPointGeometry() {
        return getSelectedMessagePointGeometry();
    }

    @Override
    protected String getExpectedMessageType() {
        return Message.GEO;
    }

    private PointGeometry getSelectedMessagePointGeometry() {
        return ((MessageGeoModel) mMessageSelected).getContent().getPointGeometry();
    }

    public String getText() {
        return ((MessageGeoModel) mMessageSelected).getContent().getText();
    }

    public String getLocationType() {
        return ((MessageGeoModel) mMessageSelected).getContent().getType();
    }

    public void goToLocation(){
        super.gotoLocationClicked(getPointGeometry());
    }

    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

}
