package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * The activity that creates an instance of this dialog fragment may
 * implement it's interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it.
 */
public abstract class BaseDialogFragment<I extends BaseDialogFragment.DialogListener> extends DialogFragment {

    protected String LOG_TAG = this.getClass().getSimpleName();
    protected AlertDialog mDialog;

    protected DialogInterface.OnClickListener mPositiveCallback = null;
    protected DialogInterface.OnClickListener mNegativeCallback = null;
    protected DialogInterface.OnClickListener mNeutralCallback = null;

    // Use this instance of the interface to deliver action events
    protected I mListener;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        View dialogView = inflater.inflate(getFragmentLayout(), null);

        //private builder for the dialogs
        onCreateDialogLayout(dialogView);

        // 2. Chain together variresous setter methods to set the dialog characteristics
        builder.setTitle(getTitleResId());

        if (getMessageResId() != -1) {
            builder.setMessage(getMessageResId());
        }

        if (hasPositiveButton()) {
            builder.setPositiveButton(getPositiveString(), mPositiveCallback);
        }
        if (hasNegativeButton()) {
            builder.setNegativeButton(getNegativeString(), mNegativeCallback);
        }
        if (hasNeutralButton()) {
            builder.setNeutralButton(getNeutralString(), mNeutralCallback);
        }


        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView);

        // Create the AlertDialog object and return it
        mDialog = builder.create();

        return mDialog;
    }

    public void setPositiveCallback(DialogInterface.OnClickListener callback) {
        mPositiveCallback = callback;
    }

    public void setNegativeCallback(DialogInterface.OnClickListener callback) {
        mNegativeCallback = callback;
    }

    public void setNeutralCallback(DialogInterface.OnClickListener callback) {
        mNeutralCallback = callback;
    }

    protected abstract int getTitleResId();

    /**
     * override this method to inject text in the dialog's message.
     *
     * @return string resource id.
     */
    protected int getMessageResId() {
        return -1;
    }

    protected abstract int getFragmentLayout();

    protected abstract void onCreateDialogLayout(View dialogView);

    protected abstract boolean hasNegativeButton();

    protected abstract boolean hasPositiveButton();

    protected abstract boolean hasNeutralButton();

    protected String getNegativeString() {
        return "";
    }

    protected String getPositiveString() {
        return "";
    }

    protected String getNeutralString() {
        return "";
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        // Verify that the host **activity** implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (I) activity;
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
        // if the activity doesn't implement callback then the target fragment may.
        try {
            mListener = (I) getTargetFragment();
        } catch (ClassCastException e) {
            // if the activity and also the fragment doesn't implement callback then the they can use
            // setCallback methods.
            mListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * The activity that creates an instance of this dialog fragment may
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface DialogListener {
    }
}
