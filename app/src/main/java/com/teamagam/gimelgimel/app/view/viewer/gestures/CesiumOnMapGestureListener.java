package com.teamagam.gimelgimel.app.view.viewer.gestures;

import android.app.Fragment;
import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.view.viewer.GGMap;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Created by CV on 6/20/2016.
 */
public class CesiumOnMapGestureListener implements OnMapGestureListener {

    private static final float ZOOM_LEVEL_FACTOR = 0.5f;

    private Fragment mFragment;
    private GGMap mGGmap;

    public CesiumOnMapGestureListener(Fragment fragment, GGMap ggMap) {
        mFragment = fragment;
        mGGmap = ggMap;
    }

    @Override
    public void onDown(PointGeometry pointGeometry) {

    }

    @Override
    public void onShowPress(PointGeometry pointGeometry) {

    }

    @Override
    public void onSingleTapUp(PointGeometry pointGeometry) {

    }

    @Override
    public void onLongPress(PointGeometry pointGeometry) {
        /** create send geo message dialog **/
        onCreateGeographicMessage(pointGeometry);
    }

    @Override
    public void onDoubleTap(final PointGeometry pointGeometry) {
        // Read the current position of the camera, and get the height
        mGGmap.readAsyncCenterPosition(new ValueCallback<PointGeometry>() {
            @Override
            public void onReceiveValue(PointGeometry value) {
                mGGmap.zoomTo(
                        (float) pointGeometry.longitude,
                        (float) pointGeometry.latitude,
                        (float) value.altitude * ZOOM_LEVEL_FACTOR
                );
            }
        });
    }

    private void onCreateGeographicMessage(PointGeometry pointGeometry) {
        SendGeographicMessageDialog sendGeographicMessageDialogFragment =
                SendGeographicMessageDialog.newInstance(pointGeometry, mFragment);

        sendGeographicMessageDialogFragment.show(mFragment.getFragmentManager(), "sendCoordinatesDialog");
    }
}
