package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


/**
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class MessageJsonAdapterTest {

    @Test
    public void checkMessageTypeJsonConverter() {

        //Arrange
        String senderId = "sender1";
        GeoContentApp location = new GeoContentApp(new PointGeometryApp(23, 32), "example", "Regular");

//        MessageApp msgText = new MessageTextApp(senderId, "text123");
//        MessageApp msgGeo = new MessageGeoApp(senderId, location);
//        MessageApp msgImage = new MessageImageApp(senderId, new ImageMetadataApp(123, ImageMetadataApp.SENSOR));
//        MessageApp[] messages = new MessageApp[]{msgText, msgGeo, msgImage};

        Gson gson = new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(MessageApp.class, new MessageJsonAdapter())
                .create();

        //Act
//        for (MessageApp msg : messages) {
//            String msgJson = gson.toJson(msg, MessageApp.class);
//            System.out.println("serialized with the custom serializer:" + msgJson);
//            MessageApp msgObj = gson.fromJson(msgJson, MessageApp.class);
//            //Assert
//            assertEquals(msgObj.getClass(),msg.getClass());
//        }
    }

}

