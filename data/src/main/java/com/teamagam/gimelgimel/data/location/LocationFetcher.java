package com.teamagam.gimelgimel.data.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
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
import io.reactivex.functions.Action;

public class LocationFetcher {

  private static final Logger sLogger = LoggerFactory.create(LocationFetcher.class.getSimpleName());
  private final Context mAppContext;
  private final LocationManager mLocationManager;
  private final Collection<String> mProviders;
  private final LocationListener mLocationListener;
  private final Set<GpsLocationListener> mListeners;
  private final StoppedGpsStatusDelegator mStoppedGpsStatusDelegator;
  private final UiRunner mUiRunner;
  private Location mOldLocation;
  private boolean mIsRequestingUpdates;
  private boolean mRapidLocationRequests;
  private long mMinSamplingFrequencyMs;
  private long mRapidSamplingFrequencyMs;
  private long mDistanceDeltaSamplingMeters;
  private int mRegisteredProviders;

  public LocationFetcher(Context applicationContext, UiRunner uiRunner, long minSamplingFrequencyMs,
      long rapidSamplingFrequencyMs, long minDistanceDeltaSamplingMeters) {

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
    mRapidSamplingFrequencyMs = rapidSamplingFrequencyMs;
    mDistanceDeltaSamplingMeters = minDistanceDeltaSamplingMeters;

    mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
    mProviders = new ArrayList<>();
    mIsRequestingUpdates = false;
    mRapidLocationRequests = false;

    mStoppedGpsStatusDelegator = new StoppedGpsStatusDelegator();

    mLocationListener = new LocationFetcherListener();
    mListeners = new HashSet<>();

    mRegisteredProviders = 0;

    addProviders();
  }

