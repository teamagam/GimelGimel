package com.teamagam.gimelgimel.app.message.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.message.viewModel.SendGeoMessageViewModel;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Sending geographical message dialog.
 * Displays coordinates to be sent with an OK/Cancel buttons.
 * On OK will send geographical message to GGMessaging server and place a pin at
 * associated geographical location.
 */
public class SendGeographicMessageDialog extends BaseDialogFragment {

    private static final String ARG_POINT_GEOMETRY = SendGeographicMessageDialog.class
            .getSimpleName() + "_PointGeometry";

    private String mText;

    @BindView(R.id.dialog_send_geo_message_text)
    TextView mDialogMessageTV;

    @BindView(R.id.dialog_send_geo_message_edit_text)
    EditText mEditText;

    @BindString(R.string.dialog_validation_failed_geo_text_message)
    String mGeoTextValidationError;

    @BindView(R.id.dialog_send_geo_message_geo_types)
    Spinner mGeoTypesSpinner;

    private AdapterView.OnItemSelectedListener mSpinnerItemSelectedLogger;

    @Inject
    SendGeoMessageViewModel mViewModel;

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
    public void onResume() {
        super.onResume();
        mGeoTypesSpinner.setOnItemSelectedListener(mSpinnerItemSelectedLogger);
    }

    @Override
    public void onPause() {
        super.onPause();
        mGeoTypesSpinner.setOnItemSelectedListener(null);
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
    protected Activity castInterface(Activity activity) {
        return activity;
    }

    @Override
    protected Fragment castInterface(Fragment fragment) {
        return fragment;
    }

    @Override
    protected void onCreateDialogLayout(View dialogView) {
        initSpinner();
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
        mViewModel.clickedOK();
    }

    private void initSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.geo_locations_types, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mGeoTypesSpinner.setAdapter(adapter);

        mSpinnerItemSelectedLogger = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = (String) mGeoTypesSpinner.getItemAtPosition(position);
                sLogger.userInteraction("Selected message geo-type " + type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }
}
