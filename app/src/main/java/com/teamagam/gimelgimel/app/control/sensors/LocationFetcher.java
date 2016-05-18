package com.teamagam.gimelgimel.app.control.sensors;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.teamagam.gimelgimel.app.model.entities.LocationSample;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Handles location fetching against the system's sensors
 */
public class LocationFetcher {

    private static final String LOG_TAG = LocationFetcher.class.getSimpleName();

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
    public static final String LOCATION_FILTER_BROADCAST = "com.teamagam.gimelgimel.app.LocationFetcher.LOCATION_READY";
    private static final Object mLock = new Object();
    private static LocationFetcher sInstance;

    private PendingIntent mLocationIntent;
    private IntentFilter mIntentFilter;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private Collection<String> mProviders;
    private boolean mIsRegistered;

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

    @NonNull
    public static LocationSample getLocationSample(Intent intent) {
        Location loc = (Location) intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
        return new LocationSample(loc);
    }

    /**
     * singelton pattern
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
        mIsRegistered = false;

        Intent intent = new Intent(LOCATION_FILTER_BROADCAST);
        mLocationIntent = PendingIntent.getBroadcast(mAppContext,
                0, intent, 0);

        mIntentFilter = new IntentFilter(LOCATION_FILTER_BROADCAST);
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

        if (mIsRegistered) {
            throw new RuntimeException("Cannot add providers to an already registered fetcher!");
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

        if (mIsRegistered) {
            throw new RuntimeException("Fetcher already registered!");
        }

        checkForLocationPermission(mAppContext);

        if (mProviders.isEmpty()) {
            throw new RuntimeException("No providers added to fetcher to register with");
        }

        for (String provider : mProviders) {
            mLocationManager.requestLocationUpdates(provider, minSamplingFrequencyMs,
                    minDistanceDeltaSamplingMeters, mLocationIntent);
        }

        mIsRegistered = true;
    }

    /**
     * Stops fetcher from receiving location updates
     */
    public void removeFromUpdates() {
        if (!mIsRegistered) {
            throw new RuntimeException("Fetcher is not registered");
        }

        checkForLocationPermission(mAppContext);

        mLocationManager.removeUpdates(mLocationIntent);
        mIsRegistered = false;
    }

    /**
     * used for registering receiver to get location update
     *
     * @param receiver - for updates result
     */
    public void registerReceiver(BroadcastReceiver receiver) {
        mAppContext.registerReceiver(receiver, mIntentFilter);
    }

    /**
     * used for unregistering receivers from Location updates
     *
     * @param receiver
     */
    public void unregisterReceiver(BroadcastReceiver receiver) {
        mAppContext.unregisterReceiver(receiver);
    }


}
