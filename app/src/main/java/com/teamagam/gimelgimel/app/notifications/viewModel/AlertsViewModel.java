package com.teamagam.gimelgimel.app.notifications.viewModel;

import com.teamagam.gimelgimel.domain.notifications.ConnectivityDisplayer;
import com.teamagam.gimelgimel.domain.notifications.DisplayDataConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.DisplayDataConnectivityStatusInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.DisplayGpsConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.DisplayGpsConnectivityStatusInteractorFactory;

/**
 * Manages GPS and Data connectivity status displaying (at the view-model level)
 */
public class AlertsViewModel {

    private final DisplayGpsConnectivityStatusInteractorFactory mGpsFactory;
    private final DisplayDataConnectivityStatusInteractorFactory mDataFactory;
    private final AlertsDisplayer mAlertsDisplayer;

    private DisplayGpsConnectivityStatusInteractor mGpsStatusInteractor;
    private DisplayDataConnectivityStatusInteractor mDataStatusInteractor;


    public AlertsViewModel(AlertsDisplayer alertsDisplayer,
                           DisplayGpsConnectivityStatusInteractorFactory gpsFactory,
                           DisplayDataConnectivityStatusInteractorFactory dataFactory) {
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
        mDataStatusInteractor = mDataFactory.create(
                new ConnectivityDisplayer() {
                    @Override
                    public void connectivityOn() {
                        mAlertsDisplayer.displayDataConnected();
                    }

                    @Override
                    public void connectivityOff() {
                        mAlertsDisplayer.displayDataDisconnected();
                    }
                });
        mDataStatusInteractor.execute();
    }

    private void startGpsAlerts() {
        mGpsStatusInteractor = mGpsFactory.create(
                new ConnectivityDisplayer() {
                    @Override
                    public void connectivityOn() {
                        mAlertsDisplayer.displayGpsConnected();
                    }

                    @Override
                    public void connectivityOff() {
                        mAlertsDisplayer.displayGpsDisconnected();
                    }
                });
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
}
