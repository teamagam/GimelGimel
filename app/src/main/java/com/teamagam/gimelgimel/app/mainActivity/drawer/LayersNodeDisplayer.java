package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.graphics.drawable.Drawable;
import android.view.View;
import java.util.UUID;

public interface LayersNodeDisplayer {

  void addNode(Node node);

  void setNodeSelectionState(String nodeId, boolean isSelected);

  class Node {

    private String mId;
    private String mParentId;
    private boolean mIsSelected;
    private String mTitle;
    private Drawable mIcon;
    private View.OnClickListener mOnListingClickListener;
    private View.OnClickListener mOnIconClickListener;

    private Node(String id,
        String parentId,
        boolean isSelected,
        String title,
        Drawable icon,
        View.OnClickListener onListingClickListener,
        View.OnClickListener onIconClickListener) {
      mId = id;
      mParentId = parentId;
      mIsSelected = isSelected;
      mTitle = title;
      mIcon = icon;
      mOnListingClickListener = onListingClickListener;
      mOnIconClickListener = onIconClickListener;
    }

    public String getId() {
      return mId;
    }

    public String getParentId() {
      return mParentId;
    }

    public boolean isSelected() {
      return mIsSelected;
    }

    public String getTitle() {
      return mTitle;
    }

    public Drawable getIcon() {
      return mIcon;
    }

    public View.OnClickListener getOnListingClickListener() {
      return mOnListingClickListener;
    }

    public View.OnClickListener getOnIconClickListener() {
      return mOnIconClickListener;
    }
  }

  class NodeBuilder {
    private String mId = UUID.randomUUID().toString();
    private String mParentId = "";
    private boolean mIsSelected = false;
    private String mTitle;
    private Drawable mIcon = null;
    private View.OnClickListener mOnListingClickListener;
    private View.OnClickListener mOnIconClickListener = null;

    public NodeBuilder setParentId(String parentId) {
      mParentId = parentId;
      return this;
    }

    public NodeBuilder setTitle(String title) {
      mTitle = title;
      return this;
    }

    public NodeBuilder setIcon(Drawable icon) {
      mIcon = icon;
      return this;
    }

    public NodeBuilder setIsSelected(boolean isSelected) {
      mIsSelected = isSelected;
      return this;
    }

    public NodeBuilder setOnListingClickListener(View.OnClickListener onListingClickListener) {
      mOnListingClickListener = onListingClickListener;
      return this;
    }

    public NodeBuilder setOnIconClickListener(View.OnClickListener onIconClickListener) {
      mOnIconClickListener = onIconClickListener;
      return this;
    }

    public LayersNodeDisplayer.Node createNode() {
      return new LayersNodeDisplayer.Node(mId, mParentId, mIsSelected, mTitle, mIcon,
          mOnListingClickListener, mOnIconClickListener);
    }
  }
}
