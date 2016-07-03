package com.teamagam.gimelgimel.app.view.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * used to configure how the views should behave.
 */
public class MessageViewAdapter extends cool {

    private final MessageViewAdapter.OnItemClickListener mListener;
    private final Context mAppContext;

    MessageViewAdapter(View itemView, MessageViewAdapter.OnItemClickListener listener) {
        super(itemView);
        mView = itemView;
        mListener = listener;
        mAppContext = mView.getContext().getApplicationContext();
        ButterKnife.bind(mView);
        mTypeView = (ImageView) mView.findViewById(R.id.fragment_messages_master_icon);
        mSenderView = (TextView) mView.findViewById(R.id.fragment_messages_master_sender);
        mTimeView = (TextView) mView.findViewById(R.id.fragment_messages_master_time);

    }


    public View mView;

    @BindView(R.id.fragment_messages_master_icon)
    public ImageView mTypeView;

    @BindView(R.id.fragment_messages_master_time)
    public TextView mTimeView;

    @BindView(R.id.fragment_messages_master_sender)
    public TextView mSenderView;

    public Message mItem;


    @Override
    public String toString() {
        return super.toString() + " '" + mSenderView.getText() + "'";
    }

    @Override
    View bindView(Message msg) {
        mItem = msg;
        mTypeView.setImageDrawable(mAppContext.getDrawable(android.R.drawable.ic_media_ff));
        mTimeView.setText(msg.getCreatedAt().toString());
        mSenderView.setText(msg.getSenderId());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListItemInteraction(mItem);
            }
        });
        return mView;
    }

    @Override
    void bindView(View view, Message msg) {

    }

    @Override
    int countTypeView() {
        return 0;
    }

    @Override
    int messageType(Message msg) {
        return 0;
    }

    public interface OnItemClickListener {
        void onListItemInteraction(Message msg);
    }

    public static int getLayout() {
        return R.layout.fragment_messages_list_item;
    }

}
