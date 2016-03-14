package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.teamagam.gimelgimel.R;

/**
 * Created by Yoni on 3/9/2016.
 */
public class GoToDialogFragment extends DialogFragment {

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    //TODO: change member names to conventions
    EditText editTextX;
    EditText editTextY;
    EditText editTextZ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        View dialogView = inflater.inflate(R.layout.dialog_input_position, null);
        editTextX = (EditText) dialogView.findViewById(R.id.dialog_longitude);
        editTextY = (EditText) dialogView.findViewById(R.id.dialog_latitude);
        editTextZ = (EditText) dialogView.findViewById(R.id.dialog_altitude);

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_position_massage)
                .setTitle(R.string.dialog_position_title)
                .setPositiveButton(R.string.dialog_position_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        float x = Float.parseFloat(editTextX.getText().toString());
                        float y = Float.parseFloat(editTextY.getText().toString());
                        float z;
                        if (editTextZ.getText().toString().isEmpty())
                            z = -1;
                        else
                            z = Float.parseFloat(editTextZ.getText().toString());
                        mListener.onPositionDialogPositiveClick(GoToDialogFragment.this, x, y, z);
                    }
                })
                .setNegativeButton(R.string.dialog_position_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onPositionDialogNegativeClick(GoToDialogFragment.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
    //TODO: consider injecting map-goto capability instead of using event listener
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onPositionDialogPositiveClick(DialogFragment dialog, float x, float y, float z);

        void onPositionDialogNegativeClick(DialogFragment dialog);
    }


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        // Verify that the host **activity** implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, who knows? maybe the fragment will.
            mListener = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener != null) {
            return;
        }
        // if the activity doesn't implement callback then the target fragment should.
//        // Verify that the host **fragment** implements the callback interface
        try {
            mListener = (NoticeDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
