package com.teamagam.gimelgimel.app.control.sensors;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.teamagam.gimelgimel.app.control.receivers.GpsStatusBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.utils.Constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Handles location fetching against the system's sensors
 */
public class LocationFetcher {


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

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static final String KEY_NEW_LOCATION_SAMPLE = "com.teamagam.gimelgimel.app.control.sensors.LOCATION_SMAPLE";

    private static final String LOCATION_FILTER_BROADCAST = "com.teamagam.gimelgimel.app.LocationFetcher.LOCATION_READY";
    private static final Object mLock = new Object();
    private static LocationFetcher sInstance;

    private IntentFilter mIntentFilter;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private Collection<String> mProviders;
    private boolean mIsRequestingUpdates;
    private LocationSample mLastLocation;
    private LocationListener mLocationListener;
    private GpsStatus.Listener mNativeGpsStatusListener;

    private static void checkForLocationPermission(Context context) {
        int fineLocationPermissionStatus = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (fineLocationPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("No permission granted for location fetching");
        }
    }

    /**
     * opens dialog for permission from the user. needed for API 23
     *
     * @param activity - the activity should implement onRequestPermissionsResult method for response
     */
    public static void askForLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    /**
     * Singleton pattern
     *
     * @param context
     * @return LocationFetcher instance
     */
    public static LocationFetcher getInstance(Context context) {
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new LocationFetcher(context.getApplicationContext());
            }
            return sInstance;
        }
    }

    private LocationFetcher(Context applicationContext) {
        mAppContext = applicationContext;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mProviders = new ArrayList<>();
        mIsRequestingUpdates = false;

        mIntentFilter = new IntentFilter(LocationFetcher.LOCATION_FILTER_BROADCAST);

        mNativeGpsStatusListener = new NativeGpsStatusListenerImpl();

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                handleNewLocation(location);
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
        };
    }

    /**
     * Adds provider to be used when registering the fetcher
     *
     * @param locationProvider - type of provider to use for location updates
     */
    public void addProvider(@ProviderType String locationProvider) {
        if (locationProvider == null || locationProvider.isEmpty()) {
            throw new IllegalArgumentException("location provider cannot be null or empty");
        }

        if (mIsRequestingUpdates) {
            throw new RuntimeException("Cannot add providers to an already registered fetcher!");
        }

        if (!isProviderExistsAndEnabled(locationProvider)) {
            throw new RuntimeException(
                    "Provider " + locationProvider + " is not supported/enabled on this device");
        }

        mProviders.add(locationProvider);
    }

    /**
     * Registers fetcher for location updates
     *
     * @param minSamplingFrequencyMs         - minimum time between location samples,  in milliseconds
     * @param minDistanceDeltaSamplingMeters - minimum distance between location samples, in meters
     */
    public void requestLocationUpdates(long minSamplingFrequencyMs,
                                       float minDistanceDeltaSamplingMeters) {
        if (minSamplingFrequencyMs < 0) {
            throw new IllegalArgumentException("minSamplingFrequencyMs cannot be negative");
        }

        if (minDistanceDeltaSamplingMeters < 0) {
            throw new IllegalArgumentException(
                    "minDistanceDeltaSamplingMeters cannot be a negative number");
        }

        if (mIsRequestingUpdates) {
            throw new RuntimeException("Fetcher already registered!");
        }

        checkForLocationPermission(mAppContext);

        if (mProviders.isEmpty()) {
            throw new RuntimeException("No providers added to fetcher to register with");
        }

        for (String provider : mProviders) {
            mLocationManager.requestLocationUpdates(provider, minSamplingFrequencyMs,
                    minDistanceDeltaSamplingMeters, mLocationListener);
        }

        mIsRequestingUpdates = true;

        //Attach NativeGpsStatus listener
        mLocationManager.addGpsStatusListener(mNativeGpsStatusListener);
    }

    public boolean getIsRequestingUpdates() {
        return mIsRequestingUpdates;
    }

    private void handleNewLocation(Location location) {
        notifyGpsStatus(location);

        LocationSample locationSample = new LocationSample(location);

        broadcastLocation(locationSample);

        mLastLocation = locationSample;
    }

    private boolean isProviderExistsAndEnabled(@ProviderType String locationProvider) {
        return mLocationManager.isProviderEnabled(locationProvider);
    }

    private void notifyGpsStatus(Location location) {
        float maximumAllowedDeviation = Constants.MAXIMUM_GPS_SAMPLE_DEVIATION_METERS;

        if (location.getAccuracy() < maximumAllowedDeviation) {
            mNativeGpsStatusListener.onGpsStatusChanged(GpsStatus.GPS_EVENT_STARTED);
        } else {
            mNativeGpsStatusListener.onGpsStatusChanged(GpsStatus.GPS_EVENT_STOPPED);
        }
    }

    private void broadcastLocation(LocationSample locationSample) {

        if (locationSample.getAccuracy() < Constants.MAXIMUM_GPS_SAMPLE_DEVIATION_METERS) {
            Intent broadcastIntent = new Intent(LocationFetcher.LOCATION_FILTER_BROADCAST);
            broadcastIntent.putExtra(LocationFetcher.KEY_NEW_LOCATION_SAMPLE, locationSample);
            LocalBroadcastManager.getInstance(mAppContext).sendBroadcast(broadcastIntent);
        }
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

        mLocationManager.removeGpsStatusListener(mNativeGpsStatusListener);
    }

    /**
     * used for registering receiver to get location update
     *
     * @param receiver - for updates result
     */
    public void registerReceiver(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(mAppContext).registerReceiver(receiver, mIntentFilter);
    }

    /**
     * used for unregistering receivers from Location updates
     *
     * @param receiver
     */
    public void unregisterReceiver(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(mAppContext).unregisterReceiver(receiver);
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
     * @return last known location. null if not present.
     */
    @Nullable
    public LocationSample getLastKnownLocation() {
        return mLastLocation;
    }

    /**
     * GpsStatus.Listener implementation used to delegate it's events to
     * a our custom broadcast receiver {@link GpsStatusBroadcastReceiver}.
     */
    public class NativeGpsStatusListenerImpl implements GpsStatus.Listener {

        private static final int WORKING_STATE_UNINITIALIZED = 0;
        private static final int WORKING_STATE_STOPPED = 1;
        private static final int WORKING_STATE_WORKING = 2;

        private int mLastWorkingState;

        public NativeGpsStatusListenerImpl() {
            mLastWorkingState = WORKING_STATE_UNINITIALIZED;
        }

        @Override
        public synchronized void onGpsStatusChanged(int event) {
            int workingState = extractWorkingState(event);

            if (workingState != mLastWorkingState) {
                mLastWorkingState = workingState;
                raiseCurrentStatus();
            }
        }

        private int extractWorkingState(int event) {
            if (event == GpsStatus.GPS_EVENT_STOPPED) {
                return WORKING_STATE_STOPPED;
            }

            GpsStatus status = LocationFetcher.this.mLocationManager.getGpsStatus(null);
            boolean hasSatellites = status.getSatellites().iterator().hasNext();

            if (!hasSatellites) {
                return WORKING_STATE_STOPPED;
            }

            return WORKING_STATE_WORKING;
        }

        private void raiseCurrentStatus() {
            if (mLastWorkingState == WORKING_STATE_STOPPED) {
                GpsStatusBroadcastReceiver.broadcastGpsStatus(LocationFetcher.this.mAppContext,
                        false);
            } else {
                GpsStatusBroadcastReceiver.broadcastGpsStatus(LocationFetcher.this.mAppContext,
                        true);
            }
        }
    }
}
