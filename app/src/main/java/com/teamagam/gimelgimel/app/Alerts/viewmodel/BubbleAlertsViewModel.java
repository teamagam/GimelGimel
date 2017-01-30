package com.teamagam.gimelgimel.app.Alerts.viewmodel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.InformNewAlertsInteractor;
import com.teamagam.gimelgimel.domain.alerts.InformNewAlertsInteractorFactory;
import com.teamagam.gimelgimel.domain.alerts.UpdateLatestInformedAlertTimeInteractorFactory;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;

@AutoFactory
public class BubbleAlertsViewModel {

    private final InformNewAlertsInteractorFactory mInformNewAlertsInteractorFactory;
    private final UpdateLatestInformedAlertTimeInteractorFactory mUpdateInformedFactory;
    private final ToolbarAnimator mToolbarAnimator;
    private final TitleSetter mTitleSetter;

    private InformNewAlertsInteractor mInformNewAlertsInteractor;

    private Alert mLatestDisplayedAlert;

    public BubbleAlertsViewModel(
            @Provided InformNewAlertsInteractorFactory alertFactory,
            @Provided UpdateLatestInformedAlertTimeInteractorFactory updateInformedFactory,
            ToolbarAnimator toolbarAnimator,
            TitleSetter titleSetter) {
        mInformNewAlertsInteractorFactory = alertFactory;
        mUpdateInformedFactory = updateInformedFactory;
        mToolbarAnimator = toolbarAnimator;
        mTitleSetter = titleSetter;
    }

    public void start() {
        mInformNewAlertsInteractor = mInformNewAlertsInteractorFactory.create(new MyDisplayer());
        mInformNewAlertsInteractor.execute();
    }

    public void stop() {
        if (mInformNewAlertsInteractor != null) {
            mInformNewAlertsInteractor.unsubscribe();
        }

        if (mToolbarAnimator.isAnimating()) {
            stopToolbarAnimation();
        }
    }

    public void onToolbarClick() {
        if (mToolbarAnimator.isAnimating()) {
            updateLatestInformedAlert();
            restoreToolbar();
        }
    }

    private void updateLatestInformedAlert() {
        mUpdateInformedFactory.create(mLatestDisplayedAlert).execute();
    }

    private void restoreToolbar() {
        stopToolbarAnimation();
        mTitleSetter.restoreDefault();
    }

    private void stopToolbarAnimation() {
        mToolbarAnimator.stopActionBarAnimation();
    }

    private class MyDisplayer implements InformNewAlertsInteractor.Displayer {
        @Override
        public void display(Alert alert) {
            mLatestDisplayedAlert = alert;
            animateIfNeeded();
            mTitleSetter.setTitle(createTitle(alert));
        }

        private String createTitle(Alert alert) {
            return "Alert!";
        }

        private void animateIfNeeded() {
            if (!mToolbarAnimator.isAnimating()) {
                mToolbarAnimator.animateActionBar();
            }
        }
    }

    public interface ToolbarAnimator {
        void animateActionBar();

        void stopActionBarAnimation();

        boolean isAnimating();
    }

    public interface TitleSetter {
        void setTitle(String title);

        void restoreDefault();
    }
}


