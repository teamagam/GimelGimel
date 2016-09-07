package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.message.model.MessageGeo;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;


/**
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class MessageJsonAdapterTest {

    @Test
    public void checkMessageTypeJsonConverter() {

        //Arrange
        String senderId = "sender1";
        GeoContent location = new GeoContent(new PointGeometry(23, 32), "example", "Regular");

        Message msgText = new MessageText(senderId, "text123");
        Message msgGeo = new MessageGeo(senderId, location);
        Message msgImage = new MessageImage(senderId, new ImageMetadata(123, ImageMetadata.SENSOR));
        Message[] messages = new Message[]{msgText, msgGeo, msgImage};

        Gson gson = new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Message.class, new MessageJsonAdapter())
                .create();

        //Act
        for (Message msg : messages) {
            String msgJson = gson.toJson(msg, Message.class);
            System.out.println("serialized with the custom serializer:" + msgJson);
            Message msgObj = gson.fromJson(msgJson, Message.class);
            //Assert
            assertEquals(msgObj.getClass(),msg.getClass());
        }
    }

}

