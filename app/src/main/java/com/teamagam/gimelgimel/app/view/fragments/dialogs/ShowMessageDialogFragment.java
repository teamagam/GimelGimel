package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Displays incoming messages.
 * Allows forward-only scanning of incoming message queue.
 * Enables GO-TO capability for location messages
 */
public class ShowMessageDialogFragment
        extends BaseDialogFragment<ShowMessageDialogFragment.ShowMessageDialogFragmentInterface> {

    //statics
    private static ShowMessageDialogFragment sInstance = new ShowMessageDialogFragment();

    //private fields
    private Queue<Message> mMessageQueue = new LinkedList<>();
    private Message mCurrentMessage;
    private int mCountTotalMessages = 0;
    private int mNumReadMessages = 0;

    /**
     * for synchronization of the singleton. this is used determining either creating new dialog or
     * updating the existing one.
     */
    private boolean mIsShown;

    //views
    private TextView mMessageTV;
    private TextView mNumMessagesTV;
    private TextView mLatLongTV;
    private Button mPositiveButton;
    private Button mNeutralButton;


    /**
     * Handles the creation and adding new messages to the fragment, either it is
     * dismissed or not. it is synchronized (static!) because we have one instance of the class.
     *
     * @param fm       FragmentManager to create new fragment.
     * @param messages {@link Collection<Message>} new messages to display.
     */
    public synchronized static void showNewMessages(FragmentManager fm,
                                                    Collection<Message> messages) {
        for (Message message : messages) {
            showNewMessages(fm, message);
        }
    }

    public static void showNewMessages(FragmentManager fm, Message message) {
        //do we want another if boolean member to lock?
        if (!sInstance.isShown()) {
            sInstance.show(fm, "ShowMessageDialogFragment");
        }
        sInstance.addMessages(message);
    }

    @Override
    public void onStart() {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        mNeutralButton = mDialog.getButton(Dialog.BUTTON_NEUTRAL);
        mPositiveButton = mDialog.getButton(Dialog.BUTTON_POSITIVE);

        if (!displayNewMessage()) {
            mMessageTV.setText(getString(R.string.fragment_show_empty_messages));
        }
    }


    /**
     * Handles positive button clicks events.
     * It is <b>synchronized</b> since this dialog's state (data, and therefore event handling)
     * can be altered by a different thread.
     */
    @Override
    protected synchronized void onPositiveClick() {
        if (!displayNewMessage()) {
            dismiss();
        }
    }

    /**
     * Handles neutral button clicks events.
     * It is <b>synchronized</b> since this dialog's state (data, and therefore event handling)
     * can be altered by a different thread.
     */

    @Override
    protected synchronized void onNeutralClick() {
        PointGeometry point = mCurrentMessage.getContent().getPoint();
        mInterface.goToLocation(point);
        mInterface.drawPin(point);
        dismiss();
    }

    /**
     * Updates the text views when the Next clicked
     * and also the buttons and numbers, using updatePresenceOfNewMessages method.
     *
     * @return true iff new message exist.
     */
    private boolean displayNewMessage() {
        mCurrentMessage = mMessageQueue.poll();
        if (mCurrentMessage == null) {
            return false;
        }

        mNumReadMessages++;
        displayTextViews();
        mNeutralButton.setEnabled(mCurrentMessage.getType().equals(Message.LAT_LONG));

        updatePresenceOfNewMessages();
        return true;
    }

    /**
     * displays text views according to the current message.
     */
    private void displayTextViews() {
        //Empty dialog's text views
        mLatLongTV.setText("");
        mMessageTV.setText("");
        mLatLongTV.setText("");

        TextView toEditTv;
        String newText;
        if (mCurrentMessage.getType().equals(Message.LAT_LONG)) {
            toEditTv = mLatLongTV;
            PointGeometry point = mCurrentMessage.getContent().getPoint();
            newText = String.format(getString(R.string.fragment_show_geo), point.latitude,
                    point.longitude);
        } else {
            toEditTv = mMessageTV;
            newText = mCurrentMessage.getContent().getText();
        }
        toEditTv.setText(newText);
    }

    /**
     * Updates count text view (how many messages left to read)
     * and positive button (OK/NEXT)
     */
    private void updatePresenceOfNewMessages() {
        String countString = String.format(getString(R.string.fragment_show_counter),
                mNumReadMessages, mCountTotalMessages);
        mNumMessagesTV.setText(countString);
        mPositiveButton.setText(getPositiveString());
    }

    @Override
    protected int getTitleResId() {
        return R.string.fragment_show_title;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_show_message;
    }

    @Override
    protected String getNegativeString() {
        return getString(R.string.fragment_show_negative);
    }

    @Override
    protected String getPositiveString() {
        if (!mMessageQueue.isEmpty()) {
            return getString(R.string.fragment_show_positive);
        } else {
            return getString(R.string.fragment_show_last);
        }
    }

    @Override
    protected String getNeutralString() {
        return getString(R.string.fragment_show_neutral);
    }

    @Override
    protected ShowMessageDialogFragmentInterface castInterface(
            Activity activity) {
        return (ShowMessageDialogFragmentInterface) activity;
    }

    @Override
    protected ShowMessageDialogFragmentInterface castInterface(
            Fragment fragment) {
        return (ShowMessageDialogFragmentInterface) fragment;
    }

    @Override
    protected void onCreateDialogLayout(View dialogView) {
        mMessageTV = (TextView) dialogView.findViewById(R.id.fragment_show_text);
        mNumMessagesTV = (TextView) dialogView.findViewById(R.id.fragment_show_counts);
        mLatLongTV = (TextView) dialogView.findViewById(R.id.fragment_show_geoText);
    }

    /**
     * add message to the queue
     *
     * @param message {@link Message}
     */
    public synchronized void addMessages(Message message) {
        if (message == null) {
            Log.d(LOG_TAG, "New Message was null");
            return;
        }

        mMessageQueue.add(message);
        mCountTotalMessages += 1;
        if (isResumed()) {
            updatePresenceOfNewMessages();
        }
    }

    @Override
    protected boolean hasNegativeButton() {
        return true;
    }

    @Override
    protected boolean hasPositiveButton() {
        return true;
    }

    @Override
    protected boolean hasNeutralButton() {
        return true;
    }

    /**
     * @return true if the fragment is shown (.show() to .onDestroyView)
     */
    public synchronized boolean isShown() {
        return mIsShown;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (mIsShown) {
            return;
        }

        super.show(manager, tag);
        mIsShown = true;
    }

    @Override
    public void onDestroyView() {
        mIsShown = false;
        super.onDestroyView();
    }

    /**
     * Containing activity or target fragment must implement this interface!
     * It is essential for this fragment communication
     */
    public interface ShowMessageDialogFragmentInterface {

        void goToLocation(PointGeometry pointGeometry);

        void drawPin(PointGeometry pointGeometry);
    }
}
