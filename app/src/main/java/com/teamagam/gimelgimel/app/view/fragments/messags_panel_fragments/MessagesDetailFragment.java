package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;

import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.view.fragments.BaseDataFragment;

import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * abstract class for detail message fragments.
 * It updates the title of the title view.
 */
public abstract class MessagesDetailFragment extends
        BaseDataFragment {



    @BindView(R.id.message_detail_sender_textview)
    TextView mMessageSenderTV;

    @BindView(R.id.message_detail_sent_date_textview)
    TextView mMessageDateTV;

    protected void updateTitle() {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.message_detail_title_time));
//        mMessageDateTV.setText(sdf.format(mViewModel.getDate()));
//        mMessageSenderTV.setText(mViewModel.getSenderId());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViews();
    }

//    @Override
    public void updateViewsOnUiThread() {
        updateViews();
    }


    private void updateViews() {
        updateTitle();
        updateContentViews();
    }

    protected abstract void updateContentViews();
}
