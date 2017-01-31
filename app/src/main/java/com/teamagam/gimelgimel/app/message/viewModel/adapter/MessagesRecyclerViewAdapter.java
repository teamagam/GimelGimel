package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.drawee.view.SimpleDraweeView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseDisplayedMessagesRandomAccessor;
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


    //    private final RecyclerView.LayoutManager mLayoutManager;
    private final BaseDisplayedMessagesRandomAccessor<MessageApp> mDisplayedAccessor;
    private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
    private final ToggleMessageOnMapInteractorFactory mDrawMessageOnMapInteractorFactory;
    private MessageApp mCurrentlySelected;

    public MessagesRecyclerViewAdapter(
            BaseDisplayedMessagesRandomAccessor<MessageApp> accessor,
            OnItemClickListener<MessageApp> listener,
            GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
            ToggleMessageOnMapInteractorFactory drawMessageOnMapInteractorFactory) {
        super(accessor, listener);
//        mLayoutManager = layoutManager;
        mDisplayedAccessor = accessor;
        mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
        mDrawMessageOnMapInteractorFactory = drawMessageOnMapInteractorFactory;
    }

    @Override
    public int getItemViewType(int position) {
        MessageApp messageApp = mDisplayedAccessor.get(position);
        if (messageApp.isFromSelf()) {
            return VIEW_TYPE_SELF;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    public synchronized void select(String messageId) {
        unselectCurrent();
        selectNew(messageId);
        notifyDataSetChanged();
    }

    private synchronized void selectWithEffect(MessageViewHolder viewHolder) {
        RelativeLayout container = viewHolder.container;

        ObjectAnimator colorFade = ObjectAnimator.ofObject(
                container,
                "backgroundColor",
                new ArgbEvaluator(), 0x00000000, 0x603F51B5);
        ObjectAnimator reverseFade = ObjectAnimator.ofObject(
                container,
                "backgroundColor",
                new ArgbEvaluator(),
                0x803F51B5, 0x00000000);

        colorFade.setDuration(200);
        reverseFade.setDuration(2500);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(colorFade, reverseFade);

        set.start();
    }

    public void messageShownOnMap(String messageId) {
        try {
            setShownOnMap(messageId, true);
        } catch (Exception ignored) {

        }
    }

    public void messageHiddenFromMap(String messageId) {
        setShownOnMap(messageId, false);
    }

    public int getItemPosition(String messageId) {
        return mDisplayedAccessor.getPosition(messageId);
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
            selectWithEffect(holder);
        }
    }

    private void selectNew(String messageId) {
        MessageApp messageApp = getMessage(messageId);
        messageApp.setSelected(true);
        mCurrentlySelected = messageApp;
    }

    private void unselectCurrent() {
        if (mCurrentlySelected != null) {
            mCurrentlySelected.setSelected(false);
        }
    }

    private void setShownOnMap(String messageId, boolean isShownOnMap) {
        MessageApp messageApp = getMessage(messageId);

        messageApp.setShownOnMap(isShownOnMap);

        notifyDataSetChanged();
    }

    private MessageApp getMessage(String messageId) {
        int idx = mDisplayedAccessor.getPosition(messageId);
        return mDisplayedAccessor.get(idx);
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
}
