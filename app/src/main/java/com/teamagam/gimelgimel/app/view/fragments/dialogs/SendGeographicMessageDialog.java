package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Sending geographical message dialog.
 * Displays coordinates to be sent with an OK/Cancel buttons.
 * On OK will send geographical message to GGMessaging server and place a pin at
 * associated geographical location.
 */
public class SendGeographicMessageDialog extends
        BaseDialogFragment<SendGeographicMessageDialog.SendGeographicMessageDialogInterface> {

    private static final String ARG_POINT_GEOMETRY = SendGeographicMessageDialog.class
            .getSimpleName() + "_PointGeometry";

    private PointGeometry mPoint;
    private String mText;

    @BindView(R.id.dialog_send_geo_message_text)
    TextView mDialogMessageTV;

    @BindView(R.id.dialog_send_geo_message_edit_text)
    EditText mEditText;

    @BindString(R.string.dialog_validation_failed_geo_text_message)
    String mGeoTextValidationError;

    @BindView(R.id.dialog_send_geo_message_geo_types)
    Spinner mGeoTypesSpinner;

    /**
     * Works the same as {@link SendGeographicMessageDialog#newInstance(PointGeometry pointGeometry,
     * Fragment targetFragment)) method
     * without settings a target fragment
     */
    public static SendGeographicMessageDialog newInstance(PointGeometry pointGeometry) {
        SendGeographicMessageDialog fragment = new SendGeographicMessageDialog();

        Bundle args = new Bundle();
        args.putParcelable(ARG_POINT_GEOMETRY, pointGeometry);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Utility for creation of {@link SendGeographicMessageDialog} with a {@link PointGeometry}
     * bundled argument (split into primitives) and containing {@link Fragment} (target-fragment)
     * to be used if necessary
     *
     * @param pointGeometry  - the point to pass as argument (as primitives)
     * @param targetFragment - the containing fragment
     * @return bundled fragment with the given {@link PointGeometry}
     */
    public static SendGeographicMessageDialog newInstance(PointGeometry pointGeometry,
                                                          Fragment targetFragment) {
        SendGeographicMessageDialog f = newInstance(pointGeometry);
        f.setTargetFragment(targetFragment, 0 /*optional*/);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            mPoint = arguments.getParcelable(ARG_POINT_GEOMETRY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_POINT_GEOMETRY, mPoint);
    }

    @Override
    protected int getTitleResId() {
        return R.string.dialog_send_message_geo_title;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_send_geo_message;
    }

    @Override
    protected String getNegativeString() {
        return getString(R.string.dialog_send_message_cancel);
    }

    @Override
    protected String getPositiveString() {
        return getString(R.string.dialog_send_message_ok);
    }

    @Override
    protected SendGeographicMessageDialogInterface castInterface(
            Activity activity) {
        return (SendGeographicMessageDialogInterface) activity;
    }

    @Override
    protected SendGeographicMessageDialogInterface castInterface(
            Fragment fragment) {
        return (SendGeographicMessageDialogInterface) fragment;
    }

    @Override
    protected void onCreateDialogLayout(View dialogView) {
        initSpinner();
        setupGeoPointDisplayText();
    }

    @Override
    protected boolean hasNegativeButton() {
        return true;
    }

    @Override
    protected boolean hasPositiveButton() {
        return true;
    }

    @Override
    protected void onPositiveClick() {
        sLogger.userInteraction("Clicked OK");

        if (isInputValid()) {
            String type = mGeoTypesSpinner.getSelectedItem().toString();
            new GGMessageSender(getActivity()).sendGeoMessageAsync(mPoint, mText, type);
            mInterface.drawSentPin(mPoint, type);
            dismiss();

        } else {
            //validate that the user has entered description
            mEditText.setError(mGeoTextValidationError);
            mEditText.requestFocus();
        }
    }

    private void initSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.geo_locations_types, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mGeoTypesSpinner.setAdapter(adapter);
    }

    private boolean isInputValid() {
        mText = mEditText.getText().toString();
        return !mText.isEmpty();
    }

    private void setupGeoPointDisplayText() {
        mDialogMessageTV.setText(
                getString(R.string.geo_dd_format, mPoint.latitude, mPoint.longitude));
    }

    /**
     * Containing activity or target fragment must implement this interface!
     * It is essential for this fragment communication
     */
    public interface SendGeographicMessageDialogInterface {

        /**
         * Draws a pin over the map
         *
         * @param pointGeometry - the geometry to draw the pin at
         */
        void drawSentPin(PointGeometry pointGeometry, String type);
    }
}
