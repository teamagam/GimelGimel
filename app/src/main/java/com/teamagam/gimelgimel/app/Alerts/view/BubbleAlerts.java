package com.teamagam.gimelgimel.app.Alerts.view;

import android.app.Activity;
import android.widget.Toolbar;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.Alerts.viewmodel.BubbleAlertsViewModel;


/**
 * Created by Admin on 29/12/2016.
 */

public class BubbleAlerts implements BubbleAlertsViewModel.AlertsInformer{

    private Activity mActivity;

    private ValueAnimator mColorAnimation;

    public BubbleAlerts(Activity activity){
        mActivity = activity;
    }

    private void startInformNewBubbleAlertAnimation() {

        mColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), R.color.blue, R.color.red);

        mColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                Toolbar toolbar = (Toolbar)mActivity.findViewById(R.id.main_toolbar);
                toolbar.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });

        mColorAnimation.setDuration(1000);
        mColorAnimation.setStartDelay(0);
        mColorAnimation.start();
    }

    @Override
    public void informNewBubbleAlert() {startInformNewBubbleAlertAnimation();}

    @Override
    public void stopDisplayNewBubbleAlert() {
        // mColorAnimation.cancel();
        }

}
