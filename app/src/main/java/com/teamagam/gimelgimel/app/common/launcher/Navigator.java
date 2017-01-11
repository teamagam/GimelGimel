package com.teamagam.gimelgimel.app.common.launcher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.teamagam.gimelgimel.app.location.TurnOnGpsDialogFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityAlerts;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.view.ImageFullscreenActivity;
import com.teamagam.gimelgimel.app.message.view.SendGeographicMessageDialog;

/**
 * Navigator for creating and navigating between views.
 */

public class Navigator {

    private static final String TAG_FRAGMENT_TURN_ON_GPS_DIALOG =
            MainActivityAlerts.class.getSimpleName() + "TURN_ON_GPS";

    /**
     * Opens the full image view (activity) {@link ImageFullscreenActivity}.
     * <p>
     * the context is always the application's context.
     *
     * @param imageUri
     * @param activityContext from the backstack.
     */
    public static void navigateToFullScreenImage(Activity activityContext, Uri imageUri) {
        Intent intentToLaunch = ImageFullscreenActivity.getCallingIntent(activityContext);
        intentToLaunch.setData(imageUri);
        intentToLaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityContext.startActivity(intentToLaunch);
    }


    public static void navigateToSendGeoMessage(PointGeometryApp pointGeometry, Activity ActivityContext) {
        SendGeographicMessageDialog.newInstance(pointGeometry)
                .show(ActivityContext.getFragmentManager(), "sendCoordinatesDialog");
    }

    public static void navigateToTurnOnGPSDialog(Activity activityContext) {
        TurnOnGpsDialogFragment dialogFragment = new TurnOnGpsDialogFragment();
        dialogFragment.show(activityContext.getFragmentManager(), TAG_FRAGMENT_TURN_ON_GPS_DIALOG);
    }

}
