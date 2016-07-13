package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.MessageDetailViewModel;
import com.teamagam.gimelgimel.app.view.fragments.BaseDataFragment;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * abstract class for detail message fragments.
 * It updates the title of the title view.
 */
public abstract class MessagesDetailFragment<VM extends MessageDetailViewModel> extends BaseDataFragment<GGApplication> {

    @BindView(R.id.message_detail_sender_textview)
    TextView mMessageSenderTV;

    @BindView(R.id.message_detail_sent_date_textview)
    TextView mMessageDateTV;

    protected VM mMessageViewModel;

    protected void updateTitle() {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.message_detail_title_time));
        mMessageDateTV.setText(sdf.format(mMessageViewModel.getDate()));
        mMessageSenderTV.setText(mMessageViewModel.getSenderId());
    }

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        createSpecificView(rootView);
        getSpecificViewModel();
        mMessageViewModel.addObserver(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
        updateContentViews();
    }


    /**
     * if the detail fragment needs to add specific view functionality (e.g. OnClick).
     */
    @SuppressWarnings("unused")
    protected void createSpecificView(View rootView) {}

    abstract void getSpecificViewModel();

    @Override
    public void onDataChange() {
        updateContentViews();
    }

    protected abstract void updateContentViews();
}
