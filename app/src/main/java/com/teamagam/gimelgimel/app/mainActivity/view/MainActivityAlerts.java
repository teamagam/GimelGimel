package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.AlertsViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 11/30/2016.
 */

public class MainActivityAlerts extends ActivitySubcomponent implements AlertsViewModel.AlertsDisplayer {

    @BindView(R.id.no_gps_signal_text_view)
    TextView mNoGpsTextView;
    @BindView(R.id.no_network_text_view)
    TextView mNoNetworkTextView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolBar;


    @Inject
    AlertsViewModel mViewModel;

    MainActivityAlerts(Activity activity) {
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

    @Override
    public void onStart() {
        mViewModel.start();
    }

    @Override
    public void onStop() {
        mViewModel.stop();
    }

    private void displayAlertTextView(TextView textview) {
        textview.setVisibility(View.VISIBLE);
        textview.bringToFront();
    }

    private void changeToolBarColorForNewBubbleAlert() {

        Integer colorFrom = ContextCompat.getColor(mActivity.getBaseContext(), R.color.blue);
        Integer colorTo = ContextCompat.getColor(mActivity.getBaseContext(), R.color.red);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mToolBar.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });

        colorAnimation.setDuration(1000);
        colorAnimation.setStartDelay(0);
        colorAnimation.start();
    }

    private void hideAlertTextView(TextView textView) {
        textView.setVisibility(View.GONE);
    }
}
