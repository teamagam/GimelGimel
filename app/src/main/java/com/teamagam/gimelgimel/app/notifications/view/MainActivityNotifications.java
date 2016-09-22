package com.teamagam.gimelgimel.app.notifications.view;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;

import com.teamagam.gimelgimel.app.notifications.viewModel.MainNotificationsViewModel;
import com.teamagam.gimelgimel.app.view.MainActivity;

import javax.inject.Inject;

/**
 * Created on 9/22/2016.
 * TODO: complete text
 */
public class MainActivityNotifications implements MainNotificationsViewModel.IMessageNotificationView{

    private Activity mActivity;

    @Inject
    MainNotificationsViewModel mViewModel;

    public MainActivityNotifications(Activity activity) {
        mActivity = activity;
        ((MainActivity) mActivity).getMainActivityComponent().inject(this);

        mViewModel.setView(this);
    }

    public void onStart(){
        mViewModel.start();
    }

    public void onStop(){
        mViewModel.stop();
    }

    public void showMessageNotification(String msgText, int msgColor) {
//        Toast.makeText(mActivity, msgText, Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar.make(mActivity.findViewById(android.R.id.content), msgText,
                Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(mActivity, msgColor));
        snackbar.show();
    }
}
