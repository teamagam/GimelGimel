package com.teamagam.gimelgimel.app.view.viewer;

import android.app.Fragment;

import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.app.view.viewer.gestures.SimpleOnMapGestureListener;

public class GGMapGestureListener extends SimpleOnMapGestureListener
        implements OnMapGestureListener
        {

    private static final Logger sLogger = LoggerFactory.create(GGMapGestureListener.class);

    private Fragment mFragment;
    private GGMap mGGMap;

    public GGMapGestureListener(Fragment fragment, GGMap ggMap) {
        mFragment = fragment;
        mGGMap = ggMap;
    }
    
    @Override
    public void onLongPress(PointGeometry pointGeometry) {
        /** create send geo message dialog **/
        sLogger.userInteraction("long-press");
        openSendGeographicMessageDialog(pointGeometry);
    }

    @Override
    public void onDoubleTap(final PointGeometry pointGeometry) {
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
