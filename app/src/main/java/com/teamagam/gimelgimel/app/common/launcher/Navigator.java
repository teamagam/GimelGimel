package com.teamagam.gimelgimel.app.common.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.view.ImageFullscreenActivity;
import com.teamagam.gimelgimel.app.message.view.SendGeographicMessageDialog;

import javax.inject.Inject;

/**
 * Navigator for creating and navigating between views.
 */

public class Navigator {

    private final Context mContext;

    @Inject
    public Navigator(Context context) {
        mContext = context;
    }

    /**
     * Opens the full image view (activity) {@link ImageFullscreenActivity}.
     * <p>
     * the context is always the application's context.
     *
     * @param imageUri
     * @param activityContext from the backstack.
     */
    public void navigateToFullScreenImage(Activity activityContext, Uri imageUri) {
        Intent intentToLaunch = ImageFullscreenActivity.getCallingIntent(activityContext);
        intentToLaunch.setData(imageUri);
        intentToLaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityContext.startActivity(intentToLaunch);
    }


    public void navigateToSendGeoMessage(PointGeometryApp pointGeometry, Activity ActivityContext) {
        SendGeographicMessageDialog.newInstance(pointGeometry)
                .show(ActivityContext.getFragmentManager(), "sendCoordinatesDialog");
    }
}
