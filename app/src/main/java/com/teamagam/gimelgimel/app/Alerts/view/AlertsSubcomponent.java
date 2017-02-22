package com.teamagam.gimelgimel.app.Alerts.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.Alerts.viewmodel.AlertsViewModel;
import com.teamagam.gimelgimel.app.Alerts.viewmodel.AlertsViewModelFactory;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;

import javax.inject.Inject;

public class AlertsSubcomponent extends ActivitySubcomponent {

    @Inject
    AlertsViewModelFactory mAlertsViewModelFactory;

    private final CharSequence mToolbarOriginalTitle;
    private final AlertsViewModel mAlertsViewModel;
    private final Toolbar mToolbar;

    public AlertsSubcomponent(Activity activity) {
        mToolbar = (Toolbar) activity.findViewById(R.id.main_toolbar);
        mToolbarOriginalTitle = mToolbar.getTitle();

        ((MainActivity) activity).getMainActivityComponent().inject(this);

        mAlertsViewModel = createViewModel(activity);
    }

    @Override
    public void onStart() {
        mAlertsViewModel.start();
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolbarClicked();
            }
        });
    }

    @Override
    public void onStop() {
        mAlertsViewModel.stop();
        mToolbar.setOnClickListener(null);
    }

    private AlertsViewModel createViewModel(Activity activity) {
        ToolbarTitleSetter toolbarTitleSetter = new ToolbarTitleSetter();
        PeakColorToolbarAnimator actionbarAnimator = createActionbarAnimator(activity);
        return mAlertsViewModelFactory.create(actionbarAnimator, toolbarTitleSetter);
    }

    private PeakColorToolbarAnimator createActionbarAnimator(Activity activity) {
        return new PeakColorToolbarAnimator(mToolbar, activity);
    }

    private void onToolbarClicked() {
        mAlertsViewModel.onToolbarClick();
    }

    private class PeakColorToolbarAnimator implements AlertsViewModel.ToolbarAnimator {

        private static final int ANIMATION_START_DELAY_MS = 0;
        private static final int ANIMATION_DURATION_MS = 1000;

        private ValueAnimator mColorAnimation;
        private final int mToolbarOriginalColor;
        private final int mToolbarAnimationPeakColor;

        private boolean mIsAnimating;


        private PeakColorToolbarAnimator(Toolbar toolbar, Context context) {
            mToolbarOriginalColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
            mToolbarAnimationPeakColor =
                    ContextCompat.getColor(context, R.color.alerts_informing_peak_color);

            initializeAnimation(toolbar, mToolbarAnimationPeakColor, mToolbarOriginalColor);

            mIsAnimating = false;
        }

        @Override
        public void animateActionBar() {
            mIsAnimating = true;
            mColorAnimation.start();
        }

        @Override
        public void stopActionBarAnimation() {
            mColorAnimation.cancel();
            mToolbar.setBackgroundColor(mToolbarOriginalColor);
            mIsAnimating = false;
        }

        @Override
        public boolean isAnimating() {
            return mIsAnimating;
        }

        private void initializeAnimation(final Toolbar toolbar, int from, int to) {
            mColorAnimation = ObjectAnimator.ofObject(toolbar, "backgroundColor",
                    new ArgbEvaluator(), from, to);

            mColorAnimation.setDuration(ANIMATION_DURATION_MS);
            mColorAnimation.setStartDelay(ANIMATION_START_DELAY_MS);
            mColorAnimation.setRepeatCount(ValueAnimator.INFINITE);
            mColorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        }
    }

    private class ToolbarTitleSetter implements AlertsViewModel.ToolbarTitleSetter {
        @Override
        public void setTitle(String title) {
            mToolbar.setTitle(title);
        }

        @Override
        public void restoreDefault() {
            mToolbar.setTitle(mToolbarOriginalTitle);
        }
    }
}
