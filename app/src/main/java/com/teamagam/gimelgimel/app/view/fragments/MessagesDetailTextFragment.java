package com.teamagam.gimelgimel.app.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.TextMessageDetailViewModel;


import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesDetailTextFragment extends MessagesDetailFragment {

    @BindView(R.id.fragment_text_message_content)
    TextView mContentTV;

    private TextMessageDetailViewModel mMessageViewModel;

    public MessagesDetailTextFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_text_message;
    }

    @Override
    void getSpecificViewModel() {
        mMessageViewModel = mApp.getTextMessageDetailViewModel();
        mMessageViewModel.addObserver(this);
    }

    @Override
    protected void updateContentViews() {
        updateTitle(mMessageViewModel.getSenderId(), mMessageViewModel.getDate());
        mContentTV.setText(mMessageViewModel.getText());
    }
}

