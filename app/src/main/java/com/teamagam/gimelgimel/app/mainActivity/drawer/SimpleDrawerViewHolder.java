package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerViewHolder;

public class SimpleDrawerViewHolder extends BaseRecyclerViewHolder {

  @BindView(R.id.drawer_list_item_text)
  TextView mTextView;

  public SimpleDrawerViewHolder(View itemView) {
    super(itemView);
  }

  public void setText(String text) {
    mTextView.setText(text);
  }

  public void setTextColor(int colorId) {
    mTextView.setTextColor(ContextCompat.getColor(mTextView.getContext(), colorId));
  }
}