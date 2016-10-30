package com.teamagam.gimelgimel.app.viewModels;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.notifications.SyncGpsConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.SyncGpsConnectivityStatusInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.entity.GpsConnectivityStatus;

import javax.inject.Inject;

/**
 * Manages GPS and Data connectivity status displaying (at the view-model level)
 */
@PerActivity
public class AlertsViewModel {

    @Inject
    SyncGpsConnectivityStatusInteractorFactory mFactory;

    private AlertsDisplayer mAlertsDisplayer;
    private SyncGpsConnectivityStatusInteractor mGpsStatusInteractor;

    public AlertsViewModel(AlertsDisplayer alertsDisplayer) {
        mAlertsDisplayer = alertsDisplayer;
    }

    public void start() {
        mGpsStatusInteractor = mFactory.create(new GpsStatusDisplaySubscriber());
        mGpsStatusInteractor.execute();
    }

    public void stop() {
        mGpsStatusInteractor.unsubscribe();
    }


    public interface AlertsDisplayer {

        void displayGpsConnected();

        void displayGpsDisconnected();

        //Data connectivity should be handled through here as-well
    }

    private class GpsStatusDisplaySubscriber extends SimpleSubscriber<GpsConnectivityStatus> {
        @Override
        public void onNext(GpsConnectivityStatus gpsConnectivityStatus) {
            if (gpsConnectivityStatus.isConnected()) {
                mAlertsDisplayer.displayGpsConnected();
            } else {
                mAlertsDisplayer.displayGpsDisconnected();
            }
        }
    }
}
