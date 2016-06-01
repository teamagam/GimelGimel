package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 */
public class MessageJsonAdapterTest {

    @Test
    public void checkMessageTypeJsonConverter() {

        //Arrange
        String senderId = "sender1";
        Message msgT = new MessageText(senderId, "text123");
        Message msgL = new MessageLatLong(senderId, new PointGeometry(23, 32));
        Message msgI = new MessageImage(senderId, new ImageMetadata(123, ImageMetadata.Sensor));
        Message[] messages = new Message[]{msgT, msgL, msgI};

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

