package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.app.Activity;

import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.notifications.ConnectivityDisplayer;
import com.teamagam.gimelgimel.domain.notifications.DisplayDataConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.DisplayDataConnectivityStatusInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.DisplayGpsConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.DisplayGpsConnectivityStatusInteractorFactory;

import javax.inject.Inject;

/**
 * Manages GPS and Data connectivity status displaying (at the view-model level)
 */
@PerActivity
public class AlertsViewModel {

    private final DisplayGpsConnectivityStatusInteractorFactory mGpsFactory;
    private final DisplayDataConnectivityStatusInteractorFactory mDataFactory;
    private AlertsDisplayer mAlertsDisplayer;

    private DisplayGpsConnectivityStatusInteractor mGpsStatusInteractor;
    private DisplayDataConnectivityStatusInteractor mDataStatusInteractor;

    @Inject
    Navigator mNavigator;

    @Inject
    LocationFetcher mLocationFetcher;

    @Inject
    public AlertsViewModel(DisplayGpsConnectivityStatusInteractorFactory gpsFactory,
                           DisplayDataConnectivityStatusInteractorFactory dataFactory) {
        mGpsFactory = gpsFactory;
        mDataFactory = dataFactory;
    }

    public void setAlertsDisplayer(AlertsDisplayer alertsDisplayer){
        mAlertsDisplayer = alertsDisplayer;
    }

    public void start() {
        initGpsStatus();
        startGpsAlerts();
        startDataAlerts();
    }

    public void stop() {
        stopGpsAlerts();
        stopDataAlerts();
    }

    private void initGpsStatus() {
        if (!mLocationFetcher.isGpsProviderEnabled()) {
            mAlertsDisplayer.displayGpsDisconnected();
            mNavigator.navigateToTurnOnGPSDialog(mAlertsDisplayer.getActivity());
        }
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

        Activity getActivity();
    }
}
