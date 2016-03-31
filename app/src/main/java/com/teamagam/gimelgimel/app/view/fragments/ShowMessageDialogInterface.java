package com.teamagam.gimelgimel.app.view.fragments;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Callback listener for the {@link ShowMessageDialogFragment}
 */
public interface ShowMessageDialogInterface extends BaseDialogFragment.DialogListener {
    void goToLocation(PointGeometry point);
}
