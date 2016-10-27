package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;

/**
 * Base ViewModel for Image and Geo (Both use map related functions)
 */

public abstract class MessageBaseGeoViewModel<V> extends MessageDetailViewModel<V> {

    protected void gotoLocationClicked(PointGeometryApp point) {
        sLogger.userInteraction("goto button clicked");
//        interactor...
//        mGeoMessageListener.goToLocation(point);
    }

    protected void showPinOnMapClicked() {
        sLogger.userInteraction("show pin button clicked");
        //        interactor...
//        mViewModel.drawMessageOnMap(mGeoMessageListener);
    }

}
