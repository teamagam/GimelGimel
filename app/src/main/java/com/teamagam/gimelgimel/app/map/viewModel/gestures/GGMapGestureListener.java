package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import android.app.Fragment;

import com.teamagam.gimelgimel.app.map.view.GGMap;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.message.view.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

public class GGMapGestureListener extends SimpleOnMapGestureListener {

    private static final Logger sLogger = LoggerFactory.create(GGMapGestureListener.class);

    private Fragment mFragment;
    private GGMap mGGMap;

    public GGMapGestureListener(Fragment fragment, GGMap ggMap) {
        mFragment = fragment;
        mGGMap = ggMap;
    }
    
    @Override
    public void onLocationChosen(PointGeometry pointGeometry) {
        /** create send geo message dialog **/
        sLogger.userInteraction("long-press");
        openSendGeographicMessageDialog(pointGeometry);
    }

    @Override
    public void onZoomRequested(final PointGeometry pointGeometry) {
        sLogger.userInteraction("double-tap");
        zoomInTo(pointGeometry);
    }

    private void zoomInTo(PointGeometry pointGeometry) {
        pointGeometry.altitude = getCurrentHeight() * Constants.ZOOM_IN_FACTOR;
        mGGMap.zoomTo(pointGeometry);
    }

    private double getCurrentHeight() {
        return mGGMap.getLastViewedLocation().altitude;
    }

    private void openSendGeographicMessageDialog(PointGeometry pointGeometry) {
        SendGeographicMessageDialog sendGeographicMessageDialogFragment =
                SendGeographicMessageDialog.newInstance(pointGeometry, mFragment);

        sendGeographicMessageDialogFragment.show(mFragment.getFragmentManager(), "sendCoordinatesDialog");
    }
}
