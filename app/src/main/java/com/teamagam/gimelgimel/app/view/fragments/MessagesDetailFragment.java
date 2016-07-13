package com.teamagam.gimelgimel.app.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * abstract class for detail view.
 */
public abstract class MessagesDetailFragment extends BaseDataFragment<GGApplication> {

    @BindView(R.id.message_detail_sender_textview)
    TextView mMessageSenderTV;

    @BindView(R.id.message_detail_sent_date_textview)
    TextView mMessageDateTV;

    protected void updateTitle(String sender, Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.message_detail_title_time));
        mMessageDateTV.setText(sdf.format(time));
        mMessageSenderTV.setText(sender);
    }

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        getSpecificViewModel();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContentViews();
    }

    abstract void getSpecificViewModel();

    @Override
    public void onDataChange() {
        updateContentViews();
    }

    protected abstract void updateContentViews();
}
