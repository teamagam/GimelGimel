package com.teamagam.gimelgimel.data.response.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.geogson.core.gson.GeometryAdapterFactory;
import com.teamagam.geogson.core.model.Coordinates;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.gimelgimel.data.response.entity.GGResponse;
import com.teamagam.gimelgimel.data.response.entity.GeometryResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageResponse;
import com.teamagam.gimelgimel.data.response.entity.TextResponse;
import com.teamagam.gimelgimel.data.response.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.contents.ImageMetadataData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class MessageJsonAdapterTest {

  @Test
  public void checkMessageTypeJsonConverter() {

    //Arrange
    String senderId = "sender1";
    GeoContentData location =
        new GeoContentData(new Point(new SinglePosition(Coordinates.of(23, 32))), "example",
            "Regular");

    GGResponse msgText = new TextResponse("text123");
    GGResponse msgGeo = new GeometryResponse(location);
    GGResponse msgImage =
        new ImageResponse(new ImageMetadataData(123, ImageMetadataData.SENSOR));
    GGResponse[] messages = new GGResponse[] { msgText, msgGeo, msgImage };
    for (GGResponse msg : messages) {
      msg.setSenderId(senderId);
    }

    Gson gson = new GsonBuilder()
        //                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapterFactory(new GeometryAdapterFactory())
        .registerTypeAdapter(GGResponse.class, new MessageJsonAdapter())
        .create();

    //Act
    for (GGResponse msg : messages) {
      String msgJson = gson.toJson(msg, GGResponse.class);
      System.out.println("serialized with the custom serializer:" + msgJson);
      GGResponse msgObj = gson.fromJson(msgJson, GGResponse.class);
      //Assert
      assertEquals(msgObj.getClass(), msg.getClass());
    }
  }
}

