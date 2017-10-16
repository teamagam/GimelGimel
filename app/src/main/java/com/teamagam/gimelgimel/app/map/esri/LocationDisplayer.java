package com.teamagam.gimelgimel.app.map.esri;

import android.location.LocationListener;

public class LocationDisplayer {

  private final LocationDisplayManager mLocationDisplayManager;

  public LocationDisplayer(LocationDisplayManager locationDisplayManager,
      LocationListener locationListener) {
    mLocationDisplayManager = locationDisplayManager;
    mLocationDisplayManager.setLocationListener(locationListener);
  }

  public void displaySelfLocation() {
    mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);
  }

  public void centerAndShowAzimuth() {
    mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.COMPASS);
  }

  public void start() {
    mLocationDisplayManager.start();
  }
}