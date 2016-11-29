package com.teamagam.gimelgimel.app.view.fragments.dialogs.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;

import butterknife.ButterKnife;

/**
 * Simplifies work with (up-to) 3-buttons dialogs.
 *
 * @param <DialogInterface> Dialog's concrete callback interface
 */
public abstract class BaseDialogFragment<DialogInterface> extends DialogFragment {

    protected AppLogger sLogger = AppLoggerFactory.create(this.getClass());
    protected AlertDialog mDialog;

    /**
     * Dialog interface needed by dialog
     */
    protected DialogInterface mInterface;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        sLogger.onAttach();
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
        sLogger.onCreate();
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getTitleResId());

        makeButtons(builder);

        if (isCustomMessageAvailable()) {
            builder.setMessage(getMessageResId());
        }

        if (isCustomLayoutAvailable()) {
            makeCustomLayout(builder);
        }

        mDialog = builder.create();
        return mDialog;
    }

    private void makeCustomLayout(AlertDialog.Builder builder) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(getDialogLayout(), null);

        ButterKnife.bind(this, dialogView);
        onCreateDialogLayout(dialogView);

        builder.setView(dialogView);
    }

    private boolean isCustomLayoutAvailable() {
        return getDialogLayout() != -1;
    }

    private boolean isCustomMessageAvailable() {
        return getMessageResId() != -1;
    }

    private void makeButtons(AlertDialog.Builder builder) {
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sLogger.onCreateView();
        //this method returns null
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        sLogger.onStart();
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
    public void onResume() {
        sLogger.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        sLogger.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        sLogger.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        sLogger.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        sLogger.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        sLogger.onDetach();
        super.onDetach();
        mInterface = null;
    }


    /**
     * Should be overridden when hasPositiveButton is true
     * to implement button click functionality
     */
    protected void onPositiveClick() {
        sLogger.userInteraction("OK clicked");
        mDialog.dismiss();
    }

    /**
     * Should be overridden when hasNegativeButton is true
     * to implement button click functionality
     */
    protected void onNegativeClick() {
        sLogger.userInteraction("Cancel clicked");
        mDialog.dismiss();
    }

    /**
     * Should be overridden when hasNeutralButton is true
     * to implement button click functionality
     */
    protected void onNeutralClick() {
        sLogger.userInteraction("Neutral clicked");
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

    protected int getDialogLayout() {
        return -1;
    }

    protected void onCreateDialogLayout(View dialogView) {
    }

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
