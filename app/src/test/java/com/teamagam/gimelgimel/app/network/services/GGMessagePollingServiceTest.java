package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.Intent;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.util.ServiceController;

import java.util.Collection;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Gil.Raytan on 10-May-16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class GGMessagePollingServiceTest {

    private GGMessageLongPollingServiceMock mMockService;
    private Context mShadowContext;
    private Intent mIntent;
    private ServiceController<GGMessageLongPollingServiceMock> mServiceController;
    private GGMessageLongPollingServiceMock mService;

    /**
     * Helper class to expose onHandleIntent method for testing
     */
    public class GGMessageLongPollingServiceMock extends GGMessageLongPollingService {

        @Override
        protected void onHandleIntent(Intent intent) {
            super.onHandleIntent(intent);
        }
    }

    @Before
    public void setUp() throws Exception {

        mShadowContext = spy(ShadowApplication.getInstance().getApplicationContext());
        mIntent = new Intent(mShadowContext, GGMessageLongPollingServiceMock.class);

        mService = spy(new GGMessageLongPollingServiceMock());
        mService.onCreate();
    }

    @After
    public void tearDown() throws Exception {
        mService.onDestroy();
    }

    @Test
    public void testStartMessagePollingPeriodically_sendIntent() {
        //arrange
        //act
        mService.onStartCommand(mIntent, 0, 1);
        //Assert
        verify(mService, times(1)).onHandleIntent(mIntent);
        // verify(mMockService, after(15000).atLeastOnce()).handleActionMessagePolling();
    }
}
