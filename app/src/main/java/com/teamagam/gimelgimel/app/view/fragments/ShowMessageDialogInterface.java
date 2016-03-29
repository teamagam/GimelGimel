package com.teamagam.gimelgimel.app.view.fragments;

import android.app.DialogFragment;

/**
 * this interface is for callback listener for the {link ShowMessageDialogFragment}
 * Created by Yoni on 3/28/2016.
 */
public interface ShowMessageDialogInterface extends BaseDialogFragment.DialogListener {
    void onShowMessageDialogClick(DialogFragment dialog, int which);
}
