package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.map.DrawMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;

/**
 * Base ViewModel for Image and Geo (Both use map related functions)
 */

public abstract class MessageBaseGeoViewModel<V> extends MessageDetailViewModel<V> {

    private GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
    private DrawMessageOnMapInteractorFactory mDrawMessageOnMapInteractorFactory;

    public MessageBaseGeoViewModel(Context context,
                                   DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory,
                                   GoToLocationMapInteractorFactory gotoFactory,
                                   DrawMessageOnMapInteractorFactory drawMessageOnMapInteractorFactory) {
        super(context, selectedMessageInteractorFactory);
        mGoToLocationMapInteractorFactory = gotoFactory;
        mDrawMessageOnMapInteractorFactory = drawMessageOnMapInteractorFactory;
    }

    protected void gotoLocationClicked(PointGeometryApp point) {
        sLogger.userInteraction("goto button clicked");

        mGoToLocationMapInteractorFactory.create(point.getPointDomain()).execute();
    }

    protected void showPinOnMapClicked() {
        sLogger.userInteraction("show pin button clicked");
        mDrawMessageOnMapInteractorFactory.create(mMessage.getMessageId()).execute();
    }
}
