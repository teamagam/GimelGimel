package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;

/**
 * Base ViewModel for Image and Geo (Both use map related functions)
 */

public abstract class MessageBaseGeoViewModel<V> extends MessageDetailViewModel<V> {

    private GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;

    public MessageBaseGeoViewModel(Context context, GoToLocationMapInteractorFactory gotoFactory,
                                   MessageApp messageApp) {
        super(context, messageApp);
        mGoToLocationMapInteractorFactory = gotoFactory;
    }

    protected void gotoLocationClicked(PointGeometryApp point) {
        sLogger.userInteraction("goto button clicked");

        mGoToLocationMapInteractorFactory.create(point.getPointDomain()).execute();
    }

    protected void showPinOnMapClicked() {
        sLogger.userInteraction("show pin button clicked");
    }
}
