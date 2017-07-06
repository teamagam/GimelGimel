package com.teamagam.gimelgimel.data.response.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.geogson.core.gson.GeometryAdapterFactory;
import com.teamagam.geogson.core.model.Coordinates;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.gimelgimel.data.response.entity.GeometryMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.TextMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.contents.ImageMetadataData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class ResponseJsonAdapterTest {

  @Test
  public void checkMessageTypeJsonConverter() {

    //Arrange
    String senderId = "sender1";
    GeoContentData location =
        new GeoContentData(new Point(new SinglePosition(Coordinates.of(23, 32))), "example");

    ServerResponse msgText = new TextMessageResponse("text123");
    ServerResponse msgGeo = new GeometryMessageResponse(location);
    ServerResponse msgImage =
        new ImageMessageResponse(new ImageMetadataData(123, ImageMetadataData.SENSOR));
    ServerResponse[] messages = new ServerResponse[] { msgText, msgGeo, msgImage };
    for (ServerResponse msg : messages) {
      msg.setSenderId(senderId);
    }

    Gson gson = new GsonBuilder()
        //                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapterFactory(new GeometryAdapterFactory())
        .registerTypeAdapter(ServerResponse.class, new ResponseJsonAdapter())
        .create();

    //Act
    for (ServerResponse msg : messages) {
      String msgJson = gson.toJson(msg, ServerResponse.class);
      System.out.println("serialized with the custom serializer:" + msgJson);
      ServerResponse msgObj = gson.fromJson(msgJson, ServerResponse.class);
      //Assert
      assertEquals(msgObj.getClass(), msg.getClass());
    }
  }
}