  public void start() {
    requestLocationUpdates(mMinSamplingFrequencyMs);
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

  public boolean isRequestingUpdates() {
    return mIsRequestingUpdates;
  }

  public LocationListener getLocationListener() {
    return mLocationListener;
  }

  private void addProviders() {
    addProvider(ProviderType.LOCATION_PROVIDER_GPS);
  }

  private void addProvider(
      @ProviderType
          String locationProvider) {
    mProviders.add(locationProvider);
  }

  private void requestLocationUpdates(long frequencyMs) {
    if (mIsRequestingUpdates) {
      throw new RuntimeException("Fetcher already registered!");
    }

    checkForLocationPermission(mAppContext);

    if (mProviders.isEmpty()) {
      throw new RuntimeException("No providers added to fetcher to register with");
    }

    mUiRunner.run(() -> {
      for (String provider : mProviders) {
        requestLocationUpdates(provider, frequencyMs);
      }

      validateProviderRegistered();
      mIsRequestingUpdates = true;

      //Attach NativeGpsStatus listener
      mLocationManager.addGpsStatusListener(mStoppedGpsStatusDelegator);
    });
  }

  private void removeFromUpdates() {
    if (!mIsRequestingUpdates) {
      throw new RuntimeException("Fetcher is not registered");
    }

    checkForLocationPermission(mAppContext);

    mLocationManager.removeUpdates(mLocationListener);
    mIsRequestingUpdates = false;

    mLocationManager.removeGpsStatusListener(mStoppedGpsStatusDelegator);
  }

  @SuppressWarnings("MissingPermission")
  private void requestLocationUpdates(String provider, long frequencyMs) {
    try {
      mLocationManager.requestLocationUpdates(provider, frequencyMs, mDistanceDeltaSamplingMeters,
          mLocationListener);
      mRegisteredProviders++;
    } catch (IllegalArgumentException ex) {
      sLogger.e("Could not add provider" + provider + " (does it exist on the device?)", ex);
    }
  }

  private void validateProviderRegistered() {
    if (mRegisteredProviders == 0) {
      sLogger.w("No location provider registered");
    }
  }

  private void handleNewLocation(Location location) {
    if (isSufficientQuality(location)) {
      notifyNewLocation(location);
      adjustUpdateFrequency(mOldLocation, location);
      mOldLocation = location;
    } else {
      notifyNoConnection();
      adjustUpdateFrequency(mOldLocation, location);
    }
  }

  private void adjustUpdateFrequency(Location oldLocation, Location newLocation) {
    if (isSufficientQuality(newLocation)) {
      if (mRapidLocationRequests) {
        normalLocationRequest();
      }
    } else if (isSignificantlyNewer(oldLocation, newLocation)) {
      if (!mRapidLocationRequests) {
        rapidLocationRequest();
      }
    }
  }

  private boolean isSufficientQuality(Location location) {
    return location.getAccuracy() < Constants.MAXIMUM_GPS_SAMPLE_DEVIATION_METERS;
  }

  private void normalLocationRequest() {
    stopUpdates();

    mRapidLocationRequests = false;
    requestLocationUpdates(mMinSamplingFrequencyMs);
  }

  private void rapidLocationRequest() {
    stopUpdates();

    mRapidLocationRequests = true;
    requestLocationUpdates(mRapidSamplingFrequencyMs);
  }

  private void stopUpdates() {
    if (mIsRequestingUpdates) {
      removeFromUpdates();
    }
  }

  private boolean isSignificantlyNewer(Location oldLocation, Location newLocation) {
    if (oldLocation == null) {
      return true;
    }

    long timeDifference = newLocation.getTime() - oldLocation.getTime();
    return timeDifference >= mMinSamplingFrequencyMs;
  }

  private LocationSample convertToLocationSample(Location location) {
    PointGeometry point =
        new PointGeometry(location.getLatitude(), location.getLongitude(), location.hasAltitude(),
            location.getAltitude());

    return new LocationSample(point, location.getTime(), location.getProvider(),
        location.hasSpeed(), location.getSpeed(), location.hasBearing(), location.getBearing(),
        location.hasAccuracy(), location.getAccuracy());
  }

  private void notifyNewLocation(Location location) {
    sLogger.d("Notifying new location");
    LocationSample locationSample = convertToLocationSample(location);

    for (GpsLocationListener listener : mListeners) {
      listener.onNewLocation(locationSample);
    }
  }

  private void notifyNoConnection() {
    sLogger.d("Notifying bad connection");
    for (GpsLocationListener listener : mListeners) {
      listener.onBadConnection();
    }
  }

  private void checkForLocationPermission(Context context) {
    int fineLocationPermissionStatus =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

    if (fineLocationPermissionStatus != PackageManager.PERMISSION_GRANTED) {
      throw new SecurityException("No permission granted for location fetching");
    }
  }

  @StringDef({
      ProviderType.LOCATION_PROVIDER_GPS, ProviderType.LOCATION_PROVIDER_NETWORK,
      ProviderType.LOCATION_PROVIDER_PASSIVE
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface ProviderType {

    String LOCATION_PROVIDER_GPS = LocationManager.GPS_PROVIDER;

    String LOCATION_PROVIDER_NETWORK = LocationManager.NETWORK_PROVIDER;
    String LOCATION_PROVIDER_PASSIVE = LocationManager.PASSIVE_PROVIDER;
  }

  public interface UiRunner {
    void run(Action action);
  }

  private class StoppedGpsStatusDelegator implements GpsStatus.Listener {

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
      sLogger.d("New GPS sample " + location.toString());
      LocationFetcher.this.handleNewLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      sLogger.v("Provider: " + provider + ", Status: " + getStatusString(status));
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private String getStatusString(int status) {
      switch (status) {
        case LocationProvider.AVAILABLE:
          return "available";
        case LocationProvider.OUT_OF_SERVICE:
          return "out of service";
        case LocationProvider.TEMPORARILY_UNAVAILABLE:
          return "temporarily unavailable";
        default:
          return "unknown status";
      }
    }
  }
}
