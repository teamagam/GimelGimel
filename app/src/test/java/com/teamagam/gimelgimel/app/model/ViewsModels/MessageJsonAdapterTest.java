package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.junit.Test;


/**
 */
public class MessageJsonAdapterTest {

    @Test
    public void checkMessageTypeJsonConverter(){

        String senderId = "sender1";

        String text = "text123";
        Message msgT = new MessageText(senderId, text);
        Message msgL = new MessageLatLong(senderId, new PointGeometry(23,32));
        Message[] messages = new Message[]{msgT, msgL};

        Gson gson = new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Message.class, new MessageJsonAdapter())
                .create();

            for (Message msg : messages) {
                String msgJson = gson.toJson(msg, Message.class);
                System.out.println("serialized with the custom serializer:"  + msgJson);
                Message msgObj = gson.fromJson(msgJson, Message.class);
                System.out.println(msgObj.toString());
            }
        }

}

