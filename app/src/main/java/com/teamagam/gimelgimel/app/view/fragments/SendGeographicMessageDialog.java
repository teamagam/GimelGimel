package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Gil.Raytan on 30-Mar-16.
 */
public class SendGeographicMessageDialog extends BaseDialogFragment<SendGeographicMessageDialogInterface> {

    private static final Object mLock = new Object();
    private static ShowMessageDialogFragment mDialogFragment = null;
    private Queue<Message> mMessages = new LinkedList<>();
    private TextView mGeoMessage;
    private Button mPositiveButton;
    private boolean mIsShown;

    public PointGeometry getPoint() {
        return mPoint;
    }

    private PointGeometry mPoint;

    public SendGeographicMessageDialog(PointGeometry point)
    {
        mPoint = point;
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
        mGeoMessage = (TextView) dialogView.findViewById(R.id.dialog_send_geo_message_text);
        mGeoMessage.setText("The coordinates you are sending are "+ mPoint.longitude+":"+ mPoint.longitude);
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
