package com.teamagam.gimelgimel.app.map.esri;

import android.location.LocationListener;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;

public class LocationDisplayer {

  private final LocationDisplay mLocationDisplay;

  public LocationDisplayer(LocationDisplay locationDisplay,
      LocationListener locationListener) {
    mLocationDisplay = locationDisplay;
    //mLocationDisplay.setLocationListener(locationListener);
  }

  public void displaySelfLocation() {
    mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.OFF);
  }

  public void centerAndShowAzimuth() {
    mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
  }

  public void start() {
    mLocationDisplay.startAsync();
  }
}