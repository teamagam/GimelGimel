package com.teamagam.gimelgimel.app.mainActivity.viewmodel;


import android.content.Context;
import android.text.TextUtils;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.domain.base.interactors.Interactor;
import com.teamagam.gimelgimel.domain.notifications.ConnectivityDisplayer;
import com.teamagam.gimelgimel.domain.notifications.Display3GConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.Display3GConnectivityStatusInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.DisplayDataConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.DisplayDataConnectivityStatusInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.DisplayGpsConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.DisplayGpsConnectivityStatusInteractorFactory;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * Manages GPS and Data connectivity status displaying (at the view-model level)
 */
@PerActivity
public class ConnectivityAlertsViewModel {

    private final DisplayGpsConnectivityStatusInteractorFactory mGpsFactory;
    private final DisplayDataConnectivityStatusInteractorFactory mDataFactory;
    private final Display3GConnectivityStatusInteractorFactory m3GStatusFactory;

    private final String mGpsAlert;
    private final String mDataAlert;
    private final String m3GAlert;

    private final Set<String> mDisplayedAlerts;

    private ConnectivityAlertsDisplayer mConnectivityAlertsDisplayer;

    private DisplayGpsConnectivityStatusInteractor mGpsStatusInteractor;
    private DisplayDataConnectivityStatusInteractor mDataStatusInteractor;
    private Display3GConnectivityStatusInteractor m3GInteractor;

    @Inject
    public ConnectivityAlertsViewModel(Context context,
                                       DisplayGpsConnectivityStatusInteractorFactory gpsFactory,
                                       DisplayDataConnectivityStatusInteractorFactory dataFactory,
                                       Display3GConnectivityStatusInteractorFactory threeGFactory) {
        mGpsFactory = gpsFactory;
        mDataFactory = dataFactory;
        m3GStatusFactory = threeGFactory;
        mGpsAlert = context.getString(R.string.alert_no_gps_signal);
        mDataAlert = context.getString(R.string.alert_no_network);
        m3GAlert = context.getString(R.string.alert_no_3g_network);
        mDisplayedAlerts = new HashSet<>();
    }

    public void setAlertsDisplayer(ConnectivityAlertsDisplayer connectivityAlertsDisplayer) {
        mConnectivityAlertsDisplayer = connectivityAlertsDisplayer;
    }

    public void start() {
        startGpsAlerts();
        startDataAlerts();
        start3GAlerts();
    }

    public void stop() {
        unsubscribe(mDataStatusInteractor, mGpsStatusInteractor, m3GInteractor);
    }

    private void startGpsAlerts() {
        mGpsStatusInteractor = mGpsFactory.create(new AlertsDisplayer(mGpsAlert));
        mGpsStatusInteractor.execute();
    }

    private void startDataAlerts() {
        mDataStatusInteractor = mDataFactory.create(new AlertsDisplayer(mDataAlert));
        mDataStatusInteractor.execute();
    }

    private void start3GAlerts() {
        m3GInteractor = m3GStatusFactory.create(new AlertsDisplayer(m3GAlert));
        m3GInteractor.execute();
    }

    private void unsubscribe(Interactor... interactors) {
        for (Interactor interactor :
                interactors) {
            if (interactor != null) {
                interactor.unsubscribe();
            }
        }
    }


    public interface ConnectivityAlertsDisplayer {

        void displayAlerts(String alertText);

        void hideAlerts();
    }

    private class AlertsDisplayer implements ConnectivityDisplayer {

        private static final String ALERTS_SEPARATOR = ", ";
        private final String mAlertText;

        AlertsDisplayer(String alertText) {
            mAlertText = alertText;
        }

        @Override
        public void connectivityOn() {
            mDisplayedAlerts.remove(mAlertText);
            refreshAlerts();
        }

        @Override
        public void connectivityOff() {
            mDisplayedAlerts.add(mAlertText);
            refreshAlerts();
        }

        private void refreshAlerts() {
            if (mDisplayedAlerts.isEmpty()) {
                mConnectivityAlertsDisplayer.hideAlerts();
            } else {
                mConnectivityAlertsDisplayer.displayAlerts(buildAlertsText());
            }
        }

        private String buildAlertsText() {
            return TextUtils.join(ALERTS_SEPARATOR, mDisplayedAlerts);
        }
    }
}
