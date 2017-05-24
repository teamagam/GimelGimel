package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.ConnectivityAlertsViewModel;
import javax.inject.Inject;

public class MainActivityConnectivityAlerts extends ActivitySubcomponent
    implements ConnectivityAlertsViewModel.ConnectivityAlertsDisplayer {

  @BindView(R.id.alerts_text_view)
  TextView mAlertsTextView;

  @Inject
  ConnectivityAlertsViewModel mViewModel;

  public MainActivityConnectivityAlerts(Activity activity) {
    ((MainActivity) activity).getMainActivityComponent().inject(this);
    ButterKnife.bind(this, activity);
    mViewModel.setAlertsDisplayer(this);
  }

  @Override
  public void onStart() {
    mViewModel.start();
  }

  @Override
  public void onStop() {
    mViewModel.stop();
  }

  @Override
  public void displayAlerts(String alertText) {
    mAlertsTextView.setText(alertText);
    mAlertsTextView.setVisibility(View.VISIBLE);
    mAlertsTextView.bringToFront();
  }

  @Override
  public void hideAlerts() {
    mAlertsTextView.setVisibility(View.GONE);
  }
}
