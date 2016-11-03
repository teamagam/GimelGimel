package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;

import javax.inject.Inject;

/**
 * Base ViewModel for Image and Geo (Both use map related functions)
 */

public abstract class MessageBaseGeoViewModel<V> extends MessageDetailViewModel<V> {

    @Inject
    GoToLocationMapInteractorFactory goToLocationMapInteractorFactory;

    protected void gotoLocationClicked(PointGeometryApp point) {
        sLogger.userInteraction("goto button clicked");

        goToLocationMapInteractorFactory.create(point.getPointDomain()).execute();

    }

    protected void showPinOnMapClicked() {
        sLogger.userInteraction("show pin button clicked");
        //        interactor...
//        mViewModel.drawMessageOnMap(mGeoMessageListener);
    }

}
