package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Go-To (center map) a specified location dialog.
 * <p/>
 * Dialog has input fields for the user to fill.
 * On positive button click, the dialog "goes-to" specified location
 */
public class GoToDialogFragment
        extends BaseDialogFragment<GoToDialogFragment.GoToDialogFragmentInterface> {

    private EditText mLongitudeEditText;
    private EditText mLatitudeEditText;
    private EditText mAltitudeEditText;

    private boolean mIsLongitudeValid;
    private boolean mIsLatitudeValid;
    private boolean mIsAltitudeValid;

    public GoToDialogFragment() {
        mIsAltitudeValid = false;
        mIsLatitudeValid = false;
        mIsLongitudeValid = false;
    }

    @Override
    protected int getTitleResId() {
        return R.string.dialog_go_to_title;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.dialog_go_to;
    }

    @Override
    protected void onCreateDialogLayout(View dialogView) {
        mAltitudeEditText = (EditText) dialogView.findViewById(
                R.id.dialog_go_to_altitude_text_view);
        mLongitudeEditText = (EditText) dialogView.findViewById(
                R.id.dialog_go_to_longitude_text_view);
        mLatitudeEditText = (EditText) dialogView.findViewById(
                R.id.dialog_go_to_latitude_text_view);
    }

    @Override
    protected GoToDialogFragmentInterface castInterface(Activity activity) {
        return (GoToDialogFragmentInterface) activity;
    }

    @Override
    protected GoToDialogFragmentInterface castInterface(Fragment fragment) {
        return (GoToDialogFragmentInterface) fragment;
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
    protected String getPositiveString() {
        return getString(R.string.dialog_go_to_positive_button);
    }

    @Override
    protected String getNegativeString() {
        return getString(R.string.dialog_go_to_negative_button);
    }

    @Override
    protected void onPositiveClick() {
        updateInputValidationState();

        if (!isUserInputValid()) {
            showErrors();
            return;
        }

        PointGeometry pointGeometry = getPointGeometry();
        mInterface.goToLocation(pointGeometry);

        super.onPositiveClick();
    }

    @NonNull
    private PointGeometry getPointGeometry() {
        double latitude = Double.parseDouble(mLatitudeEditText.getText().toString());
        double longitude = Double.parseDouble(mLongitudeEditText.getText().toString());
        String altitudeString = mAltitudeEditText.getText().toString();
        double altitude = 0;
        if (!altitudeString.isEmpty()) {
            altitude = Double.parseDouble(altitudeString);
        }

        return new PointGeometry(latitude, longitude, altitude);
    }

    private void updateInputValidationState() {
        mIsLatitudeValid = isNumericString(mLatitudeEditText.getText().toString());

        mIsLongitudeValid = isNumericString(mLongitudeEditText.getText().toString());

        String altitudeString = mAltitudeEditText.getText().toString();
        mIsAltitudeValid = altitudeString.isEmpty() || isNumericString(altitudeString);
    }

    /**
     * Displays errors to user
     */
    private void showErrors() {

        if (!mIsLatitudeValid) {
            showError(mLatitudeEditText);
        }
        if (!mIsLongitudeValid) {
            showError(mLongitudeEditText);
        }
        if (!mIsAltitudeValid) {
            showError(mAltitudeEditText);
        }
    }

    private void showError(EditText editText) {
        editText.setError(getString(R.string.dialog_go_to_invalid_input));

        //error will be shown only when view is in focus state.
        editText.requestFocus();
    }

    private boolean isUserInputValid() {
        return mIsAltitudeValid && mIsLongitudeValid && mIsLatitudeValid;
    }

    private static boolean isNumericString(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public interface GoToDialogFragmentInterface {
        void goToLocation(PointGeometry pointGeometry);
    }
}
