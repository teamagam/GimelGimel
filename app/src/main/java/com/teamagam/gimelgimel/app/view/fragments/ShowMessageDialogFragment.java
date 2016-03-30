package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Dialog;
import android.app.FragmentManager;
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


    private static final Object mLock = new Object();
    private static ShowMessageDialogFragment mDialogFragment = null;
    private static ShowMessageDialogInterface mGoToListener;

    private Queue<Message> mMessages = new LinkedList<>();
    private Message mCurrentMessage;
    private int mCountTotalMessages = 0;
    private int mNumReadMessages = 0;
    private TextView mMessageTV;
    private TextView mNumMessagesTV;
    private Button mPositiveButton;
    private boolean mIsShown;
    private Button mNeutralButton;
    private TextView mLatLongTV;

    /**
     * this method handles the creation and adding new messages to the fragment, either it is
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

        if (!updateDialogWithContent()) {
            mMessageTV.setText(getString(R.string.fragment_show_empty_messages));
        }
    }


    /**
     * this method handles all clicks on synchronized method
     * @param v is the button clicked
     */
    @Override
    public synchronized void onClick(View v) {
        if (v.getId() == mPositiveButton.getId()) {
            if (!updateDialogWithContent()) {
                dismiss();
            }
        } else if (v.getId() == mNeutralButton.getId()) {
            mGoToListener.goToSelectedMessage(mCurrentMessage.getContent().getPoint());
            dismiss();
        }
    }


    /**
     * this method update new message and also the buttons and numbers.
     *
     * @return true if new message exist.
     */
    private boolean updateDialogWithContent() {
        mCurrentMessage = mMessages.poll();
        if (mCurrentMessage == null) {
            return false;
        }
        mNumReadMessages++;
        if (mCurrentMessage.getType().equals(Message.LAT_LONG)) {
            PointGeometry point = mCurrentMessage.getContent().getPoint();
            String geoStr = String.format(getString(R.string.fragment_show_geo), point.latitude, point.longitude);
            mLatLongTV.setText(geoStr);
        } else {
            mMessageTV.setText(mCurrentMessage.getContent().getText());
            mLatLongTV.setText("");
        }
        mNeutralButton.setEnabled((mCurrentMessage.getType().equals(Message.LAT_LONG)));

        updateDialogWithoutContent();
        return true;
    }

    private void updateDialogWithoutContent() {
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
        if (!mMessages.isEmpty()) {
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

    public synchronized void addMessages(Message message) {
        if (message == null) {
            return;
        }
        mMessages.add(message);
        mCountTotalMessages += 1;
        if (isResumed()) {
            updateDialogWithoutContent();
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
