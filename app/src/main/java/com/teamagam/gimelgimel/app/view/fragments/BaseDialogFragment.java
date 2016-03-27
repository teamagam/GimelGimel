package com.teamagam.gimelgimel.app.view.fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Yoni on 3/27/2016.
 */
public abstract class BaseDialogFragment<T extends Application> extends DialogFragment {

    protected String TAG_FRAGMENT = ((Object) this).getClass().getSimpleName();
    protected AlertDialog mDialog;

    protected T mApp;

    protected DialogInterface.OnClickListener mPositiveCallback = null;
    protected DialogInterface.OnClickListener mNegativeCallback = null;

    protected boolean hasNegativeButton;
    protected boolean hasPositiveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_FRAGMENT, "onCreate");
        mApp = (T)(getActivity().getApplicationContext());
    }

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

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle(getTitle());

        if (getMessage() != 0) {
            builder.setMessage(getMessage());
        }

        if(mPositiveCallback != null || hasPositiveButton){
            builder.setPositiveButton(getPositiveString(), mPositiveCallback);
        }
        if(mNegativeCallback != null || hasNegativeButton){
            builder.setNegativeButton(getNegativeString(), mNegativeCallback);
        }


        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView);

        // Create the AlertDialog object and return it
        mDialog = builder.create();

        return mDialog;
    }

    public void setPositiveCallback(DialogInterface.OnClickListener callback){
        mPositiveCallback = callback;
    }

    public void setNegativeCallback(DialogInterface.OnClickListener callback){
        mNegativeCallback = callback;
    }

    protected abstract int getTitle();

    protected abstract int getMessage();

    protected abstract int getFragmentLayout();

    protected abstract void onCreateDialogLayout(View dialogView);

    protected String getNegativeString() {
        return "";
    }
    protected String getPositiveString() {
        return "";
    }


}
