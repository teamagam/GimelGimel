package com.teamagam.gimelgimel.app.view.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gimelgimel.domain.logging.Logger;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.DisplayMessage;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.MessagesViewModel;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;


/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link OnItemClickListener}.
 */
public class MessagesRecyclerViewAdapter extends
        BaseRecyclerArrayAdapter<MessagesRecyclerViewAdapter.MessageViewHolder, DisplayMessage> {

    private static Logger sLogger = LoggerFactory.create();

    private static Map<String, Integer> sTypeMessageMap = new TreeMap<>();

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_GEO = 1;
    private static final int TYPE_IMAGE = 2;

    static {
        sTypeMessageMap.put(Message.TEXT, TYPE_TEXT);
        sTypeMessageMap.put(Message.GEO, TYPE_GEO);
        sTypeMessageMap.put(Message.IMAGE, TYPE_IMAGE);
    }

    private final OnItemClickListener mListener;

    public MessagesRecyclerViewAdapter(MessagesViewModel.DisplayedMessagesRandomAccessor accessor,
                                       OnItemClickListener listener) {
        super(accessor);
        mListener = listener;
    }

    @Override
    protected MessageViewHolder createNewViewHolder(View view) {
        sLogger.d("createNewViewHolder");
        return new MessageViewHolder(view);
    }

    @Override
    protected int getSingleItemLayoutRes() {
        return R.layout.recycler_messages_list_item;
    }

    @Override
    public int getItemViewType(int position) {
        return getMessageType(mAccessor.get(position));
    }

    private static int getMessageType(DisplayMessage msg) {
        return sTypeMessageMap.get(msg.getMessage().getType());
    }

    @Override
    protected void bindItemToView(final MessageViewHolder holder,
                                  final DisplayMessage displayMessage) {
        sLogger.d("onBindItemView");
        drawMessageIcon(holder, displayMessage);
        drawMessageDate(holder, displayMessage);
        drawMessageBackground(holder, displayMessage);

        holder.senderTV.setText(displayMessage.getMessage().getSenderId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListItemInteraction(displayMessage);
            }
        });
    }

    private void drawMessageIcon(MessageViewHolder holder, DisplayMessage displayMessage) {
        int draw;

        if(displayMessage.isSelected()) {
            draw = R.drawable.ic_done;
            holder.typeIV.setColorFilter(R.color.black);
        } else {
            switch (getMessageType(displayMessage)) {
                case TYPE_TEXT:
                    draw = R.drawable.ic_message;
                    break;
                case TYPE_IMAGE:
                    draw = R.drawable.ic_camera;
                    break;
                case TYPE_GEO:
                    draw = R.drawable.ic_map_marker;
                    break;
                default:
                    draw = R.drawable.ic_notifications_black_24dp;
            }

            holder.typeIV.setColorFilter(R.color.white);
        }
        holder.typeIV.setImageDrawable(holder.itemView.getContext().getDrawable(draw));
    }

    private void drawMessageDate(MessageViewHolder holder, DisplayMessage displayMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                holder.mAppContext.getString(R.string.message_list_item_time));
        holder.timeTV.setText(sdf.format(displayMessage.getMessage().getCreatedAt()));
    }

    private void drawMessageBackground(MessageViewHolder holder, DisplayMessage displayMessage) {
        int backgroundColorId;

        backgroundColorId = getBackgroundColorId(displayMessage);

        holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(), backgroundColorId));
    }

    private int getBackgroundColorId(DisplayMessage displayMessage) {
        if (displayMessage.isSelected()) {
            return R.color.message_chosen;
        }
        if (displayMessage.isRead()) {
            return R.color.message_read;
        } else {
            return R.color.message_unread;
        }
    }


    public interface OnItemClickListener {
        void onListItemInteraction(DisplayMessage item);
    }

    /**
     * used to configure how the views should behave.
     */
    static class MessageViewHolder extends BaseRecyclerViewHolder<DisplayMessage> {

        @BindView(R.id.message_row_type_imageview)
        public ImageView typeIV;

        @BindView(R.id.message_row_date_textview)
        public TextView timeTV;

        @BindView(R.id.message_row_sender_textview)
        public TextView senderTV;

        public MessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
