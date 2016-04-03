package com.teamagam.gimelgimel.app.view.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageContent;
import com.teamagam.gimelgimel.app.network.services.GGMessagingUtils;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Sending geographical message dialog.
 * Displays coordinates to be sent with an OK/Cancel buttons.
 * On OK will send geographical message to GGMessaging server.
 */
public class SendGeographicMessageDialog extends BaseDialogFragment<SendGeographicMessageDialogInterface> {

    private static final String ARG_POINT_GEOMETRY_ALT =
            SendGeographicMessageDialog.class.getSimpleName() + "_PointGeometry_Altitude";
    private static final String ARG_POINT_GEOMETRY_LAT =
            SendGeographicMessageDialog.class.getSimpleName() + "_PointGeometry_Latitude";
    private static final String ARG_POINT_GEOMETRY_LONG =
            SendGeographicMessageDialog.class.getSimpleName() + "_PointGeometry_Longitude";

    private TextView mDialogMessageTV;

    private PointGeometry mPoint;

    /**
     * Utility for creation of {@link SendGeographicMessageDialog} with a {@link PointGeometry}
     * bundled argument (split into primitives).
     *
     * @param pointGeometry - the point to pass as argument (as primitives)
     * @return bundled fragment with the given {@link PointGeometry}
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

        if (mPositiveCallback == null) {
            setPositiveCallback(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String senderId = NetworkUtil.getMac();
                    MessageContent content = new MessageContent(mPoint);
                    Message messageToSend = new Message(senderId, content, Message.LAT_LONG);
                    GGMessagingUtils.sendMessageAsync(messageToSend);
                }
            });
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
    protected void onCreateDialogLayout(View dialogView) {
        mDialogMessageTV = (TextView) dialogView.findViewById(R.id.dialog_send_geo_message_text);
        //TODO: use a resource string to display and format coordinates
        mDialogMessageTV.setText(
                "The coordinates you are sending are " + mPoint.longitude + ":" + mPoint.longitude);
    }

    @Override
    protected boolean hasNegativeButton() {
        return true;
    }

    @Override
    protected boolean hasPositiveButton() {
        return true;
    }
}
