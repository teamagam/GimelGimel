package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

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
public class ConnectivityAlertsViewModel {

    private final DisplayGpsConnectivityStatusInteractorFactory mGpsFactory;
    private final DisplayDataConnectivityStatusInteractorFactory mDataFactory;
    private ConnectivityAlertsDisplayer mConnectivityAlertsDisplayer;

    private DisplayGpsConnectivityStatusInteractor mGpsStatusInteractor;
    private DisplayDataConnectivityStatusInteractor mDataStatusInteractor;

    @Inject
    LocationFetcher mLocationFetcher;

    @Inject
    public ConnectivityAlertsViewModel(DisplayGpsConnectivityStatusInteractorFactory gpsFactory,
                                       DisplayDataConnectivityStatusInteractorFactory dataFactory) {
        mGpsFactory = gpsFactory;
        mDataFactory = dataFactory;
    }

    public void setAlertsDisplayer(ConnectivityAlertsDisplayer connectivityAlertsDisplayer){
        mConnectivityAlertsDisplayer = connectivityAlertsDisplayer;
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
            mConnectivityAlertsDisplayer.displayGpsDisconnected();
        }
    }

    private void startDataAlerts() {
        mDataStatusInteractor = mDataFactory.create(
                new ConnectivityDisplayer() {
                    @Override
                    public void connectivityOn() {
                        mConnectivityAlertsDisplayer.displayDataConnected();
                    }

                    @Override
                    public void connectivityOff() {
                        mConnectivityAlertsDisplayer.displayDataDisconnected();
                    }
                });
        mDataStatusInteractor.execute();
    }

    private void startGpsAlerts() {
        mGpsStatusInteractor = mGpsFactory.create(
                new ConnectivityDisplayer() {
                    @Override
                    public void connectivityOn() {
                        mConnectivityAlertsDisplayer.displayGpsConnected();
                    }

                    @Override
                    public void connectivityOff() {
                        mConnectivityAlertsDisplayer.displayGpsDisconnected();
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

    public interface ConnectivityAlertsDisplayer {

        void displayGpsConnected();

        void displayGpsDisconnected();

        void displayDataConnected();

        void displayDataDisconnected();
    }
}
