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
import com.teamagam.gimelgimel.data.response.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.IconData;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ResponseJsonAdapterTest {

  public static final String SENDER_ID = "senderId1";
  private Gson mGson;

  @Before
  public void setUp() throws Exception {
    mGson = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory())
        .registerTypeAdapter(ServerResponse.class, new ResponseJsonAdapter())
        .create();
  }

  @Test
  public void deserializeTextMessageResponse() throws Exception {
    //Arrange
    String text = "text";
    String textMessageJson =
        createResponseJson(SENDER_ID, TextMessageResponse.TEXT, "\"" + text + "\"");

    //Act
    TextMessageResponse tmr = (TextMessageResponse) parseJson(textMessageJson);

    //Assert
    assertResponseCommon(tmr);
    assertThat(tmr.getContent(), equalTo(text));
  }

  @Test
  public void deserializeGeometryMessageResponse() throws Exception {
    //Arrange
    String geoContent = "{\n"
        + "      \"geometry\":{\n"
        + "         \"type\":\"Point\",\n"
        + "         \"coordinates\":[\n"
        + "            34.453125,\n"
        + "            31.052933\n"
        + "         ]\n"
        + "      },\n"
        + "      \"style\":{\n"
        + "         \"icon\":{\n"
        + "            \"id\":\"1\",\n"
        + "            \"color\":\"#FF00FF\"\n"
        + "         }\n"
        + "      },\n"
        + "      \"text\":\"you so happy\"\n"
        + "   }";
    String geometryMessageResponseJson =
        createResponseJson(SENDER_ID, GeometryMessageResponse.GEO, geoContent);

    //Act
    GeometryMessageResponse gmr = (GeometryMessageResponse) parseJson(geometryMessageResponseJson);

    //Assert
    assertResponseCommon(gmr);
    GeoContentData geoContentData = gmr.getContent();
    assertThat(geoContentData.getGeometry(), is(instanceOf(Point.class)));
    assertThat(geoContentData.getText(), is("you so happy"));

    IconData iconData = geoContentData.getStyle().getIconData();
    assertThat(iconData.getIconId(), is("1"));
    assertThat(iconData.getColor(), is("#FF00FF"));
  }

  @Test
  public void checkMessageTypeJsonConverter() {
    //Arrange
    String senderId = "sender1";
    GeoContentData location =
        new GeoContentData("id", new Point(new SinglePosition(Coordinates.of(23, 32))));

    ServerResponse msgText = new TextMessageResponse("text123");
    ServerResponse msgGeo = new GeometryMessageResponse(location);
    ServerResponse msgImage =
        new ImageMessageResponse(new ImageMetadataData(123, ImageMetadataData.SENSOR));
    ServerResponse[] messages = new ServerResponse[] { msgText, msgGeo, msgImage };
    for (ServerResponse msg : messages) {
      msg.setSenderId(senderId);
    }

    //Act
    for (ServerResponse msg : messages) {
      String msgJson = mGson.toJson(msg, ServerResponse.class);
      ServerResponse msgObj = mGson.fromJson(msgJson, ServerResponse.class);
      //Assert
      assertThat(msgObj, is(instanceOf(msg.getClass())));
    }
  }

  private void assertResponseCommon(ServerResponse serverResponse) {
    assertNotNull(serverResponse);
    assertThat(serverResponse.getSenderId(), is(SENDER_ID));
  }

  private String createResponseJson(String senderId, String type, String content) {
    return "{\n"
        + "    \"senderId\":\""
        + senderId
        + "\",\n"
        + "    \"type\" : \""
        + type
        + "\",\n"
        + "    \"content\" : "
        + content
        + "\n"
        + "}";
  }

  private ServerResponse parseJson(String responseJson) {
    return mGson.fromJson(responseJson, ServerResponse.class);
  }
}

