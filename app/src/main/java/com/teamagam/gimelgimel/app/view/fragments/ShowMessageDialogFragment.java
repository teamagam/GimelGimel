package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.util.List;

/**
 * Created by Yoni on 3/27/2016.
 */
public class ShowMessageDialogFragment extends BaseDialogFragment {

    private List<Message> mMessages;
    private TextView mMessageTV;
    private TextView mNumMessagesTV;
    private int mCountMessages = 1;

    @Override
    public void onStart() {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        final Button positiveButton = mDialog.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPositiveCallback.onClick(mDialog, DialogInterface.BUTTON_POSITIVE);
                positiveButton.setText(getPositiveString());
            }
        });
    }


    @Override
    protected int getTitle() {
        return R.string.fragment_show_title;
    }

    @Override
    protected int getMessage() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_show_message;
    }

    protected String getNegativeString() {
        return getString(R.string.fragment_show_negative);
    }

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
            setNewMessageText();
        }

        mPositiveCallback = new OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!mMessages.isEmpty()) {
                    setNewMessageText();
                } else {
                    mDialog.dismiss();
                }
            }
        };

        hasNegativeButton = true;
    }

    private void setNewMessageText() {
        String countString = String.format(getString(R.string.fragment_show_counter),
                mCountMessages - mMessages.size() + 1, mCountMessages);
        mNumMessagesTV.setText(countString);
        mMessageTV.setText(mMessages.remove(0).getContent().getText());
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
        mCountMessages = mMessages.size();
    }
}
