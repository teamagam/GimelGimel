/*
package com.teamagam.gimelgimel.app.network.services.message_polling;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.network.services.message_polling.polling.IMessageBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.polling.PolledMessagesBroadcaster;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = Config.NONE)
public class PolledMessagesBroadcasterTest {

    private PolledMessagesBroadcaster mPolledMessageBroadcaster;
    private IMessageBroadcaster mMessageBroadcasterMock;

    @Before
    public void setUp() throws Exception {
        mMessageBroadcasterMock = mock(IMessageBroadcaster.class);
        mPolledMessageBroadcaster = new PolledMessagesBroadcaster(mMessageBroadcasterMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcess_nullArgument_shouldThrow() throws Exception {
        //Act
        mPolledMessageBroadcaster.process(null);
    }

    @Test
    public void testProcess_multipleMessagesCollection_shouldBroadcastAll() throws Exception {
        //Arrange
        MessageApp msg1 = mock(MessageApp.class);
        MessageApp msg2 = mock(MessageApp.class);
        MessageApp msg3 = mock(MessageApp.class);

        Collection<MessageApp> messages = Arrays.asList(msg1, msg2, msg3);

        //Act
        mPolledMessageBroadcaster.process(messages);

        //Assert
        verify(mMessageBroadcasterMock, times(1)).broadcast(msg1);
        verify(mMessageBroadcasterMock, times(1)).broadcast(msg2);
        verify(mMessageBroadcasterMock, times(1)).broadcast(msg3);
    }
}*/
