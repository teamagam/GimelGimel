package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class MessageBroadcastReceiverTest {

    private Context mShadowContext;
    private MessageBroadcastReceiver mReceiver;
    private MessageBroadcastReceiver.NewMessageHandler mMessageHandler;


    @Before
    public void setUp() throws Exception {
        mShadowContext = spy(ShadowApplication.getInstance().getApplicationContext());
        mMessageHandler = mock(MessageBroadcastReceiver.NewMessageHandler.class);
    }

    @Test()
    public void testConstructor_AllMessages_shouldCreateAll() throws Exception {
        //Act
        for (String type : MessageJsonAdapter.sClassMessageMap.keySet()) {
            MessageBroadcastReceiver mbr = new MessageBroadcastReceiver(mMessageHandler, type);
        }
    }

    @Test()
    public void testGetIntentFilter_getter_shouldGetFilter() throws Exception {
        //Act
        mReceiver = new MessageBroadcastReceiver(mMessageHandler, MessageApp.TEXT);
        //Assert
        assertEquals(mReceiver.getIntentFilter().getAction(0), MessageTextApp.class.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_WrongMessageName_shouldThrow() throws Exception {
        //Act
//        MessageBroadcastReceiver mbr = new MessageBroadcastReceiver(mMessageHandler, "EmptyWrongNotAMessage");
//        mbr.getIntentFilter();
    }


    @Test()
    public void testOnReceive_TextMessage_shouldCallHandler() throws Exception {
        //Arrange
//        MessageApp msgT = new MessageTextApp("sender1", "text123");
        Intent i = mock(Intent.class);
//        when(i.getStringExtra(any(String.class))).thenReturn(GsonUtil.toJson(msgT));
        mReceiver = new MessageBroadcastReceiver(mMessageHandler, MessageApp.TEXT);

        //Act
        mReceiver.onReceive(mShadowContext, i);

        //Assert
        verify(mMessageHandler, times(1)).onNewMessage(any(MessageTextApp.class));
    }

    @Test()
    public void testOnReceive_BroadcastMessage_shouldCallOnReceive() throws Exception {
        //Arrange
//        MessageApp msgT = new MessageTextApp("sender1", "text123");
        Intent i = mock(Intent.class);
//        when(i.getStringExtra(any(String.class))).thenReturn(GsonUtil.toJson(msgT));
        mReceiver = new MessageBroadcastReceiver(mMessageHandler, MessageApp.TEXT);

        //Act
        LocalBroadcastManager.getInstance(mShadowContext).registerReceiver(
                mReceiver, mReceiver.getIntentFilter());
//        Intent intent = new Intent(msgT.getClass().getName());
//        intent.putExtra(MessageBroadcastReceiver.MESSAGE, GsonUtil.toJson(msgT));
//        LocalBroadcastManager.getInstance(mShadowContext).sendBroadcast(intent);

        //Assert
        verify(mMessageHandler, times(1)).onNewMessage(any(MessageTextApp.class));
    }

    @Test()
    public void testOnReceive_BroadcastMessageSeveralCalls_shouldCallOnReceiveThreeTimes() throws Exception {
        //Arrange
//        MessageApp msgT = new MessageTextApp("sender1", "text123");
        Intent i = mock(Intent.class);
//        when(i.getStringExtra(any(String.class))).thenReturn(GsonUtil.toJson(msgT));
        mReceiver = new MessageBroadcastReceiver(mMessageHandler, MessageApp.TEXT);

        //Act
        LocalBroadcastManager.getInstance(mShadowContext).registerReceiver(
                mReceiver, mReceiver.getIntentFilter());
//        Intent intent = new Intent(msgT.getClass().getName());
//        intent.putExtra(MessageBroadcastReceiver.MESSAGE, GsonUtil.toJson(msgT));
//        LocalBroadcastManager.getInstance(mShadowContext).sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mShadowContext).sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mShadowContext).sendBroadcast(intent);

        //Assert
        verify(mMessageHandler, times(3)).onNewMessage(any(MessageTextApp.class));
    }

    @Test()
    public void testOnReceive_BroadcastMessageTypes_shouldCallOnReceiveTwoTimes() throws Exception {
        //Arrange
//        MessageApp msgT = new MessageTextApp("sender1", "text123");
//        GeoContentApp location = new GeoContentApp(new PointGeometryApp(23, 32), "example", "Regular");
//        MessageApp msgL = new MessageGeoApp("sender1", location);

        Intent iT = mock(Intent.class);
//        when(iT.getStringExtra(any(String.class))).thenReturn(GsonUtil.toJson(msgT));
//
//        Intent iL = mock(Intent.class);
//        when(iL.getStringExtra(any(String.class))).thenReturn(GsonUtil.toJson(msgL));

        MessageBroadcastReceiver mbr1 = new MessageBroadcastReceiver(mMessageHandler, MessageApp.TEXT);
        MessageBroadcastReceiver mbr2 = new MessageBroadcastReceiver(mMessageHandler, MessageApp.GEO);


        LocalBroadcastManager.getInstance(mShadowContext).registerReceiver(
                mbr1, mbr1.getIntentFilter());
        LocalBroadcastManager.getInstance(mShadowContext).registerReceiver(
                mbr2, mbr2.getIntentFilter());

        //Act
//        Intent intent1 = new Intent(msgT.getClass().getName());
//        intent1.putExtra(MessageBroadcastReceiver.MESSAGE, GsonUtil.toJson(msgT));
//        Intent intent2 = new Intent(msgT.getClass().getName());
//        intent2.putExtra(MessageBroadcastReceiver.MESSAGE, GsonUtil.toJson(msgL));

        //Assert
//        LocalBroadcastManager.getInstance(mShadowContext).sendBroadcast(intent2);
//        verify(mMessageHandler, times(1)).onNewMessage(isA(MessageGeoApp.class));
//
//        LocalBroadcastManager.getInstance(mShadowContext).sendBroadcast(intent1);
//        verify(mMessageHandler, times(1)).onNewMessage(isA(MessageTextApp.class));
    }

    @Test()
    public void testOnReceive_severalBroadcastReceivers_shouldCallOnReceiveTwoTimes() throws Exception {
        //Arrange
//        MessageApp msgT = new MessageTextApp("sender1", "text123");
//        Intent iT = mock(Intent.class);
//        when(iT.getStringExtra(any(String.class))).thenReturn(GsonUtil.toJson(msgT));

        MessageBroadcastReceiver mbr1 = new MessageBroadcastReceiver(mMessageHandler, MessageApp.TEXT);
        MessageBroadcastReceiver mbr2 = new MessageBroadcastReceiver(mMessageHandler, MessageApp.TEXT);
        LocalBroadcastManager.getInstance(mShadowContext).registerReceiver(
                mbr1, mbr1.getIntentFilter());
        LocalBroadcastManager.getInstance(mShadowContext).registerReceiver(
                mbr2, mbr2.getIntentFilter());

        //Act
//        Intent intent1 = new Intent(msgT.getClass().getName());
//        intent1.putExtra(MessageBroadcastReceiver.MESSAGE, GsonUtil.toJson(msgT));

        //Assert
//        LocalBroadcastManager.getInstance(mShadowContext).sendBroadcast(intent1);
        verify(mMessageHandler, times(2)).onNewMessage(any(MessageGeoApp.class));
    }

}