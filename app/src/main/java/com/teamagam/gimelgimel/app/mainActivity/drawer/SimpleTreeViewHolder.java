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

public class SimpleTreeViewHolder extends TreeNode.BaseNodeViewHolder<LayersNodeDisplayer.Node> {

  @BindView(R.id.drawer_layer_node_title)
  TextView mTitleTextView;

  @BindView(R.id.drawer_layer_node_icon)
  ImageView mIconImageView;

  @BindView(R.id.drawer_layer_node_expand_icon)
  ImageView mExpandImageView;

  @BindView(R.id.drawer_layer_node_expand_visibility)
  ImageView mVisibilityImageView;

  public SimpleTreeViewHolder(Context context) {
    super(context);
  }

  @Override
  public View createNodeView(TreeNode treeNode, LayersNodeDisplayer.Node layerNode) {
    View view = LayoutInflater.from(context).inflate(R.layout.drawer_layers_node_view, null, false);
    ButterKnife.bind(this, view);

    if (isFolder(layerNode)) {
      mExpandImageView.setVisibility(View.VISIBLE);
      mVisibilityImageView.setVisibility(View.GONE);
    } else {
      mExpandImageView.setVisibility(View.INVISIBLE);
      mVisibilityImageView.setVisibility(View.VISIBLE);
    }

    setExpandableIcon(treeNode.isExpanded());
    setVisibilityIcon(layerNode.isSelected());

    mTitleTextView.setText(layerNode.getTitle());
    mIconImageView.setImageDrawable(layerNode.getIcon());
    setClickListener(mVisibilityImageView, layerNode.getOnListingClickListener());
    setClickListener(mIconImageView, layerNode.getOnIconClickListener());

    return view;
  }

  @Override
  public void toggle(boolean active) {
    super.toggle(active);
    setExpandableIcon(active);
  }

  public void setSelected(boolean isSelected) {
    setVisibilityIcon(isSelected);
  }

  private boolean isFolder(LayersNodeDisplayer.Node layerNode) {
    return !layerNode.hasParent();
  }

  private void setExpandableIcon(boolean isExpanded) {
    mExpandImageView.setImageDrawable(getExpandStateDrawable(isExpanded));
  }

  private Drawable getExpandStateDrawable(boolean isExpanded) {
    return ContextCompat.getDrawable(context, getExpandDrawableId(isExpanded));
  }

  private int getExpandDrawableId(boolean isExpanded) {
    return isExpanded ? R.drawable.ic_folder_open : R.drawable.ic_folder;
  }

  private void setVisibilityIcon(boolean isVisible) {
    mVisibilityImageView.setImageDrawable(getVisibilityDrawable(isVisible));
  }

  private Drawable getVisibilityDrawable(boolean selected) {
    int drawableResId = selected ? R.drawable.ic_visibility_on : R.drawable.ic_visibility_off;
    return ContextCompat.getDrawable(context, drawableResId);
  }

  private void setClickListener(View view, View.OnClickListener clickListener) {
    if (clickListener != null) {
      view.setOnClickListener(clickListener);
    }
  }
}
