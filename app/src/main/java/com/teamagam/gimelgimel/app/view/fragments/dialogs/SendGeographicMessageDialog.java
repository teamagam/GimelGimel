package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageContent;
import com.teamagam.gimelgimel.app.network.services.GGMessagingUtils;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Sending geographical message dialog.
 * Displays coordinates to be sent with an OK/Cancel buttons.
 * On OK will send geographical message to GGMessaging server and place a pin at
 * associated geographical location.
 */
public class SendGeographicMessageDialog extends
        BaseDialogFragment<SendGeographicMessageDialog.SendGeographicMessageDialogInterface> {

    private static final String ARG_POINT_GEOMETRY_ALT =
            SendGeographicMessageDialog.class.getSimpleName() + "_PointGeometry_Altitude";
    private static final String ARG_POINT_GEOMETRY_LAT =
            SendGeographicMessageDialog.class.getSimpleName() + "_PointGeometry_Latitude";
    private static final String ARG_POINT_GEOMETRY_LONG =
            SendGeographicMessageDialog.class.getSimpleName() + "_PointGeometry_Longitude";

    private TextView mDialogMessageTV;

    private PointGeometry mPoint;

    /**
     * Works the same as {@link SendGeographicMessageDialog#newInstance(PointGeometry, Fragment)}
     * without settings a target fragment
     *
     * @see SendGeographicMessageDialog#newInstance(PointGeometry, Fragment)
     */
    public static SendGeographicMessageDialog newInstance(PointGeometry pointGeometry) {
        SendGeographicMessageDialog fragment = new SendGeographicMessageDialog();
        Bundle args = new Bundle();
        args.putDouble(ARG_POINT_GEOMETRY_LAT, pointGeometry.latitude);
        args.putDouble(ARG_POINT_GEOMETRY_LONG, pointGeometry.longitude);
        args.putDouble(ARG_POINT_GEOMETRY_ALT, pointGeometry.altitude);

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

            double latitude = arguments.getDouble(ARG_POINT_GEOMETRY_LAT);
            double longitude = arguments.getDouble(ARG_POINT_GEOMETRY_LONG);
            double altitude = arguments.getDouble(ARG_POINT_GEOMETRY_ALT);
            mPoint = new PointGeometry(latitude, longitude, altitude);
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
        mDialogMessageTV = (TextView) dialogView.findViewById(R.id.dialog_send_geo_message_text);
        mDialogMessageTV.setText(
                getString(R.string.fragment_show_geo, mPoint.latitude, mPoint.longitude));
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
        GGMessagingUtils.sendLatLongMessageAsync(mPoint);

        mListener.drawPin(mPoint);

        dismiss();
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
