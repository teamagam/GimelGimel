package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.Intent;

import com.teamagam.gimelgimel.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import junit.framework.Assert;

import static org.mockito.Mockito.spy;

/**
 * Created by Gil.Raytan on 10-May-16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class GGMessagePollingServiceTest {

    private GGMessageLongPollingServiceMock mMockService;
    private Context mShadowContext;
    private Intent mIntent;

    static {
        ShadowLog.stream = System.out;
    }

    public class GGMessageLongPollingServiceMock extends GGMessageLongPollingService {

        @Override
        public void onHandleIntent(Intent intent) {
            super.onHandleIntent(intent);
        }
    }

    @Before
    public void setUp() throws Exception {

        mIntent = new Intent(RuntimeEnvironment.application, GGMessageLongPollingServiceMock.class);
        mShadowContext = spy(ShadowApplication.getInstance().getApplicationContext());
        mMockService = new GGMessageLongPollingServiceMock();
    }

    @Test
    public void testStartMessagePollingPeriodically_sendIntent() {
        Intent serviceIntent = new Intent(mShadowContext, GGMessageLongPollingServiceMock.class);

        GGMessageLongPollingServiceMock service = new GGMessageLongPollingServiceMock();

        service.startMessageLongPollingInfinitly(mShadowContext);
        service.onHandleIntent(serviceIntent);

        Assert.assertEquals(serviceIntent.getAction(), "com.teamagam.gimelgimel.app.network.services.action.MESSAGE_POLLING");
    }
}
