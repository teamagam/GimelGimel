package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * TODO: add class summary notes
 */

public class SimpleTreeViewHolder extends TreeNode.BaseNodeViewHolder<LayersNodeDisplayer.Node> {

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
    mExpandImageView.setImageDrawable(getExpandDrawable(node.isExpanded()));

    mTitleTextView.setText(value.getTitle());
    mTitleTextView.setTextColor(getTextColor(value));

    mIconImageView.setImageDrawable(value.getIcon());

    setClickListener(mTitleTextView, value.getOnListingClickListener());
    setClickListener(mIconImageView, value.getOnIconClickListener());

    return view;
  }

  @Override
  public void toggle(boolean active) {
    super.toggle(active);
    mExpandImageView.setImageDrawable(getExpandDrawable(active));
  }

  public void setSelected(boolean selected) {

  }

  private Drawable getExpandDrawable(boolean isExpanded) {
    return isExpanded ? ContextCompat.getDrawable(context, R.drawable.ic_down_arrow)
        : ContextCompat.getDrawable(context, R.drawable.ic_dash);
  }

  private int getTextColor(LayersNodeDisplayer.Node value) {
    return value.isSelected() ? ContextCompat.getColor(context, R.color.themeBlue)
        : ContextCompat.getColor(context, R.color.black);
  }

  private void setClickListener(View view, View.OnClickListener clickListener) {
    if (clickListener != null) {
      view.setOnClickListener(clickListener);
    }
  }
}
