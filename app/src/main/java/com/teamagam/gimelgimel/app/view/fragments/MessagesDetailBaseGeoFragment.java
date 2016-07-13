package com.teamagam.gimelgimel.app.view.fragments;

import android.content.Context;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Created on 7/13/2016.
 * TODO: complete text
 */
public abstract class MessagesDetailBaseGeoFragment extends MessagesDetailFragment{

    protected GeoMessageInterface mGeoMessageListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GeoMessageInterface) {
            mGeoMessageListener = (GeoMessageInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void gotoLocationClicked(PointGeometry point) {
        sLogger.userInteraction("goto button clicked");
        mGeoMessageListener.goToLocation(point);
    }

    /**
     * Containing activity or target fragment must implement this interface!
     * It is essential for this fragment communication
     */
    public interface GeoMessageInterface {
        void goToLocation(PointGeometry pointGeometry);
    }
}
