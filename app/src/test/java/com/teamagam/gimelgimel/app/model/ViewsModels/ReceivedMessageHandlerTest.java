package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;

import com.teamagam.gimelgimel.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ReceivedMessageHandlerTest {

    private Context mShadowContext;
    private ReceivedMessageHandler mMessageHandler;
    private ReceivedMessageHandler.ShowMessagesSubscriber mShowListener;
    private ReceivedMessageHandler.UpdateLocationMessagesSubscriber mLocationListener;



    @Before
    public void setUp() throws Exception {
        mShadowContext = spy(ShadowApplication.getInstance().getApplicationContext());

        mMessageHandler = ReceivedMessageHandler.getInstance();
        mShowListener = mock(ReceivedMessageHandler.ShowMessagesSubscriber.class);
        mLocationListener = mock(ReceivedMessageHandler.UpdateLocationMessagesSubscriber.class);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnNewMessage_nullArgument_ShouldThrow() throws Exception {
        //Act
        (null);
    }

    @Test()
    public void testOnUpdateLocationMessage_LocationMessage_shouldCall() throws Exception {
        //Act
        MessageUserLocation msg = mock(MessageUserLocation.class);
        mMessageHandler.subscribeLocation(mLocationListener);
        mMessageHandler.onNewMessage(msg);
        verify(mLocationListener, times(1)).onUpdateLocationMessage(any(MessageUserLocation.class));
    }

    @Test(expected = NullPointerException.class)
    public void testOnUpdateLocationMessage_LatLongMessage_shouldThrow() throws Exception {
        //Act
        MessageLatLong msg = mock(MessageLatLong.class);
        mMessageHandler.subscribeLocation(mLocationListener);
        mMessageHandler.onNewMessage(msg);
        verify(mLocationListener, times(1)).onUpdateLocationMessage(any(MessageUserLocation.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterForUpdates_negativeFrequency_shouldThrow() throws Exception {
        //Act
//        mLocationFetcher.registerForUpdates(-1, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterForUpdates_negativeDistanceDelta_shouldThrow() throws Exception {
        //Act
//        mLocationFetcher.registerForUpdates(0, -1);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterForUpdates_noProviderAdded_shouldThrow() throws Exception {
        //Act
//        mLocationFetcher.registerForUpdates(0, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterForUpdates_alreadyRegistered_shouldThrow() throws Exception {
        //Arrange
//        mLocationFetcher.registerForUpdates(0, 0);

        //Act
//        mLocationFetcher.registerForUpdates(0, 0);
    }


    @Test
    public void testRegisterForUpdates_validState_ShouldNotifyOnNewLocation() throws Exception {
        //Arrange
        Location locationMock = mock(Location.class);

        when(mShadowContext.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(),
                anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);

        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
                LocationListener.class);

        //Act
//        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
//        mLocationFetcher.registerForUpdates(0, 0);
//        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
//                locationListenerArgumentCaptor.capture());
//
//        locationListenerArgumentCaptor.getValue().onLocationChanged(locationMock);
//
//        //Assert
//        verify(mListenerMock, times(1)).onNewLocationSample(any(LocationSample.class));
    }

    @Test
    public void testRegisterForUpdates_validState_ShouldNotifyOnProviderEnabled() throws Exception {
        //Arrange
        when(mShadowContext.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(),
                anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);

        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
                LocationListener.class);

        //Act
//        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
//        mLocationFetcher.registerForUpdates(0, 0);
//        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
//                locationListenerArgumentCaptor.capture());
//
//        locationListenerArgumentCaptor.getValue().onProviderEnabled(
//                ProviderType.LOCATION_PROVIDER_GPS);
//
//        //Assert
//        verify(mListenerMock, times(1)).onProviderEnabled(ProviderType.LOCATION_PROVIDER_GPS);
    }

    @Test
    public void testRegisterForUpdates_validState_ShouldNotifyOnProviderDisabled() throws Exception {
        //Arrange
        when(mShadowContext.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(),
                anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);

        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
                LocationListener.class);

        //Act
//        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
//        mLocationFetcher.registerForUpdates(0, 0);
//        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
//                locationListenerArgumentCaptor.capture());
//
//        locationListenerArgumentCaptor.getValue().onProviderDisabled(
//                ProviderType.LOCATION_PROVIDER_GPS);
//
//        //Assert
//        verify(mListenerMock, times(1)).onProviderDisabled(ProviderType.LOCATION_PROVIDER_GPS);
    }

    @Test(expected = RuntimeException.class)
    public void testUnregisterFromUpdates_notRegistered_shouldThrow() throws Exception {
        //Act
//        mLocationFetcher.unregisterFromUpdates();
    }

    @Test
    public void testUnregisterFromUpdates_validState_shouldRemoveLocationManagerUpdates() throws Exception {
        //Arrange
        when(mShadowContext.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(),
                anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);
//        mLocationFetcher.addProvider(ProviderType.LOCATION_PROVIDER_GPS);
//        mLocationFetcher.registerForUpdates(0, 0);
//
//        //Act
//        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
//                LocationListener.class);
//
//        mLocationFetcher.unregisterFromUpdates();
//
//        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
//                locationListenerArgumentCaptor.capture());
//
//        //Assert
//        verify(mLocationManagerMock, times(1)).removeUpdates(
//                locationListenerArgumentCaptor.getValue());
    }
}