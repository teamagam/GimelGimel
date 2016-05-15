package com.teamagam.gimelgimel.app.control.sensors;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

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


    private static void checkForLocationPermission(Context context) {
        int fineLocationPermissionStatus = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (fineLocationPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("No permission granted for location fetching");
        }
    }

    /**
     * opens dialog for permission from the user. needed for API 23
     * @param activity - the activity should implement onRequestPermissionsResult method for response
     */
    public static void askForLocationPermission(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }



    private Context mContext;

    private LocationManager mLocationManager;

    /**
     * Underlying "native" listener
     */
    private LocationListener mLocationListener;

    /**
     * Location updates notifier
     */
    private LocationFetcherListener mLocationFetcherListener;

    private Collection<String> mProviders;
    private boolean mIsRegistered;

    public LocationFetcher(Context context, LocationManager locationManager,
                           LocationFetcherListener locationFetcherListener) {
        mContext = context;
        mLocationManager = locationManager;
        mLocationFetcherListener = locationFetcherListener;
        mLocationListener = new MyLocationListener();

        mProviders = new ArrayList<>();
        mIsRegistered = false;
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

        if(mIsRegistered){
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
    public void registerForUpdates(long minSamplingFrequencyMs,
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

        checkForLocationPermission(mContext);

        if (mProviders.isEmpty()) {
            throw new RuntimeException("No providers added to fetcher to register with");
        }

        for (String provider : mProviders) {
            mLocationManager.requestLocationUpdates(provider, minSamplingFrequencyMs,
                    minDistanceDeltaSamplingMeters, mLocationListener);
        }

        mIsRegistered = true;
    }

    /**
     * Stops fetcher from receiving location updates
     */
    public void unregisterFromUpdates() {
        if (!mIsRegistered) {
            throw new RuntimeException("Fetcher is not registered");
        }

        checkForLocationPermission(mContext);

        mLocationManager.removeUpdates(mLocationListener);
        mIsRegistered = false;
    }


    /**
     * Used for receiving notifications from the {@link LocationFetcher}
     * when location events occurs.
     * New location notifications will be raised only after the
     * {@link LocationFetcher} registered for location changes
     */
    public interface LocationFetcherListener {
        void onProviderDisabled(@ProviderType String locationProvider);

        void onProviderEnabled(@ProviderType String locationProvider);

        void onNewLocationSample(LocationSample locationSample);
    }

    /**
     * Encapsulates incoming updates handling from {@link LocationFetcher}'s underlying {@link LocationManager}
     */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LocationSample locationSample = new LocationSample(location);
            Log.v(LOG_TAG, "New location sample: " + locationSample.toString());
            mLocationFetcherListener.onNewLocationSample(locationSample);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == android.location.LocationProvider.AVAILABLE) {
                mLocationFetcherListener.onProviderEnabled(provider);
            } else {
                mLocationFetcherListener.onProviderDisabled(provider);
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            mLocationFetcherListener.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            mLocationFetcherListener.onProviderDisabled(provider);
        }
    }
}
