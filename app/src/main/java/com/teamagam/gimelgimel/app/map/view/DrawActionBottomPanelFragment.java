package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseFragment;

public class DrawActionBottomPanelFragment extends BaseFragment<GGApplication> {

  private Button mPositiveButton;
  private Button mCancelButton;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    mPositiveButton = (Button) view.findViewById(R.id.action_draw_positive);
    mCancelButton = (Button) view.findViewById(R.id.action_draw_cancel);
    mCancelButton.setText(getResources().getString(android.R.string.cancel));
    return view;
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_action_draw_bottom_panel;
  }

  public void setOnPositiveClickListener(Button.OnClickListener listener) {
    mPositiveButton.setOnClickListener(listener);
  }

  public void setOnCancelClickListener(Button.OnClickListener listener) {
    mCancelButton.setOnClickListener(listener);
  }

  public void setPositiveText(String text) {
    mPositiveButton.setText(text);
  }
}
