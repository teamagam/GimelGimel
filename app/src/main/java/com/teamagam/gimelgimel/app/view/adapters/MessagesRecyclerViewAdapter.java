package com.teamagam.gimelgimel.app.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.view.fragments.MessagesMasterFragment;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link MessagesMasterFragment.OnMessageMasterFragmentClickListener}.
 */
public class MessagesRecyclerViewAdapter extends
        BaseRecyclerArrayAdapter<MessagesRecyclerViewAdapter.MessageViewHolder, Message> {

    private static Map<String, Integer> sTypeMessageMap = new TreeMap<>();

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_LAT_LONG = 1;
    private static final int TYPE_IMAGE = 2;

    static {
        sTypeMessageMap.put(Message.TEXT, TYPE_TEXT);
        sTypeMessageMap.put(Message.LAT_LONG, TYPE_LAT_LONG);
        sTypeMessageMap.put(Message.IMAGE, TYPE_IMAGE);
    }

    private final OnItemClickListener mListener;

    public MessagesRecyclerViewAdapter(MessageListViewModel.DisplayMessagesRandomAccessor accessor, MessagesRecyclerViewAdapter.OnItemClickListener listener) {
        super(accessor);
        mListener = listener;
    }

    @Override
    protected MessageViewHolder createNewViewHolder(View view) {
        return new MessageViewHolder(view, mListener);
    }

    @Override
    protected int getSingleItemLayoutRes() {
        return R.layout.fragment_messages_list_item;
    }

    @Override
    public int getItemViewType(int position) {
        return getMessageType(mAccessor.get(position));
    }

    private static int getMessageType(Message msg) {
        return sTypeMessageMap.get(msg.getType());
    }

    public interface OnItemClickListener {
        void onListItemInteraction(Message item);
    }

    /**
     * used to configure how the views should behave.
     */
    static class MessageViewHolder extends BaseRecyclerViewHolder<Message> {

        private final Context mAppContext;
        private final OnItemClickListener mListener;

        public View mView;

        @BindView(R.id.fragment_messages_master_icon)
        public ImageView mTypeView;

        @BindView(R.id.fragment_messages_master_time)
        public TextView mTimeView;

        @BindView(R.id.fragment_messages_master_sender)
        public TextView mSenderView;

        public Message mItem;

        MessageViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mView = itemView;
            mAppContext = mView.getContext().getApplicationContext();
            mListener = listener;
            ButterKnife.bind(this, itemView);
        }

        public View bindView(Message msg) {
            mItem = msg;
            int draw;
            switch (getMessageType(msg)) {
                case TYPE_TEXT:
                    draw = android.R.drawable.ic_media_pause;
                    break;
                case TYPE_IMAGE:
                    draw = android.R.drawable.ic_media_next;
                    break;
                case TYPE_LAT_LONG:
                    draw = android.R.drawable.ic_media_rew;
                    break;
                default:
                    draw = R.drawable.ic_notifications_black_24dp;
            }
            mTypeView.setImageDrawable(mAppContext.getDrawable(draw));
            SimpleDateFormat sdf = new SimpleDateFormat(mAppContext.getString(R.string.message_list_item_time));
            mTimeView.setText(sdf.format(mItem.getCreatedAt()));
            mSenderView.setText(msg.getSenderId());
            if ((Integer.parseInt(mItem.getMessageId()) == 15)) {
                itemView.setBackgroundColor(mAppContext.getResources().getColor(R.color.message_chosen));
            } else {
                itemView.setBackgroundColor(mAppContext.getResources().getColor((Integer.parseInt(mItem.getMessageId()) % 2) == 0
                        ? R.color.message_read : R.color.message_unread));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onListItemInteraction(mItem);
                }
            });
            return mView;
        }
    }
}
