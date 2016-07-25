package com.teamagam.gimelgimel.app.control.sensors;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher.ProviderType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.lang.reflect.Field;

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
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class LocationFetcherTest {

    private LocationFetcher mLocationFetcher;
    private LocationManager mLocationManagerMock;
    private Context mShadowContext;
    private BroadcastReceiver mBroadcastReceiver;

    @Before
    public void setUp() throws Exception {
        mShadowContext = spy(ShadowApplication.getInstance().getApplicationContext());
        mLocationManagerMock = mock(LocationManager.class);

        when(mShadowContext.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(),
                anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);
        when(mShadowContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(
                mLocationManagerMock);
        when(mShadowContext.getApplicationContext()).thenReturn(mShadowContext);

        mLocationFetcher = LocationFetcher.getInstance(mShadowContext);

        mBroadcastReceiver = mock(BroadcastReceiver.class);
    }

    @After
    public void tearDown() throws Exception {
        Field lbmSingletonField = LocalBroadcastManager.class.getDeclaredField("mInstance");
        lbmSingletonField.setAccessible(true);
        lbmSingletonField.set(null, null);

        Field instance = LocationFetcher.class.getDeclaredField("sInstance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProvider_nullArgument_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.addProvider(null);
    }

    @Test(expected = RuntimeException.class)
    public void testAddProvider_alreadyReceivingUpdated_shouldThrow() throws Exception {
        //Arrange
        when(mLocationManagerMock.isProviderEnabled(ProviderType.LOCATION_PROVIDER_GPS)).thenReturn(true);
        when(mLocationManagerMock.isProviderEnabled(ProviderType.LOCATION_PROVIDER_NETWORK)).thenReturn(true);
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_NETWORK);
        mLocationFetcher.requestLocationUpdates(0, 0);

        //Act
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
    }

    @Test(expected = RuntimeException.class)
    public void testAddProvider_providerNotEnabled_shouldThrow() throws Exception {
        //Arrange
        when(mLocationManagerMock.isProviderEnabled(ProviderType.LOCATION_PROVIDER_GPS)).thenReturn(false);

        //Act
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
    }

    @Test(expected = RuntimeException.class)
    public void testRequestLocationUpdates_negativeFrequency_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.requestLocationUpdates(-1, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRequestLocationUpdates_negativeDistanceDelta_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.requestLocationUpdates(0, -1);
    }

    @Test(expected = RuntimeException.class)
    public void testRequestLocationUpdates_noProviderAdded_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.requestLocationUpdates(0, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRequestLocationUpdates_alreadyRegistered_shouldThrow() throws Exception {
        //Arrange
        mLocationFetcher.requestLocationUpdates(0, 0);

        //Act
        mLocationFetcher.requestLocationUpdates(0, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testUnregisterFromUpdates_notRegistered_shouldThrow() throws Exception {
        //Act
        mLocationFetcher.removeFromUpdates();
    }

    @SuppressWarnings("MissingPermission")
    @Test
    public void testUnregisterFromUpdates_validState_shouldRemoveLocationManagerUpdates() throws Exception {
        //Arrange
        when(mLocationManagerMock.isProviderEnabled(ProviderType.LOCATION_PROVIDER_GPS)).thenReturn(true);
        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
        mLocationFetcher.requestLocationUpdates(0, 0);

        //Act
        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
                LocationListener.class);

        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
                locationListenerArgumentCaptor.capture());

        mLocationFetcher.removeFromUpdates();

        //Assert

        verify(mLocationManagerMock, times(1)).removeUpdates(
                locationListenerArgumentCaptor.getValue());
    }

    @SuppressWarnings("MissingPermission")
    @Test
    public void testRegisterReceiver_validState_shouldRegisterBroadcastReceiver() throws Exception {
        //Arrange
        Field lbmSingletonField = LocalBroadcastManager.class.getDeclaredField("mInstance");
        lbmSingletonField.setAccessible(true);
        LocalBroadcastManager localBroadcastManagerMock = mock(LocalBroadcastManager.class);
        lbmSingletonField.set(null, localBroadcastManagerMock);

        //Act
        mLocationFetcher.registerReceiver(mBroadcastReceiver);

        //Assert
        verify(localBroadcastManagerMock, times(1)).
                registerReceiver(eq(mBroadcastReceiver), any(IntentFilter.class));
    }
}