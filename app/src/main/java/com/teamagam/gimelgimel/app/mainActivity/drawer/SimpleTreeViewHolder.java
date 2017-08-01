package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.unnamed.b.atv.model.TreeNode;

public class SimpleTreeViewHolder extends TreeNode.BaseNodeViewHolder<LayersNodeDisplayer.Node> {

  private static final int FADE_TIME_MILLIS = 200;

  @BindView(R.id.drawer_layer_node_title)
  TextView mTitleTextView;

  @BindView(R.id.drawer_layer_node_icon)
  ImageView mIconImageView;

  @BindView(R.id.drawer_layer_node_expand_icon)
  ImageView mExpandImageView;

  public SimpleTreeViewHolder(Context context) {
    super(context);
  }

  @Override
  public View createNodeView(TreeNode node, LayersNodeDisplayer.Node value) {
    final LayoutInflater inflater = LayoutInflater.from(context);
    final View view = inflater.inflate(R.layout.drawer_layers_node_view, null, false);

    ButterKnife.bind(this, view);

    mExpandImageView.setVisibility(value.hasParent() ? View.GONE : View.VISIBLE);
    loadExpandImageWithAnimation(node.isExpanded());

    mTitleTextView.setText(value.getTitle());
    mTitleTextView.setTextColor(getTextColor(value.isSelected()));

    mIconImageView.setImageDrawable(value.getIcon());

    setClickListener(mTitleTextView, value.getOnListingClickListener());
    setClickListener(mIconImageView, value.getOnIconClickListener());

    return view;
  }

  @Override
  public void toggle(boolean active) {
    super.toggle(active);
    loadExpandImageWithAnimation(active);
  }

  public void setSelected(boolean selected) {
    mTitleTextView.setTextColor(getTextColor(selected));
  }

  private void loadExpandImageWithAnimation(boolean isExpanded) {
    TransitionDrawable td = new TransitionDrawable(new Drawable[] {
        getOppositeExpandStateDrawable(isExpanded), getExpandStateDrawable(isExpanded)
    });

    mExpandImageView.setImageDrawable(td);
    td.setCrossFadeEnabled(true);
    td.startTransition(FADE_TIME_MILLIS);
  }

  private Drawable getExpandStateDrawable(boolean isExpanded) {
    return ContextCompat.getDrawable(context, getExpandDrawableId(isExpanded));
  }

  private Drawable getOppositeExpandStateDrawable(boolean isExpanded) {
    return getExpandStateDrawable(!isExpanded);
  }

  private Drawable getBaseTransitionDrawable() {
    if (mExpandImageView.getDrawable() != null) {
      return mExpandImageView.getDrawable();
    }
    return new ColorDrawable(Color.TRANSPARENT);
  }

  private int getExpandDrawableId(boolean isExpanded) {
    return isExpanded ? R.drawable.ic_dash : R.drawable.ic_down_arrow;
  }

  private int getTextColor(boolean isSelected) {
    return isSelected ? ContextCompat.getColor(context, R.color.themeBlue)
        : ContextCompat.getColor(context, R.color.black);
  }

  private void setClickListener(View view, View.OnClickListener clickListener) {
    if (clickListener != null) {
      view.setOnClickListener(clickListener);
    }
  }
}
