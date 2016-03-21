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
 * Created by Gil.Raytan on 21-Mar-16.
 */
public class SendMessageDialogFragment extends DialogFragment {
    // Use this instance of the interface to deliver action events
    SendMessageDialogListener mListener;
    EditText mSendMessageEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        View dialogView = inflater.inflate(R.layout.dialog_send_message, null);
        mSendMessageEditText = (EditText) dialogView.findViewById(R.id.dialog_send_message_edit_text);

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle(R.string.dialog_send_message_title)
                .setPositiveButton(R.string.dialog_send_message_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        String message = mSendMessageEditText.getText().toString();
                        if (message.isEmpty()) {
                            //todo: check for not exmpty
                        }
                        else {
                            mListener.onSendMessageDialogPositiveClick(SendMessageDialogFragment.this, message);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_send_message_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onSendMessageDialogNegativeClick(SendMessageDialogFragment.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
    //TODO: consider injecting map-goto capability instead of using event listener
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface SendMessageDialogListener {

        void onSendMessageDialogPositiveClick(DialogFragment dialog, String text);

        void onSendMessageDialogNegativeClick(DialogFragment dialog);
    }


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verify that the host **activity** implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SendMessageDialogListener) activity;
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
            mListener = (SendMessageDialogListener) getTargetFragment();
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
