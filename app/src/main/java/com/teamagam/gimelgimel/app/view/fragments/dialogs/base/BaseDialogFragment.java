package com.teamagam.gimelgimel.app.view.fragments.dialogs.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.teamagam.gimelgimel.app.common.logging.LogWrapper;
import com.teamagam.gimelgimel.app.common.logging.LogWrapperFactory;

/**
 * Simplifies work with (up-to) 3-buttons dialogs.
 *
 * @param <DialogInterface> Dialog's concrete callback interface
 */
public abstract class BaseDialogFragment<DialogInterface> extends DialogFragment {

    protected LogWrapper sLogger = LogWrapperFactory.create(this.getClass());
    protected AlertDialog mDialog;

    /**
     * Dialog interface needed by dialog
     */
    protected DialogInterface mInterface;

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
        builder.setTitle(getTitleResId());

        if (getMessageResId() != -1) {
            builder.setMessage(getMessageResId());
        }

        android.content.DialogInterface.OnClickListener doNothingListener =
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        //Do nothing!
                    }
                };

        // Set buttons. A listener must be passed for the dialog to construct the buttons
        // Builder will add the listener to button's click listening pipe that will eventually
        // dismiss the button on every click. To overcome it, we override the behaviour on OnStart
        // after the builder finished building the dialog
        if (hasPositiveButton()) {
            builder.setPositiveButton(getPositiveString(), doNothingListener);
        }
        if (hasNegativeButton()) {
            builder.setNegativeButton(getNegativeString(), doNothingListener);
        }
        if (hasNeutralButton()) {
            builder.setNeutralButton(getNeutralString(), doNothingListener);
        }

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView);

        // Create the AlertDialog object and return it
        mDialog = builder.create();

        return mDialog;
    }


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host **activity** implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mInterface = castInterface(activity);
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, who knows? maybe the fragment will.
            mInterface = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mInterface != null) {
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
            mInterface = castInterface(getTargetFragment());
        } catch (ClassCastException e) {
            // if the activity and also the fragment doesn't implement callback then the they can use
            // setCallback methods.
            sLogger.e(
                    "Neither activity or target fragment implements dialog's communication interface!");
            throw new RuntimeException(this.getClass().toString()
                    + " activity/target-fragment must implement fragment's communication interface");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //Override buttons behaviour here to avoid auto-dismissing of the dialog
        if (hasPositiveButton()) {
            mDialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onPositiveClick();
                        }
                    });
        }

        if (hasNegativeButton()) {
            mDialog.getButton(android.content.DialogInterface.BUTTON_NEGATIVE)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNegativeClick();
                        }
                    });
        }

        if (hasNeutralButton()) {
            mDialog.getButton(android.content.DialogInterface.BUTTON_NEUTRAL)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNeutralClick();
                        }
                    });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }


    /**
     * Should be overridden when hasPositiveButton is true
     * to implement button click functionality
     */
    protected void onPositiveClick() {
        mDialog.dismiss();
    }

    /**
     * Should be overridden when hasNegativeButton is true
     * to implement button click functionality
     */
    protected void onNegativeClick() {
        mDialog.dismiss();
    }

    /**
     * Should be overridden when hasNeutralButton is true
     * to implement button click functionality
     */
    protected void onNeutralClick() {
        mDialog.dismiss();
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
}
