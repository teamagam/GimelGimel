package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Dialog;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Yoni on 3/27/2016.
 */
public class ShowMessageDialogFragment extends BaseDialogFragment<ShowMessageDialogInterface> implements View.OnClickListener {


    //statics
    private static ShowMessageDialogFragment mDialogFragment = null;
    private static ShowMessageDialogInterface mGoToListener;

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
    public synchronized static void showNewMessages(FragmentManager fm, Collection<Message> messages) {
        for (Message message : messages) {
            showNewMessages(fm, message);
        }
    }

    public static void showNewMessages(FragmentManager fm, Message message) {
        //do we want another if boolean member to lock?
        if (mDialogFragment == null || !mDialogFragment.isShown()) {
            mDialogFragment = new ShowMessageDialogFragment();
            mDialogFragment.show(fm, "ShowMessageDialogFragment");
        }
        mDialogFragment.addMessages(message);
        //do i want to use static listener?
//        mDialogFragment.mListener = mGoToListener;
    }

    public static void setGoToListener(ShowMessageDialogInterface listener) {
        mGoToListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        mNeutralButton = mDialog.getButton(Dialog.BUTTON_NEUTRAL);
        mPositiveButton = mDialog.getButton(Dialog.BUTTON_POSITIVE);
        //overrides default behaviour so clicking would not always dismiss the dialog.
        mPositiveButton.setOnClickListener(this);
        mNeutralButton.setOnClickListener(this);

        if (!displayNewMessage()) {

            mMessageTV.setText(getString(R.string.fragment_show_empty_messages));
        }
    }


    /**
     * Handles all clicks on synchronized method
     *
     * @param v is the button clicked
     */
    @Override
    public synchronized void onClick(View v) {
        if (v.getId() == mPositiveButton.getId()) {
            if (!displayNewMessage()) {
                dismiss();
            }
        } else if (v.getId() == mNeutralButton.getId()) {
            mGoToListener.goToLocation(mCurrentMessage.getContent().getPoint());
            dismiss();
        }
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
        mNeutralButton.setEnabled((mCurrentMessage.getType().equals(Message.LAT_LONG)));

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

        TextView toEditTv = null;
        String newText = "";
        if (mCurrentMessage.getType().equals(Message.LAT_LONG)) {
            toEditTv = mLatLongTV;
            PointGeometry point = mCurrentMessage.getContent().getPoint();
            newText = String.format(getString(R.string.fragment_show_geo), point.latitude, point.longitude);
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
        if (mIsShown) return;

        super.show(manager, tag);
        mIsShown = true;
    }

    @Override
    public void onDestroyView() {
        mIsShown = false;
        super.onDestroyView();
    }

}
