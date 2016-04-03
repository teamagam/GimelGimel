package com.teamagam.gimelgimel.app.view.fragments;

import android.app.DialogFragment;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Created by Gil.Raytan on 30-Mar-16.
 */
public interface SendGeographicMessageDialogInterface extends BaseDialogFragment.DialogListener {
    void onSendGeographicMessageDialogClick(PointGeometry point);
}
