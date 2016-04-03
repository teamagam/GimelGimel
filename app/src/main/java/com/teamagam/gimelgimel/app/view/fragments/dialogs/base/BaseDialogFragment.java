package com.teamagam.gimelgimel.app.view.fragments.dialogs.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Simplifies work with (up-to) 3-buttons dialogs.
 *
 * @param <DialogInterface> Dialog's concrete callback interface
 */
public abstract class BaseDialogFragment<DialogInterface> extends DialogFragment {

    protected String LOG_TAG = this.getClass().getSimpleName();
    protected AlertDialog mDialog;

    /**
     * Dialog clicks listener. initialized with simple (do-nothing) implementation
     */
    protected DialogInterface mListener;

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
            builder.setPositiveButton(getPositiveString(),
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            onPositiveClick();
                        }
                    });
        }
        if (hasNegativeButton()) {
            builder.setNegativeButton(getNegativeString(),
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            onNegativeClick();
                        }
                    });
        }
        if (hasNeutralButton()) {
            builder.setNeutralButton(getNeutralString(),
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            onNeutralClick();
                        }
                    });
        }

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView);

        // Create the AlertDialog object and return it
        mDialog = builder.create();

        return mDialog;
    }


    /**
     * Should be overridden when hasPositiveButton is true
     * to implement button click functionality
     */
    protected void onPositiveClick() {

    }

    /**
     * Should be overridden when hasNegativeButton is true
     * to implement button click functionality
     */
    protected void onNegativeClick() {

    }

    /**
     * Should be overridden when hasNeutralButton is true
     * to implement button click functionality
     */
    protected void onNeutralClick() {

    }

    /**
     * Indicates whether to construct a positive button or not.
     * Extending class should override and return true if a positive button is needed
     *
     * @return true iff the dialog should have a positive button
     */
    protected boolean hasPositiveButton() {
        return false;
    }

    /**
     * Indicates whether to construct a negative button or not.
     * Extending class should override and return true if a negative button is needed
     *
     * @return true iff the dialog should have a negative button
     */
    protected boolean hasNegativeButton() {
        return false;
    }

    /**
     * Indicates whether to construct a neutral button or not.
     * Extending class should override and return true if a neutral button is needed
     *
     * @return true iff the dialog should have a neutral button
     */
    protected boolean hasNeutralButton() {
        return false;
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

    /**
     * @return the data to inject on click events
     */

    protected abstract int getFragmentLayout();

    protected abstract void onCreateDialogLayout(View dialogView);

    protected String getNegativeString() {
        return "";
    }

    protected String getPositiveString() {
        return "";
    }

    protected String getNeutralString() {
        return "";
    }


    /**
     * Casts given activity into a suitable DialogFragment.
     * Used to overcome Java's runtime incapability of casting to generic type
     *
     * @param activity - the activity to cast
     * @return Suitable {@link DialogInterface}
     * @throws ClassCastException - if given activity doesn't implement {@link DialogInterface}
     */
    protected abstract DialogInterface castInterface(Activity activity);

    /**
     * Casts given fragment into a suitable DialogFragment.
     * Used to overcome Java's runtime incapability of casting to generic type
     *
     * @param fragment - the activity to cast
     * @return Suitable {@link DialogInterface}
     * @throws ClassCastException - if given activity doesn't implement {@link DialogInterface}
     */

    protected abstract DialogInterface castInterface(Fragment fragment);

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host **activity** implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = castInterface(activity);
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

            Fragment f = getTargetFragment();
            if (f == null) {
                throw new RuntimeException(this.getClass().toString()
                        + " containing activity doesn't implement the dialog's interface "
                        + " and dialog fragment target-fragment wasn't set on initialization");
            }
            //Hack to overcome generic lazy cast
            mListener = castInterface(getTargetFragment());
        } catch (ClassCastException e) {
            // if the activity and also the fragment doesn't implement callback then the they can use
            // setCallback methods.
            Log.e(LOG_TAG,
                    "Neither activity or target fragment implements dialog's communication interface!");
            throw new RuntimeException(this.getClass().toString()
                    + " must implement fragment's communication interface");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
