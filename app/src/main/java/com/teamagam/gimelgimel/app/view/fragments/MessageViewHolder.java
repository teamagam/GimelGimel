package com.teamagam.gimelgimel.app.view.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
public class MessageViewHolder extends cool {

    private final MessageViewHolder.OnItemClickListener mListener;
    private final Context mAppContext;

    MessageViewHolder(View itemView, MessageViewHolder.OnItemClickListener listener) {
        super(itemView);
        mView = itemView;
        mListener = listener;
        mAppContext = mView.getContext().getApplicationContext();
        ButterKnife.bind(this, itemView);
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
        //todo: types
        int draw;
        switch (Integer.parseInt(mItem.getMessageId()) % 3){
            case 0:
                draw = android.R.drawable.ic_media_ff;
                break;
            case 1:
                draw = android.R.drawable.ic_media_next;
                break;
            case 2:
                draw = android.R.drawable.ic_media_rew;
                break;
            default:
                draw = android.R.drawable.ic_media_pause;
        }
        mTypeView.setImageDrawable(mAppContext.getDrawable(draw));

        mTimeView.setText(msg.getCreatedAt().toString());
        mSenderView.setText(msg.getSenderId());
        //todo: color for read
        itemView.setBackgroundColor(mAppContext.getResources().getColor((Integer.parseInt(mItem.getMessageId()) % 2) == 0 ? R.color.colorAccent : R.color.colorPrimaryDark));
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
