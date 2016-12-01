package com.teamagam.gimelgimel.data.location;

import android.content.Context;
import android.location.LocationManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, manifest = Config.NONE)
public class LocationFetcherTest {

    private static final int MIN_SAMPLING_FREQUENCY_MS = 1000;
    private static final int MIN_DISTANCE_DELTA_SAMPLING_METERS = 3;

    private LocationFetcher mLocationFetcher;
    private LocationManager mLocationManagerMock;
    private Context mShadowContext;

    @Test
    public void emptyTest(){

    }

//    @Before
//    public void setUp() throws Exception {
//        mShadowContext = spy(ShadowApplication.getInstance().getApplicationContext());
//        mLocationManagerMock = mock(LocationManager.class);
//
//        when(mShadowContext.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(),
//                anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);
//        when(mShadowContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(
//                mLocationManagerMock);
//        when(mShadowContext.getApplicationContext()).thenReturn(mShadowContext);
//
//        LocationFetcher.UiRunner uiRunner = new LocationFetcher.UiRunner() {
//            @Override
//            public void run(Action0 action) {
//                action.call();
//            }
//        };
//
//        if(!LoggerFactory.isInitialized()){
//            LoggerFactory.initialize(tag -> null);
//        }
//
//
//        mLocationFetcher = new LocationFetcher(mShadowContext, uiRunner, MIN_SAMPLING_FREQUENCY_MS,
//                MIN_DISTANCE_DELTA_SAMPLING_METERS);
//    }
//
//
//    @Test(expected = RuntimeException.class)
//    public void testRequestLocationUpdates_negativeFrequency_shouldThrow() throws Exception {
//        //Act
//        mLocationFetcher.requestLocationUpdates();
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void testRequestLocationUpdates_negativeDistanceDelta_shouldThrow() throws Exception {
//        //Act
//        mLocationFetcher.requestLocationUpdates();
//    }

//    @Test(expected = RuntimeException.class)
//    public void testRequestLocationUpdates_noProviderAdded_shouldThrow() throws Exception {
//        //Act
//        mLocationFetcher.requestLocationUpdates();
//    }

//    @Test(expected = RuntimeException.class)
//    public void testRequestLocationUpdates_alreadyRegistered_shouldThrow() throws Exception {
//        //Arrange
//        mLocationFetcher.requestLocationUpdates();
//
//        //Act
//        mLocationFetcher.requestLocationUpdates();
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void testUnregisterFromUpdates_notRegistered_shouldThrow() throws Exception {
//        //Act
//        mLocationFetcher.removeFromUpdates();
//    }
//
//    @SuppressWarnings("MissingPermission")
//    @Test
//    public void testUnregisterFromUpdates_validState_shouldRemoveLocationManagerUpdates() throws Exception {
//        //Arrange
//        when(mLocationManagerMock.isProviderEnabled(LocationFetcher.ProviderType.LOCATION_PROVIDER_GPS)).thenReturn(true);
//        mLocationFetcher.requestLocationUpdates();
//
//        //Act
//        ArgumentCaptor<LocationListener> locationListenerArgumentCaptor = ArgumentCaptor.forClass(
//                LocationListener.class);
//
//        verify(mLocationManagerMock).requestLocationUpdates(anyString(), anyLong(), anyFloat(),
//                locationListenerArgumentCaptor.capture());
//
//        mLocationFetcher.removeFromUpdates();
//
//        //Assert
//
//        verify(mLocationManagerMock, times(1)).removeUpdates(
//                locationListenerArgumentCaptor.getValue());
//    }
}