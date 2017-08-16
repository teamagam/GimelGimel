package com.teamagam.gimelgimel.app.common.launcher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.teamagam.gimelgimel.app.icons.IconSelectionDialogFragment;
import com.teamagam.gimelgimel.app.location.GoToLocationDialogFragment;
import com.teamagam.gimelgimel.app.location.TurnOnGpsDialogFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityConnectivityAlerts;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;
import com.teamagam.gimelgimel.app.message.view.ImageFullscreenActivity;
import com.teamagam.gimelgimel.app.message.view.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.settings.SettingsActivity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import javax.inject.Inject;

/**
 * Navigator for creating and navigating between views.
 */
public class Navigator {

  private static final String TAG_FRAGMENT_TURN_ON_GPS_DIALOG =
      MainActivityConnectivityAlerts.class.getSimpleName() + "TURN_ON_GPS";

  private final Activity mActivity;

  @Inject
  public Navigator(Activity activity) {
    mActivity = activity;
  }

  public void openGoToDialog() {
    GoToLocationDialogFragment.newInstance().show(mActivity.getFragmentManager(), "gotodialogtag");
  }

  /**
   * Opens the full image view (activity) {@link ImageFullscreenActivity}.
   * <p>
   * the context is always the application's context.
   */
  public void navigateToFullScreenImage(Uri imageUri) {
    Intent intentToLaunch = ImageFullscreenActivity.getCallingIntent(mActivity);
    intentToLaunch.setData(imageUri);
    intentToLaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    mActivity.startActivity(intentToLaunch);
  }

  public void navigateToSendGeoMessage(PointGeometry pointGeometry) {
    SendGeographicMessageDialog.newInstance(pointGeometry)
        .show(mActivity.getFragmentManager(), "sendCoordinatesDialog");
  }

  public void navigateToTurnOnGPSDialog() {
    TurnOnGpsDialogFragment dialogFragment = new TurnOnGpsDialogFragment();
    dialogFragment.show(mActivity.getFragmentManager(), TAG_FRAGMENT_TURN_ON_GPS_DIALOG);
  }

  public void openSettingsActivity() {
    mActivity.startActivity(new Intent(mActivity, SettingsActivity.class));
  }

  public void openSendQuadrilateralAction() {
    DrawActionActivity.startSendQuadAction(mActivity);
  }

  public void openMeasureDistanceAction() {
    DrawActionActivity.startMeasureAction(mActivity);
  }

  public void openSendGeometryAction() {
    DrawActionActivity.startSendGeometryAction(mActivity);
  }

  public void openDynamicLayerEditAction(DynamicLayer dynamicLayer) {
    DrawActionActivity.startDynamicLayerEditAction(mActivity, dynamicLayer);
  }

  public void openFreeDrawAction() {
    DrawActionActivity.startFreeDrawAction(mActivity);
  }

  public void openTimeplayAction() {
    DrawActionActivity.startTimeplayAction(mActivity);
  }

  public void openIconSelectionDialog() {
    IconSelectionDialogFragment.show(mActivity.getFragmentManager());
  }
}
