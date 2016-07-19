package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;

import android.content.Context;

import com.teamagam.gimelgimel.app.model.ViewsModels.messages.MessageDetailViewModel;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * A subclass {@link MessagesDetailFragment} for managing listener for goto-button.
 */
public abstract class MessagesDetailBaseGeoFragment<VM extends
        MessageDetailViewModel> extends MessagesDetailFragment<VM>{

    protected GeoMessageInterface mGeoMessageListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GeoMessageInterface) {
            mGeoMessageListener = (GeoMessageInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GeoMessageInterface");
        }
    }

    protected void gotoLocationClicked(PointGeometry point) {
        sLogger.userInteraction("goto button clicked");
        mGeoMessageListener.goToLocation(point);
    }

    protected void showPinOnMapClicked(PointGeometry point) {
        sLogger.userInteraction("show pin button clicked");
        mGeoMessageListener.addMessageLocationPin(point);
    }

    /**
     * Containing activity or target fragment must implement this interface!
     * It is essential for this fragment communication
     */
    public interface GeoMessageInterface {

        void goToLocation(PointGeometry pointGeometry);

        void addMessageLocationPin(PointGeometry pointGeometry);
    }
}
