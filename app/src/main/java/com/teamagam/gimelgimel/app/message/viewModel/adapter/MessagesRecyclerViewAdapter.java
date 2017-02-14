package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.drawee.view.SimpleDraweeView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerViewHolder;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ToggleMessageOnMapInteractorFactory;

import butterknife.BindView;

import static com.teamagam.gimelgimel.R.id.recycler_message_listitem_layout;


/**
 * {@link RecyclerView.Adapter} that can display a {@link MessageApp} and makes a call to the
 * specified {@link OnItemClickListener}.
 */
public class MessagesRecyclerViewAdapter extends
        BaseRecyclerArrayAdapter<MessagesRecyclerViewAdapter.MessageViewHolder, MessageApp> {

    private static AppLogger sLogger = AppLoggerFactory.create();

    private static final int VIEW_TYPE_SELF = 0;
    private static final int VIEW_TYPE_OTHER = 1;
    private static final int VIEW_TYPE_ALERT = 2;

    private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
    private final ToggleMessageOnMapInteractorFactory mDrawMessageOnMapInteractorFactory;
    private MessageApp mCurrentlySelected;

    public MessagesRecyclerViewAdapter(
            OnItemClickListener<MessageApp> listener,
            GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
            ToggleMessageOnMapInteractorFactory drawMessageOnMapInteractorFactory) {
        super(new SortedList<>(MessageApp.class, new MessageAppCallback()), listener);
        mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
        mDrawMessageOnMapInteractorFactory = drawMessageOnMapInteractorFactory;
    }

    @Override
    public int getItemViewType(int position) {
        MessageApp messageApp = get(position);
        if (messageApp.isFromSelf()) {
            return VIEW_TYPE_SELF;
        } else if (isAlertMessage(messageApp)) {
            return VIEW_TYPE_ALERT;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    public synchronized void select(String messageId) {
        unselectCurrent();
        selectNew(messageId);
        notifyDataSetChanged();
    }

    @Override
    protected MessageViewHolder createNewViewHolder(View view, int viewType) {
        sLogger.d("createNewViewHolder");
        return new MessageViewHolder(view);
    }

    @Override
    protected int getListItemLayout(int viewType) {
        if (viewType == VIEW_TYPE_OTHER) {
            return R.layout.recycler_message_list_item_other;
        }
        if (viewType == VIEW_TYPE_ALERT) {
            return R.layout.recycler_message_list_item_alert;
        }
        return R.layout.recycler_message_list_item_self;
    }

    @Override
    protected void bindItemToView(final MessageViewHolder holder,
                                  final MessageApp message) {
        sLogger.d("onBindItemView");
        MessageViewHolderBindVisitor bindVisitor = new MessageViewHolderBindVisitor(
                holder, mGoToLocationMapInteractorFactory, mDrawMessageOnMapInteractorFactory);
        message.accept(bindVisitor);

        if(message.isSelected()) {
            animateSelection(holder);
        }
    }

    private void selectNew(String messageId) {
        MessageApp messageApp = getById(messageId);
        messageApp.setSelected(true);
        mCurrentlySelected = messageApp;
    }

    private synchronized void animateSelection(MessageViewHolder viewHolder) {
        RelativeLayout container = viewHolder.container;

        ObjectAnimator colorFade = ObjectAnimator.ofObject(
                container,
                "backgroundColor",
                new ArgbEvaluator(),
                ContextCompat.getColor(viewHolder.mAppContext, R.color.transparent),
                ContextCompat.getColor(viewHolder.mAppContext, R.color.selection_color));
        ObjectAnimator reverseFade = ObjectAnimator.ofObject(
                container,
                "backgroundColor",
                new ArgbEvaluator(),
                ContextCompat.getColor(viewHolder.mAppContext, R.color.selection_color),
                ContextCompat.getColor(viewHolder.mAppContext, R.color.transparent));

        colorFade.setDuration(200);
        reverseFade.setDuration(2500);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(colorFade, reverseFade);

        set.start();
    }

    private void unselectCurrent() {
        if (mCurrentlySelected != null) {
            mCurrentlySelected.setSelected(false);
        }
    }

    private boolean isAlertMessage(MessageApp messageApp) {
        return MessageApp.ALERT.equals(messageApp.getType());
    }

    /**
     * used to configure how the views should behave.
     */
    static class MessageViewHolder extends BaseRecyclerViewHolder<MessageApp> {

        @BindView(recycler_message_listitem_layout)
        RelativeLayout container;

        @BindView(R.id.message_type_imageview)
        SimpleDraweeView imageView;

        @BindView(R.id.message_date_textview)
        TextView timeTV;

        @BindView(R.id.message_sender_textview)
        TextView senderTV;

        @BindView(R.id.message_text_content)
        TextView contentTV;

        @BindView(R.id.message_goto_button)
        Button gotoButton;

        @BindView(R.id.message_display_toggle)
        ToggleButton displayToggleButton;

        @BindView(R.id.message_geo_panel)
        LinearLayout messageGeoPanel;

        MessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class MessageAppCallback extends SortedList.Callback<MessageApp> {
        @Override
        public int compare(MessageApp o1, MessageApp o2) {
            return o1.getCreatedAt().compareTo(o2.getCreatedAt());
        }

        @Override
        public void onChanged(int position, int count) {

        }

        @Override
        public boolean areContentsTheSame(MessageApp oldItem, MessageApp newItem) {
            return areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areItemsTheSame(MessageApp item1, MessageApp item2) {
            String id1 = item1.getId();
            String id2 = item2.getId();
            return id1.equals(id2);
        }

        @Override
        public void onInserted(int position, int count) {

        }

        @Override
        public void onRemoved(int position, int count) {

        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {

        }
    }
}