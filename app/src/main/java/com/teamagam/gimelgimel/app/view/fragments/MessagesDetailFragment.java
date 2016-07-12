package com.teamagam.gimelgimel.app.view.fragments;

import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;

import butterknife.BindView;

/**
 * abstract class for detail view.
 */
public abstract class MessagesDetailFragment extends BaseDataFragment<GGApplication>{

    @BindView(R.id.message_detail_sender_textview)
    TextView mMessageSenderTV;

    @BindView(R.id.message_detail_sent_date_textview)
    TextView mMessageDateTV;

}
