package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Yoni on 3/27/2016.
 */
public class ShowMessageDialogFragment extends BaseDialogFragment {


    private static ShowMessageDialogFragment mDialogFragment = null;
    private final Object mLock = new Object();
    private Queue<Message> mMessages = new LinkedList<>();
    private int mCountTotalMessages = 0;
    private int mNumReadMessages = 0;
    private TextView mMessageTV;
    private TextView mNumMessagesTV;
    private Button mPositiveButton;
    private boolean mIsShown = false;

    public static void showNewMessages(FragmentManager fm, Collection<Message> messages) {
        if (mDialogFragment == null || !mDialogFragment.isShown()) {
            mDialogFragment = new ShowMessageDialogFragment();
            mDialogFragment.show(fm, "ShowMessageDialogFragment");
        }
        mDialogFragment.addMessages(messages);
    }

    @Override
    public void onStart() {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point

        mPositiveButton = mDialog.getButton(Dialog.BUTTON_POSITIVE);
        //overrides default behaviour so clicking would not always dismiss the dialog.
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSynchronizedClick();
            }
        });
    }

    private synchronized void handleSynchronizedClick() {
        //for functionality handled from outside
        if (mListener != null) {
            ((NoticeDialogListener) mListener).onShowMessageDialogClick(ShowMessageDialogFragment.this, DialogInterface.BUTTON_POSITIVE);
        } else if (!mMessages.isEmpty()) {
            updateDialogNewText();
            updatePositiveButtonText();
        } else {
            dismiss();
        }
    }

    private void updatePositiveButtonText() {
        mPositiveButton.setText(getPositiveString());
    }

    private void updateDialogNewText() {
        mNumReadMessages++;
        mMessageTV.setText(mMessages.poll().getContent().getText());
        updateDialogCountMessages();
    }

    private void updateDialogCountMessages() {
        String countString = String.format(getString(R.string.fragment_show_counter),
                mNumReadMessages, mCountTotalMessages);
        mNumMessagesTV.setText(countString);
    }

    @Override
    protected int getTitleResId() {
        return R.string.fragment_show_title;
    }

    @Override
    protected String getMessage() {
        return null;
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
    protected void onCreateDialogLayout(View dialogView) {
        mMessageTV = (TextView) dialogView.findViewById(R.id.fragment_show_text);
        mNumMessagesTV = (TextView) dialogView.findViewById(R.id.fragment_show_counts);

        if (mMessages.isEmpty()) {
            mMessageTV.setText(getString(R.string.fragment_show_empty_messages));
        } else {
            updateDialogNewText();
        }
    }

    public synchronized void addMessages(Collection<Message> messages) {
        if (messages.isEmpty()) {
            return;
        }
        mMessages.addAll(messages);
        mCountTotalMessages += messages.size();
        if (isResumed()) {
            updateDialogCountMessages();
            updatePositiveButtonText();
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
    public void onDismiss(DialogInterface dialog) {
        mIsShown = false;

        super.onDismiss(dialog);
    }

    public interface NoticeDialogListener extends DialogListener {
        void onShowMessageDialogClick(DialogFragment dialog, int which);
    }
}
