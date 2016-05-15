package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.Intent;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Collection;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.spy;
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

    public class GGMessageLongPollingServiceMock extends GGMessageLongPollingService{

        @Override
        protected void onHandleIntent(Intent intent){
            super.onHandleIntent( intent );
        }

        private void processNewMessages(Collection<Message> messages)
        {
            int size = messages.size();
            System.out.println(size);
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

        //arrange
        mMockService.startMessageLongPollingInfinitly(mShadowContext);
        mMockService.onHandleIntent(mIntent);

        //act
       // verify(mMockService, after(15000).atLeastOnce()).handleActionMessagePolling();
    }
}
