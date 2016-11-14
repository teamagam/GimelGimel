package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.map.model.entities.Point;
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

    public String getText() {
        if(mEntity != null) {
            return ((Point) mEntity).getText();
        } else {
            return null;
        }

    }

    public String getLocationType() {
        if(mEntity != null) {
            return ((Point) mEntity).getTypeString();
        } else {
            return null;
        }
    }

    public void goToLocation() {
        super.gotoLocationClicked(getPointGeometry());
    }

    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

    @Override
    protected String getEntityId() {
        return  ((MessageGeoApp) mMessageSelected).getContent().getEntityId();
    }

    @Override
    protected String getExpectedMessageType() {
        return MessageApp.GEO;
    }

}
