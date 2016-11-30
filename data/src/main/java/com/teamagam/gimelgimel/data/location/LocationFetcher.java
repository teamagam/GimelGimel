package com.teamagam.gimelgimel.data.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.location.repository.GpsLocationListener;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import rx.functions.Action0;

/**
 * Handles location fetching against the system's sensors
 */
public class LocationFetcher {

    private static final Logger sLogger = LoggerFactory.create(
            LocationFetcher.class.getSimpleName());

    @StringDef({
            ProviderType.LOCATION_PROVIDER_GPS,
            ProviderType.LOCATION_PROVIDER_NETWORK,
            ProviderType.LOCATION_PROVIDER_PASSIVE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProviderType {
        String LOCATION_PROVIDER_GPS = LocationManager.GPS_PROVIDER;
        String LOCATION_PROVIDER_NETWORK = LocationManager.NETWORK_PROVIDER;
        String LOCATION_PROVIDER_PASSIVE = LocationManager.PASSIVE_PROVIDER;
    }

    private final Context mAppContext;
    private final LocationManager mLocationManager;
    private final Collection<String> mProviders;
    private final LocationListener mLocationListener;
    private final Set<GpsLocationListener> mListeners;
    private final StoppedGpsStatusDelegator mStoppedGpsStatusDelegator;
    private final UiRunner mUiRunner;

    private boolean mIsRequestingUpdates;
    private LocationSample mLastLocation;
    private long mMinSamplingFrequencyMs;
    private long mDistanceDeltaSamplingMeters;

    /**
     * @param applicationContext
     * @param minSamplingFrequencyMs         - minimum time between location samples,  in milliseconds
     * @param minDistanceDeltaSamplingMeters - minimum distance between location samples, in meters
     */
    public LocationFetcher(Context applicationContext, UiRunner uiRunner,
                           long minSamplingFrequencyMs, long minDistanceDeltaSamplingMeters) {

        if (minSamplingFrequencyMs < 0) {
            throw new IllegalArgumentException("minSamplingFrequencyMs cannot be negative");
        }

        if (minDistanceDeltaSamplingMeters < 0) {
            throw new IllegalArgumentException(
                    "minDistanceDeltaSamplingMeters cannot be a negative number");
        }

        mAppContext = applicationContext;
        mUiRunner = uiRunner;
        mMinSamplingFrequencyMs = minSamplingFrequencyMs;
        mDistanceDeltaSamplingMeters = minDistanceDeltaSamplingMeters;

        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mProviders = new ArrayList<>();
        mIsRequestingUpdates = false;

        mStoppedGpsStatusDelegator = new StoppedGpsStatusDelegator();

        mLocationListener = new LocationFetcherListener();
        mListeners = new HashSet<>();

        addProviders();
    }

    public void start() {
        requestLocationUpdates();
    }

    public void stop() {
        removeFromUpdates();
    }

    public void addListener(GpsLocationListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(GpsLocationListener listener) {
        mListeners.remove(listener);
    }

    /**
     * Registers fetcher for location updates
     */
    public void requestLocationUpdates() {
        if (mIsRequestingUpdates) {
            throw new RuntimeException("Fetcher already registered!");
        }

        checkForLocationPermission(mAppContext);

        if (mProviders.isEmpty()) {
            throw new RuntimeException("No providers added to fetcher to register with");
        }

        mUiRunner.run(() -> {
            for (String provider : mProviders) {
                requestLocationUpdates(provider);
            }
        });

        mIsRequestingUpdates = true;

        //Attach NativeGpsStatus listener
        mLocationManager.addGpsStatusListener(mStoppedGpsStatusDelegator);
    }

    @SuppressWarnings("MissingPermission")
    private void requestLocationUpdates(String provider) {
        try {
            mLocationManager.requestLocationUpdates(provider, mMinSamplingFrequencyMs,
                    mDistanceDeltaSamplingMeters, mLocationListener);
        } catch (IllegalArgumentException ex) {
            sLogger.e("Could not add provider" + provider + " (does it exist on the device?)", ex);
        }
    }

    public boolean getIsRequestingUpdates() {
        return mIsRequestingUpdates;
    }

    /**
     * Stops fetcher from receiving location updates
     */
    public void removeFromUpdates() {
        if (!mIsRequestingUpdates) {
            throw new RuntimeException("Fetcher is not registered");
        }

        checkForLocationPermission(mAppContext);

        mLocationManager.removeUpdates(mLocationListener);
        mIsRequestingUpdates = false;

        mLocationManager.removeGpsStatusListener(mStoppedGpsStatusDelegator);
    }

    /**
     * Checks whether device's GPS provider is currently enabled
     *
     * @return true iff GPS provider is enabled
     */
    public boolean isGpsProviderEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Adds provider to be used when registering the fetcher
     */
    private void addProviders() {
        if (mIsRequestingUpdates) {
            throw new RuntimeException("Cannot add providers to an already registered fetcher!");
        }

        addProvider(ProviderType.LOCATION_PROVIDER_GPS);
        addProvider(ProviderType.LOCATION_PROVIDER_NETWORK);
        addProvider(ProviderType.LOCATION_PROVIDER_PASSIVE);
    }

    private void addProvider(@ProviderType String locationProvider) {
        mProviders.add(locationProvider);
    }

    private void handleNewLocation(Location location) {
        if (isSufficientQuality(location)) {
            LocationSample locationSample = convertToLocationSample(location);
            mLastLocation = locationSample;

            notifyNewLocation(locationSample);
        } else {
            notifyNoConnection();
        }
    }

    private void notifyNewLocation(LocationSample location) {
        for (GpsLocationListener listener : mListeners) {
            listener.onNewLocation(location);
        }
    }

    private void notifyNoConnection() {
        for (GpsLocationListener listener : mListeners) {
            listener.onBadConnection();
        }
    }

    private LocationSample convertToLocationSample(Location location) {
        PointGeometry point = new PointGeometry(
                location.getLatitude(), location.getLongitude(),
                location.hasAltitude(), location.getAltitude());

        return new LocationSample(
                point, location.getTime(), location.getProvider(),
                location.hasSpeed(), location.getSpeed(),
                location.hasBearing(), location.getBearing(),
                location.hasAccuracy(), location.getAccuracy()
        );
    }


    private boolean isSufficientQuality(Location location) {
        return location.getAccuracy() < Constants.MAXIMUM_GPS_SAMPLE_DEVIATION_METERS;
    }

    private boolean isProviderExistsAndEnabled(@ProviderType String locationProvider) {
        return mLocationManager.isProviderEnabled(locationProvider);
    }

    private void checkForLocationPermission(Context context) {
        int fineLocationPermissionStatus = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (fineLocationPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("No permission granted for location fetching");
        }
    }

    /**
     * @return last known location. null if not present.
     */
    @Nullable
    public LocationSample getLastKnownLocation() {
        return mLastLocation;
    }

    /**
     * GpsConnectivityStatus.Listener implementation used to delegate it's stopped events only.
     * Those events are delegated to {@class GpsStatusChangedBroadcaster} which in-turn broadcasts
     * if needed.
     */
    public class StoppedGpsStatusDelegator implements GpsStatus.Listener {

        @Override
        public synchronized void onGpsStatusChanged(int event) {
            if (isStoppedEvent(event)) {
                notifyNoConnection();
            }
        }

        private boolean isStoppedEvent(int event) {
            return event == GpsStatus.GPS_EVENT_STOPPED;
        }
    }

    private class LocationFetcherListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LocationFetcher.this.handleNewLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    public interface UiRunner {
        void run(Action0 action);
    }
}
