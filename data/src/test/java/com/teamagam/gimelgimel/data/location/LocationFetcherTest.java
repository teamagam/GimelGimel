package com.teamagam.gimelgimel.data.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.teamagam.gimelgimel.data.location.repository.GpsLocationListener;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, manifest = Config.NONE)
public class LocationFetcherTest extends BaseTest {

  private static final int MIN_SAMPLING_FREQUENCY_MS = 1000;
  private static final int RAPID_SAMPLING_FREQUENCY_MS = 500;
  private static final int MIN_DISTANCE_DELTA_SAMPLING_METERS = 3;

  private LocationFetcher mLocationFetcher;
  private GpsLocationListener mGpsLocationListener;
  private LocationListener mLocationListener;

  @Before
  public void setUp() throws Exception {
    Context shadowContext = spy(ShadowApplication.getInstance().getApplicationContext());
    LocationManager locationManagerMock = mock(LocationManager.class);

    when(shadowContext.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(),
        anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);
    when(shadowContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManagerMock);
    when(shadowContext.getApplicationContext()).thenReturn(shadowContext);

    LocationFetcher.UiRunner uiRunner = action -> {
      try {
        action.run();
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    mGpsLocationListener = mock(GpsLocationListener.class);

    mLocationFetcher = new LocationFetcher(shadowContext, uiRunner, MIN_SAMPLING_FREQUENCY_MS,
        RAPID_SAMPLING_FREQUENCY_MS, MIN_DISTANCE_DELTA_SAMPLING_METERS);

    Field field = LocationFetcher.class.getDeclaredField("mLocationListener");
    field.setAccessible(true);
    mLocationListener = (LocationListener) field.get(mLocationFetcher);
  }

  @Test(expected = RuntimeException.class)
  public void testRequestLocationUpdates_alreadyRegistered_shouldThrow() throws Exception {
    //Arrange
    mLocationFetcher.start();

    //Act
    mLocationFetcher.start();
  }

  @Test(expected = RuntimeException.class)
  public void testUnregisterFromUpdates_notRegistered_shouldThrow() throws Exception {
    //Act
    mLocationFetcher.stop();
  }

  @Test
  public void testRequestLocationUpdates_locationChanged_shouldNotify() throws Exception {
    //Arrange
    mLocationFetcher.start();
    mLocationFetcher.addListener(mGpsLocationListener);

    Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
    mockLocation.setLatitude(32.2);
    mockLocation.setLongitude(33.0);
    mockLocation.setAltitude(0);
    mockLocation.setTime(System.currentTimeMillis());

    //Act
    mLocationListener.onLocationChanged(mockLocation);
    //        mLocationManagerMock.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation);

    //Assert
    verify(mGpsLocationListener, times(1)).onNewLocation(any(LocationSample.class));
  }
}