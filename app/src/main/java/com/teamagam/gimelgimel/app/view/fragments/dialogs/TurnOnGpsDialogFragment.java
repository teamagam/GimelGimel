package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.teamagam.gimelgimel.R;

/**
 * Represents an alert dialog that shows when the GPS is off
 */
public class TurnOnGpsDialogFragment extends AlertDialog {

    private Context mContext;

    public TurnOnGpsDialogFragment(Context context) {
        super(context);

        mContext = context;

        setTitle(R.string.gps_off_title);
        setMessage(mContext.getString(R.string.gps_off_message));

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        // Set positive button (Yes)
        setButton(AlertDialog.BUTTON_POSITIVE, mContext.getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                mContext.startActivity(settingsIntent);
            }
        });

        setButton(AlertDialog.BUTTON_NEGATIVE, mContext.getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }
}
