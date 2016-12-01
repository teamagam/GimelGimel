package com.teamagam.gimelgimel.data.message.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 */
public class MessageJsonAdapterTest {

    @Test
    public void checkMessageTypeJsonConverter() {

        //Arrange
        String senderId = "sender1";
        GeoContentData location = new GeoContentData(new PointGeometryData(23, 32), "example",
                "Regular");

        MessageData msgText = new MessageTextData("text123");
        MessageData msgGeo = new MessageGeoData(location);
        MessageData msgImage = new MessageImageData(new ImageMetadataData(123,
                ImageMetadataData.SENSOR));
        MessageData[] messages = new MessageData[]{msgText, msgGeo, msgImage};
        for (MessageData msg : messages) {
            msg.setSenderId(senderId);
        }

        Gson gson = new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(MessageData.class, new MessageJsonAdapter())
                .create();

        //Act
        for (MessageData msg : messages) {
            String msgJson = gson.toJson(msg, MessageData.class);
            System.out.println("serialized with the custom serializer:" + msgJson);
            MessageData msgObj = gson.fromJson(msgJson, MessageData.class);
            //Assert
            assertEquals(msgObj.getClass(), msg.getClass());
        }
    }
}

