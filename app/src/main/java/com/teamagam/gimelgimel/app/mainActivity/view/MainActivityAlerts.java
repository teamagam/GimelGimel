package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.AlertsViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 11/30/2016.
 */

public class MainActivityAlerts implements AlertsViewModel.AlertsDisplayer {

    AppLogger sLogger = AppLoggerFactory.create(MainActivityAlerts.class);

    @BindView(R.id.no_gps_signal_text_view)
    TextView mNoGpsTextView;
    @BindView(R.id.no_network_text_view)
    TextView mNoNetworkTextView;

    @Inject
    AlertsViewModel mViewModel;

    private Activity mActivity;

    public MainActivityAlerts(Activity activity) {
        mActivity = activity;
        ((MainActivity) activity).getMainActivityComponent().inject(this);
        ButterKnife.bind(this, activity);
        mViewModel.setAlertsDisplayer(this);
    }

    @Override
    public void displayGpsConnected() {
        hideAlertTextView(mNoGpsTextView);
    }

    @Override
    public void displayGpsDisconnected() {
        displayAlertTextView(mNoGpsTextView);
    }

    @Override
    public void displayDataConnected() {
        hideAlertTextView(mNoNetworkTextView);
    }

    @Override
    public void displayDataDisconnected() {
        displayAlertTextView(mNoNetworkTextView);
    }

    public void onStart(){
        mViewModel.start();
    }

    public void onStop(){
        mViewModel.stop();
    }

    private void displayAlertTextView(TextView textview) {
        textview.setVisibility(View.VISIBLE);
        textview.bringToFront();
    }

    private void hideAlertTextView(TextView textView) {
        textView.setVisibility(View.GONE);
    }

}