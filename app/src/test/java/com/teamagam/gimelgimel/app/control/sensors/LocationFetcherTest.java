package com.teamagam.gimelgimel.app.control.sensors;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher.ProviderType;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LocationFetcherTest {

    private LocationFetcher mLocationFetcher;
    private LocationManager mLocationManagerMock;
    private LocationFetcher.LocationFetcherListener mListenerMock;
    private Context mShadowContext;


    @Before
    public void setUp() throws Exception {
        mShadowContext = spy(ShadowApplication.getInstance().getApplicationContext());
        when(mShadowContext.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(),
                anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);
        mLocationManagerMock = mock(LocationManager.class);
        mListenerMock = mock(LocationFetcher.LocationFetcherListener.class);

        mLocationFetcher = new LocationFetcher(mShadowContext, mLocationManagerMock, mListenerMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProvider_nullArgument_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.addProvider(null);
    }

    @Test(expected = RuntimeException.class)
    public void testAddProvider_alreadyRegistered_shouldThrow() throws Exception {
        //Arrange
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_NETWORK);
        mLocationFetcher.registerForUpdates(0, 0);

        //Act
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterForUpdates_negativeFrequency_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.registerForUpdates(-1, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterForUpdates_negativeDistanceDelta_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.registerForUpdates(0, -1);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterForUpdates_noProviderAdded_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.registerForUpdates(0, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterForUpdates_alreadyRegistered_shouldThrow() throws Exception {
        //Arrange
        mLocationFetcher.registerForUpdates(0, 0);

        //Act
        mLocationFetcher.registerForUpdates(0, 0);
    }


    @SuppressWarnings("MissingPermission")
    @Test
    public void testRegisterForUpdates_validState_ShouldNotifyOnNewLocation() throws Exception {
        //Arrange
        Location locationMock = mock(Location.class);

        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
                LocationListener.class);

        //Act
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
        mLocationFetcher.registerForUpdates(0, 0);
        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
                locationListenerArgumentCaptor.capture());

        locationListenerArgumentCaptor.getValue().onLocationChanged(locationMock);

        //Assert
        verify(mListenerMock, times(1)).onNewLocationSample(any(LocationSample.class));
    }

    @SuppressWarnings("MissingPermission")
    @Test
    public void testRegisterForUpdates_validState_ShouldNotifyOnProviderEnabled() throws Exception {
        //Arrange
        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
                LocationListener.class);

        //Act
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
        mLocationFetcher.registerForUpdates(0, 0);
        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
                locationListenerArgumentCaptor.capture());

        locationListenerArgumentCaptor.getValue().onProviderEnabled(
                ProviderType.LOCATION_PROVIDER_GPS);

        //Assert
        verify(mListenerMock, times(1)).onProviderEnabled(ProviderType.LOCATION_PROVIDER_GPS);
    }

    @SuppressWarnings("MissingPermission")
    @Test
    public void testRegisterForUpdates_validState_ShouldNotifyOnProviderDisabled() throws Exception {
        //Arrange
        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
                LocationListener.class);

        //Act
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
        mLocationFetcher.registerForUpdates(0, 0);
        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
                locationListenerArgumentCaptor.capture());

        locationListenerArgumentCaptor.getValue().onProviderDisabled(
                ProviderType.LOCATION_PROVIDER_GPS);

        //Assert
        verify(mListenerMock, times(1)).onProviderDisabled(ProviderType.LOCATION_PROVIDER_GPS);
    }

    @Test(expected = RuntimeException.class)
    public void testUnregisterFromUpdates_notRegistered_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.unregisterFromUpdates();
    }

    @SuppressWarnings("MissingPermission")
    @Test
    public void testUnregisterFromUpdates_validState_shouldRemoveLocationManagerUpdates() throws Exception {
        //Arrange
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
        mLocationFetcher.registerForUpdates(0, 0);

        //Act
        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
                LocationListener.class);

        mLocationFetcher.unregisterFromUpdates();

        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
                locationListenerArgumentCaptor.capture());

        //Assert
        verify(mLocationManagerMock, times(1)).removeUpdates(
                locationListenerArgumentCaptor.getValue());
    }
}