package com.teamagam.gimelgimel.app.Alerts.viewmodel;

import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.domain.alerts.InformNewAlertsInteractor;
import com.teamagam.gimelgimel.domain.alerts.InformNewAlertsInteractorFactory;

import javax.inject.Inject;

@PerActivity
public class BubbleAlertsViewModel {

    private final InformNewAlertsInteractorFactory mAlertFactory;
    private InformNewAlertsInteractor mAlertInteractor;
    private AlertsInformer mAlertInformer;

    @Inject
    Navigator mNavigator;

    @Inject
    public BubbleAlertsViewModel(InformNewAlertsInteractorFactory alertFactory,
                                 InformNewAlertsInteractor alertInteractor) {
        mAlertFactory = alertFactory;
        mAlertInteractor = alertInteractor;
    }

    public void setAlertsDisplayer(BubbleAlertsViewModel.AlertsInformer alertsInformer){
        mAlertInformer = alertsInformer;
    }

    private void startBubbleAlerts() {
        mAlertInteractor = mAlertFactory.create(
                new InformNewAlertsInteractor.Informer() {
                    @Override
                    public void inform(Object obj) {
                        mAlertInformer.informNewBubbleAlert();
                    }
                });
        mAlertInteractor.execute();
    }
    private void stopBubbleAlerts() {
        mAlertInformer.stopDisplayNewBubbleAlert();
        mAlertInteractor.unsubscribe();
    }

    public interface AlertsInformer {

        void informNewBubbleAlert();
        void stopDisplayNewBubbleAlert();
    }
}


