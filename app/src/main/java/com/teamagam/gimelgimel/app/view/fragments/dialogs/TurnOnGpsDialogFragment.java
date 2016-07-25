package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;

/**
 * Notifies no gps available message and opens Android's settings to enable (turn-on) GPS
 */
public class TurnOnGpsDialogFragment extends BaseDialogFragment {

    @Override
    protected void onPositiveClick() {
        startSettingsActivity();
        super.onPositiveClick();
    }

    @Override
    protected String getPositiveString() {
        return getActivity().getString(R.string.dialog_yes);
    }

    @Override
    protected String getNegativeString() {
        return getActivity().getString(R.string.dialog_no);
    }

    @Override
    protected int getTitleResId() {
        return R.string.gps_off_title;
    }

    @Override
    protected int getMessageResId() {
        return R.string.gps_off_message;
    }

    @Override
    protected boolean hasPositiveButton() {
        return true;
    }

    @Override
    protected boolean hasNegativeButton() {
        return true;
    }

    @Override
    protected Object castInterface(Activity activity) {
        return activity;
    }

    @Override
    protected Object castInterface(Fragment fragment) {
        return fragment;
    }

    private void startSettingsActivity() {
        Intent settingsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        startActivity(settingsIntent);
    }
}
