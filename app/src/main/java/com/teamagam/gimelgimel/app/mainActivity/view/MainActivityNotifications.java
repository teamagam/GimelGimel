package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;

import com.teamagam.gimelgimel.app.mainActivity.viewmodel.MainNotificationsViewModel;
import com.teamagam.gimelgimel.app.mainActivity.MainActivity;

import javax.inject.Inject;

/**
 * Created on 9/22/2016.
 * TODO: complete text
 */
public class MainActivityNotifications implements MainNotificationsViewModel.IMessageNotificationView {

    @Inject
    MainNotificationsViewModel mViewModel;
    private Activity mActivity;

    public MainActivityNotifications(Activity activity) {
        mActivity = activity;
        ((MainActivity) mActivity).getMainActivityComponent().inject(this);

        mViewModel.setView(this);
    }

    public void onStart() {
        mViewModel.start();
    }

    public void onStop() {
        mViewModel.stop();
    }

    public void showMessageNotification(String msgText, int msgColor) {
        Snackbar snackbar = Snackbar.make(mActivity.findViewById(android.R.id.content), msgText,
                Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(mActivity, msgColor));
        snackbar.show();
    }
}
