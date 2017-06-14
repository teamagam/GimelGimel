package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import butterknife.BindView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerViewHolder;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ToggleMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.MessagePresentation;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import java.util.Comparator;

import static com.teamagam.gimelgimel.R.id.recycler_message_listitem_layout;

public class MessagesRecyclerViewAdapter extends
    BaseRecyclerArrayAdapter<MessagesRecyclerViewAdapter.MessageViewHolder, MessagePresentation> {

  private static final int VIEW_TYPE_SELF = 0;
  private static final int VIEW_TYPE_OTHER = 1;
  private static final int VIEW_TYPE_ALERT = 2;
  private static AppLogger sLogger = AppLoggerFactory.create();
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final ToggleMessageOnMapInteractorFactory mDrawMessageOnMapInteractorFactory;
  private final GlideLoader mGlideLoader;
  private final Navigator mNavigator;

  public MessagesRecyclerViewAdapter(OnItemClickListener<MessagePresentation> onMessageClickListener,
      GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      ToggleMessageOnMapInteractorFactory drawMessageOnMapInteractorFactory,
      GlideLoader glideLoader,
      Navigator navigator) {
    super(MessagePresentation.class, new MessageComparator(), onMessageClickListener);
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mDrawMessageOnMapInteractorFactory = drawMessageOnMapInteractorFactory;
    mGlideLoader = glideLoader;
    mNavigator = navigator;
  }

  @Override
  public int getItemViewType(int position) {
    MessagePresentation message = get(position);
    if (message.isFromSelf()) {
      return VIEW_TYPE_SELF;
    } else if (isAlertMessage(message)) {
      return VIEW_TYPE_ALERT;
    } else {
      return VIEW_TYPE_OTHER;
    }
  }

  @Override
  public MessagePresentation get(int position) {
    return super.get(position);
  }

  public synchronized void select(String messageId) {
    selectNew(messageId);
    notifyItemChanged(getItemPosition(messageId));
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
      final MessagePresentation messagePresentation) {
    sLogger.d("onBindItemView");
    MessageViewHolderBindVisitor bindVisitor =
        new MessageViewHolderBindVisitor(holder, mGoToLocationMapInteractorFactory,
            mDrawMessageOnMapInteractorFactory, mNavigator, mGlideLoader, messagePresentation);
    messagePresentation.getMessage().accept(bindVisitor);

    holder.stopAnimation();

    if (messagePresentation.isSelected()) {
      animateSelection(holder);
      messagePresentation.setIsSelected(false);
    }
  }

  private void selectNew(String messageId) {
    MessagePresentation message = getById(messageId);
    message.setIsSelected(true);
  }

  private synchronized void animateSelection(MessageViewHolder viewHolder) {
    RelativeLayout container = viewHolder.container;

    ObjectAnimator colorFade =
        ObjectAnimator.ofObject(container, "backgroundColor", new ArgbEvaluator(),
            ContextCompat.getColor(viewHolder.mAppContext, R.color.transparent),
            ContextCompat.getColor(viewHolder.mAppContext, R.color.selection_color));
    ObjectAnimator reverseFade =
        ObjectAnimator.ofObject(container, "backgroundColor", new ArgbEvaluator(),
            ContextCompat.getColor(viewHolder.mAppContext, R.color.selection_color),
            ContextCompat.getColor(viewHolder.mAppContext, R.color.transparent));

    colorFade.setDuration(200);
    reverseFade.setDuration(2500);

    AnimatorSet set = new AnimatorSet();
    set.playSequentially(colorFade, reverseFade);

    viewHolder.setAnimatorSet(set);
    viewHolder.startAnimation();
  }

  private boolean isAlertMessage(MessagePresentation message) {
    return message.getMessage().contains(AlertFeature.class);
  }

  /**
   * used to configure how the views should behave.
   */
  static class MessageViewHolder extends BaseRecyclerViewHolder {

    @BindView(recycler_message_listitem_layout)
    RelativeLayout container;

    @BindView(R.id.image_container)
    FrameLayout imageContainerLayout;

    @BindView(R.id.viewholder_progress_view)
    CircularProgressView progressView;

    @BindView(R.id.message_type_imageview)
    ImageView imageView;

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

  private static class MessageComparator implements Comparator<MessagePresentation> {
    @Override
    public int compare(MessagePresentation o1, MessagePresentation o2) {
      return o1.getMessage().getCreatedAt().compareTo(o2.getMessage().getCreatedAt());
    }
  }
}