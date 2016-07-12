package com.teamagam.gimelgimel.app.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.MessagesViewModel;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesDetailTextFragment extends MessagesDetailFragment {

    @BindView(R.id.fragment_text_message_content)
    TextView mContentTV;

    private MessagesViewModel mMessageTextViewModel;

    public MessagesDetailTextFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_text_message;
    }

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mMessageTextViewModel = mApp.getMessagesViewModel();
        mMessageTextViewModel.addObserver(this);
        updateViewMessage();
        return rootView;
    }

    @Override
    public void onDataChanged() {
        updateViewMessage();
    }

    private void updateViewMessage() {
        MessageText selectedMessage = getSelectedMessage();
        if(selectedMessage != null) {
            mMessageDateTV.setText(selectedMessage.getCreatedAt().toString());
            mMessageSenderTV.setText(selectedMessage.getSenderId());
            mContentTV.setText(selectedMessage.getContent());
        }
    }

    private MessageText getSelectedMessage() {
        MessagesViewModel.DisplayedMessagesRandomAccessor accessor = mMessageTextViewModel.getDisplayedMessagesRandomAccessor();
        for (int i = 0; i < accessor.size(); i++) {
            if (accessor.get(i).isSelected() && accessor.get(i).getMessage().getType().equals
                    (Message.TEXT) )
                return (MessageText) accessor.get(i).getMessage();
        }
        return null;
    }
}

