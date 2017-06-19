package com.teamagam.gimelgimel.app.Alerts.view;

import android.app.Activity;
import com.tapadoo.alerter.Alert;
import com.tapadoo.alerter.Alerter;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.Alerts.viewmodel.AlertsViewModel;
import com.teamagam.gimelgimel.app.Alerts.viewmodel.AlertsViewModelFactory;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import javax.inject.Inject;

public class AlertsSubcomponent extends ActivitySubcomponent {

  private final Activity mActivity;
  private final AlertsViewModel mAlertsViewModel;
  @Inject
  AlertsViewModelFactory mAlertsViewModelFactory;

  public AlertsSubcomponent(Activity activity) {
    mActivity = activity;

    ((MainActivity) mActivity).getMainActivityComponent().inject(this);

    mAlertsViewModel = createViewModel();
  }

  @Override
  public void onStart() {
    mAlertsViewModel.start();
  }

  @Override
  public void onStop() {
    mAlertsViewModel.stop();
  }

  private AlertsViewModel createViewModel() {
    AlertDisplayer alertDisplayer = createAlertDisplayer();
    return mAlertsViewModelFactory.create(alertDisplayer);
  }

  private AlertDisplayer createAlertDisplayer() {
    return new AlertDisplayer();
  }

  private void onAlertClicked() {
    mAlertsViewModel.onAlertClick();
  }

  private class AlertDisplayer implements AlertsViewModel.AlertDisplayer {

    private boolean mIsShowingAlert;
    private Alert mCurrentAlert;

    public AlertDisplayer() {
      mIsShowingAlert = false;
    }

    public void showAlert(String title, String description) {
      mCurrentAlert = getBaseAlerter().setTitle(title).setText(description).show();

      mIsShowingAlert = true;
    }

    public void hideAlert() {
      if (mCurrentAlert != null) {
        mCurrentAlert.hide();
        mIsShowingAlert = false;
      }
    }

    public boolean isShowingAlert() {
      return mIsShowingAlert;
    }

    private Alerter getBaseAlerter() {
      return Alerter.create(mActivity)
          .enableIconPulse(true)
          .enableInfiniteDuration(true)
          .setBackgroundColor(R.color.alerter_background_color)
          .setIcon(R.drawable.ic_alert)
          .setOnClickListener(v -> onAlertClicked());
    }
  }
}
