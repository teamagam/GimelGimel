package com.teamagam.gimelgimel.app.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;


/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link OnItemClickListener}.
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
        return new MessageViewHolder(view);
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

    protected void bindItemToView(final MessageViewHolder holder) {
        drawMessageIcon(holder);
        drawMessageDate(holder);
        drawMessageBackground(holder);

        holder.senderTV.setText(holder.item.getSenderId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListItemInteraction(holder.item);
            }
        });
    }

    private void drawMessageIcon(MessageViewHolder holder) {
        int draw;
        switch (getMessageType(holder.item)) {
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
        holder.typeIV.setImageDrawable(holder.mAppContext.getDrawable(draw));
    }

    private void drawMessageDate(MessageViewHolder holder) {
        SimpleDateFormat sdf = new SimpleDateFormat(holder.mAppContext.getString(R.string.message_list_item_time));
        holder.timeTV.setText(sdf.format(holder.item.getCreatedAt()));
    }

    private void drawMessageBackground(MessageViewHolder holder) {
        if ((Integer.parseInt(holder.item.getMessageId()) == 15)) {
            holder.itemView.setBackgroundColor(holder.mAppContext.getResources().getColor(R.color.message_chosen));
        } else {
            holder.itemView.setBackgroundColor(holder.mAppContext.getResources().getColor((Integer.parseInt(holder.item.getMessageId()) % 2) == 0
                    ? R.color.message_read : R.color.message_unread));
        }
    }


    public interface OnItemClickListener {
        void onListItemInteraction(Message item);
    }

    /**
     * used to configure how the views should behave.
     */
    static class MessageViewHolder extends BaseRecyclerViewHolder<Message> {

        @BindView(R.id.fragment_messages_master_icon)
        public ImageView typeIV;

        @BindView(R.id.fragment_messages_master_time)
        public TextView timeTV;

        @BindView(R.id.fragment_messages_master_sender)
        public TextView senderTV;

        public MessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
