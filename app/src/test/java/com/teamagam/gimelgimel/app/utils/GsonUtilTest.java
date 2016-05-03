package com.teamagam.gimelgimel.app.utils;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageLatLong;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * Created on 5/2/2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class GsonUtilTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGsonUtil_fromJson_shouldBeEqual() throws Exception {
        //Act
        String senderId = "sender1";
        String text = "text123";

        Message msgT = new MessageText(senderId, text);
        Message msgL = new MessageLatLong(senderId, new PointGeometry(23, 32));
        Message[] messages = new Message[]{msgT, msgL};


        for (Message msg : messages) {
            String msgJson = GsonUtil.toJson(msg);
            Message msgObj = GsonUtil.fromJson(msgJson, Message.class);
            assertEquals(msgObj.getClass(), msg.getClass());
        }
    }

}
