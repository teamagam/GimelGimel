package com.teamagam.gimelgimel.app.notifications.viewModel;

import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.notifications.SyncDataConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.SyncDataConnectivityStatusInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.SyncGpsConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.SyncGpsConnectivityStatusInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;

/**
 * Manages GPS and Data connectivity status displaying (at the view-model level)
 */
public class AlertsViewModel {

    private final SyncGpsConnectivityStatusInteractorFactory mGpsFactory;
    private final SyncDataConnectivityStatusInteractorFactory mDataFactory;
    private final AlertsDisplayer mAlertsDisplayer;

    private SyncGpsConnectivityStatusInteractor mGpsStatusInteractor;
    private SyncDataConnectivityStatusInteractor mDataStatusInteractor;

    public AlertsViewModel(AlertsDisplayer alertsDisplayer,
                           SyncGpsConnectivityStatusInteractorFactory gpsFactory,
                           SyncDataConnectivityStatusInteractorFactory dataFactory) {
        mGpsFactory = gpsFactory;
        mDataFactory = dataFactory;
        mAlertsDisplayer = alertsDisplayer;
    }

    public void start() {
        startGpsAlerts();
        startDataAlerts();
    }

    public void stop() {
        stopGpsAlerts();
        stopDataAlerts();
    }

    private void startDataAlerts() {
        mDataStatusInteractor = mDataFactory.create(new DataStatusDisplaySubscriber());
        mDataStatusInteractor.execute();
    }

    private void startGpsAlerts() {
        mGpsStatusInteractor = mGpsFactory.create(new GpsStatusDisplaySubscriber());
        mGpsStatusInteractor.execute();
    }

    private void stopDataAlerts() {
        mDataStatusInteractor.unsubscribe();
    }

    private void stopGpsAlerts() {
        mGpsStatusInteractor.unsubscribe();
    }


    public interface AlertsDisplayer {

        void displayGpsConnected();

        void displayGpsDisconnected();

        void displayDataConnected();

        void displayDataDisconnected();
    }

    private class GpsStatusDisplaySubscriber extends SimpleSubscriber<ConnectivityStatus> {
        @Override
        public void onNext(ConnectivityStatus gpsConnectivityStatus) {
            if (gpsConnectivityStatus.isConnected()) {
                mAlertsDisplayer.displayGpsConnected();
            } else {
                mAlertsDisplayer.displayGpsDisconnected();
            }
        }
    }

    private class DataStatusDisplaySubscriber extends SimpleSubscriber<ConnectivityStatus> {
        @Override
        public void onNext(ConnectivityStatus dataConnectivityStatus) {
            if (dataConnectivityStatus.isConnected()) {
                mAlertsDisplayer.displayDataConnected();
            } else {
                mAlertsDisplayer.displayDataDisconnected();
            }
        }
    }
}
