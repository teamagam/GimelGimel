package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.entities.LocationEntity;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private GGMessageSender mMessageSender;
    @BindView(R.id.dialog_send_message_edit_text)
    EditText editText;

    /**
     * Works the same as {@link SendGeographicMessageDialog#newInstance(PointGeometry pointGeometry
            Fragment targetFragment)
     * without settings a target fragment
     *
     * @see SendGeographicMessageDialog#newInstance(PointGeometry pointGeometry,
            String pointText,
            Fragment targetFragment)
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
    @NotNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMessageSender = new GGMessageSender(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            mPoint = arguments.getParcelable(ARG_POINT_GEOMETRY);
            mText = arguments.getString(ARG_POINT_TEXT);
        }
    }

    @Override
    protected int getTitleResId() {
        return R.string.dialog_send_message_geo_title;
    }

    @Override
    protected int getFragmentLayout() {
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
        setupGeoPointDisplayText(dialogView);
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

        EditText editText = (EditText) getDialog().findViewById(R.id.dialog_send_message_edit_text);
        mText = editText.getText().toString();
        if (mText.isEmpty()) {
            //validate that the user has entered description
            editText.setError("Please enter location dexcription");
            return;
        }
        mMessageSender.sendGeoMessageAsync(mPoint, mText, LocationEntity.REGULAR);
        mInterface.drawPin(mPoint);

        dismiss();
    }

    private void setupGeoPointDisplayText(View dialogView) {
        TextView mDialogMessageTV = (TextView) dialogView.findViewById(
                R.id.dialog_send_geo_message_text);
        mDialogMessageTV.setText(
                getString(R.string.dialog_goto_show_geo, mPoint.latitude, mPoint.longitude));
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
        void drawPin(PointGeometry pointGeometry);
    }
}
