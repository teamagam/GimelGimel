package com.teamagam.gimelgimel.data.location;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.location.repository.GpsLocationListener;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    private IntentFilter mIntentFilter;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private Collection<String> mProviders;
    private boolean mIsRequestingUpdates;
    private LocationSampleEntity mLastLocation;
    private LocationListener mLocationListener;
    private StoppedGpsStatusDelegator mStoppedGpsStatusDelegator;
    private GpsStatusChangedBroadcaster mGpsStatusChangedBroadcaster;
    private Set<GpsLocationListener> mListeners;
    private long mMinSamplingFrequencyMs;
    private long mDistanceDeltaSamplingMeters;

    /**
     *
     * @param applicationContext
     * @param minSamplingFrequencyMs         - minimum time between location samples,  in milliseconds
     * @param minDistanceDeltaSamplingMeters - minimum distance between location samples, in meters
     */
    public LocationFetcher(Context applicationContext,
                           long minSamplingFrequencyMs, long minDistanceDeltaSamplingMeters) {

        if (minSamplingFrequencyMs < 0) {
            throw new IllegalArgumentException("minSamplingFrequencyMs cannot be negative");
        }

        if (minDistanceDeltaSamplingMeters < 0) {
            throw new IllegalArgumentException(
                    "minDistanceDeltaSamplingMeters cannot be a negative number");
        }


        mAppContext = applicationContext;
        mMinSamplingFrequencyMs = minSamplingFrequencyMs;
        mDistanceDeltaSamplingMeters = minDistanceDeltaSamplingMeters;
        
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mProviders = new ArrayList<>();
        mIsRequestingUpdates = false;

        mIntentFilter = new IntentFilter(LocationFetcher.LOCATION_FILTER_BROADCAST);

        mGpsStatusChangedBroadcaster = new GpsStatusChangedBroadcaster();
        mStoppedGpsStatusDelegator = new StoppedGpsStatusDelegator(
                mGpsStatusChangedBroadcaster);

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

    public LocationSampleEntity getLastLocationSample() {
        LocationSampleEntity location = getLastKnownLocation();

        if (location == null) {
            return null;
        }

        PointGeometry point = new PointGeometry(
                location.getLocation().getLatitude(), location.getLocation().getLongitude());

        return new LocationSampleEntity(point, location.getTime());
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

        for (String provider : mProviders) {
            mLocationManager.requestLocationUpdates(provider, mMinSamplingFrequencyMs,
                    mMinSamplingFrequencyMs, mLocationListener);
        }

        mIsRequestingUpdates = true;

        //Attach NativeGpsStatus listener
        mLocationManager.addGpsStatusListener(mStoppedGpsStatusDelegator);
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
     * Adds provider to be used when registering the fetcher
     */
    private void addProviders() {

        if (mIsRequestingUpdates) {
            throw new RuntimeException("Cannot add providers to an already registered fetcher!");
        }

        tryAddProvider(ProviderType.LOCATION_PROVIDER_GPS);
        tryAddProvider(ProviderType.LOCATION_PROVIDER_NETWORK);
        tryAddProvider(ProviderType.LOCATION_PROVIDER_PASSIVE);
    }

    private void tryAddProvider(@ProviderType String locationProvider) {
        if (!isProviderExistsAndEnabled(locationProvider)) {
            throw new RuntimeException(
                    "Provider " + locationProvider + " is not supported/enabled on this device");
        }

        mProviders.add(locationProvider);
        mProviders.add(locationProvider);
        mProviders.add(locationProvider);
    }

    private void handleNewLocation(Location location) {
        if (isSufficientQuality(location)) {
            LocationSampleEntity locationSample = convertToLocationSample(location);

            //broadcastNewLocation(locationSample);

            mLastLocation = locationSample;

            mGpsStatusChangedBroadcaster.notifyWorking();
        } else {
            mGpsStatusChangedBroadcaster.notifyStopped();
        }
    }

    private LocationSampleEntity convertToLocationSample(Location location) {
        PointGeometry point = new PointGeometry(location.getLatitude(), location.getLongitude());
        if (location.hasAltitude()) {
            point.setAltitude(location.getAltitude());
        }

        LocationSampleEntity locationSample = new LocationSampleEntity(point, location.getTime());

        locationSample.setProvider(location.getProvider());

        if(location.hasSpeed()) {
            locationSample.setSpeed(location.getSpeed());
        }

        if(location.hasBearing()) {
            locationSample.setBearing(location.getBearing());
        }

        if(location.hasAccuracy()) {
            locationSample.setAccuracy(location.getAccuracy());
        }

        return locationSample;
    }


    private boolean isSufficientQuality(Location location) {
        return location.getAccuracy() < Constants.MAXIMUM_GPS_SAMPLE_DEVIATION_METERS;
    }

    private boolean isProviderExistsAndEnabled(@ProviderType String locationProvider) {
        return mLocationManager.isProviderEnabled(locationProvider);
    }

    /*private void broadcastNewLocation(LocationSample locationSample) {
        Intent broadcastIntent = new Intent(LocationFetcher.LOCATION_FILTER_BROADCAST);
        broadcastIntent.putExtra(LocationFetcher.KEY_NEW_LOCATION_SAMPLE, locationSample);
        LocalBroadcastManager.getInstance(mAppContext).sendBroadcast(broadcastIntent);
    }*/

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
    public LocationSampleEntity getLastKnownLocation() {
        return mLastLocation;
    }

    /**
     * GpsStatus.Listener implementation used to delegate it's stopped events only.
     * Those events are delegated to {@class GpsStatusChangedBroadcaster} which in-turn broadcasts
     * if needed.
     */
    public class StoppedGpsStatusDelegator implements GpsStatus.Listener {

        private GpsStatusChangedBroadcaster mGpsStatusChangedBroadcaster;

        public StoppedGpsStatusDelegator(
                GpsStatusChangedBroadcaster gpsStatusChangedBroadcaster) {
            mGpsStatusChangedBroadcaster = gpsStatusChangedBroadcaster;
        }

        @Override
        public synchronized void onGpsStatusChanged(int event) {
            if (isStoppedEvent(event)) {
                mGpsStatusChangedBroadcaster.notifyStopped();
            }
        }

        private boolean isStoppedEvent(int event) {
            return event == GpsStatus.GPS_EVENT_STOPPED;
        }
    }

    /**
     * Broadcasts GPS status only when status is changed.
     */
    private class GpsStatusChangedBroadcaster {

        public final int GPS_STATUS_UNINITIALIZED = 0;
        public final int GPS_STATUS_WORKING = 1;
        public final int GPS_STATUS_STOPPED = 2;

        private int mCurrentStatus;

        public GpsStatusChangedBroadcaster() {
            mCurrentStatus = GPS_STATUS_UNINITIALIZED;
        }

        public void notifyWorking() {
            tryUpdateStatus(GPS_STATUS_WORKING);
        }

        public void notifyStopped() {
            tryUpdateStatus(GPS_STATUS_STOPPED);
        }

        private void tryUpdateStatus(int status) {
            if (mCurrentStatus != status) {
                mCurrentStatus = status;
                broadcastNewGpsStatus();
            }
        }

        private void broadcastNewGpsStatus() {
            /*switch (mCurrentStatus) {
                case GPS_STATUS_STOPPED:
                    GpsStatusBroadcastReceiver.broadcastGpsStatus(LocationFetcher.this.mAppContext,
                            false);
                    break;
                case GPS_STATUS_WORKING:
                    GpsStatusBroadcastReceiver.broadcastGpsStatus(LocationFetcher.this.mAppContext,
                            true);
                    break;
                default:
                    throw new RuntimeException("Invalid GPS-status");
            }*/
        }
    }

    private class LocationFetcherListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LocationFetcher.this.handleNewLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    }
}
