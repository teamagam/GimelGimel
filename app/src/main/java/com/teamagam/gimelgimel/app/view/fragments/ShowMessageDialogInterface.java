package com.teamagam.gimelgimel.app.view.fragments;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * this interface is for callback listener for the {link ShowMessageDialogFragment}
 * Created by Yoni on 3/28/2016.
 */
public interface ShowMessageDialogInterface extends BaseDialogFragment.DialogListener {
    void goToSelectedMessage(PointGeometry point);
}
