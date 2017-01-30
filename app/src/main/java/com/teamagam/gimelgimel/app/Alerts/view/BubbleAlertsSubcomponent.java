package com.teamagam.gimelgimel.app.Alerts.view;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.Alerts.viewmodel.BubbleAlertsViewModel;
import com.teamagam.gimelgimel.app.Alerts.viewmodel.BubbleAlertsViewModelFactory;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;

import javax.inject.Inject;

public class BubbleAlertsSubcomponent extends ActivitySubcomponent {

    @Inject
    BubbleAlertsViewModelFactory mBubbleAlertsViewModelFactory;

    private final CharSequence mToolbarOriginalTitle;
    private final BubbleAlertsViewModel mBubbleAlertsViewModel;
    private final Toolbar mToolbar;


    public BubbleAlertsSubcomponent(Activity activity) {
        mToolbar = (Toolbar) activity.findViewById(R.id.main_toolbar);
        mToolbarOriginalTitle = mToolbar.getTitle();

        ((MainActivity) activity).getMainActivityComponent().inject(this);

        mBubbleAlertsViewModel = createViewModel(activity);
    }

    @Override
    public void onStart() {
        mBubbleAlertsViewModel.start();
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolbarClicked();
            }
        });
    }

    @Override
    public void onStop() {
        mBubbleAlertsViewModel.stop();
        mToolbar.setOnClickListener(null);
    }

    private BubbleAlertsViewModel createViewModel(Activity activity) {
        MyTitleSetter titleSetter = new MyTitleSetter();
        MyToolbarAnimator actionbarAnimator = createActionbarAnimator(activity);
        return mBubbleAlertsViewModelFactory.create(actionbarAnimator, titleSetter);
    }

    private MyToolbarAnimator createActionbarAnimator(Activity activity) {
        return new MyToolbarAnimator(mToolbar, activity.getResources());
    }

    private void onToolbarClicked() {
        mBubbleAlertsViewModel.onToolbarClick();
    }

    private class MyToolbarAnimator implements BubbleAlertsViewModel.ToolbarAnimator {

        private static final int ANIMATION_START_DELAY_MS = 0;
        private static final int ANIMATION_DURATION_MS = 1000;

        private ValueAnimator mColorAnimation;
        private final int mToolbarOriginalColor;
        private final int mToolbarAnimationPeakColor;

        private boolean mIsAnimating;


        private MyToolbarAnimator(Toolbar toolbar, Resources resources) {
            mToolbarOriginalColor = resources.getColor(R.color.colorPrimaryDark, null);
            mToolbarAnimationPeakColor =
                    resources.getColor(R.color.alerts_informing_peak_color, null);

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

    private class MyTitleSetter implements BubbleAlertsViewModel.TitleSetter {
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
