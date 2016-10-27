package com.teamagam.gimelgimel.app.utils;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.MessageUserLocationApp;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
import com.teamagam.gimelgimel.app.message.model.contents.LocationSample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * Created on 5/2/2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class GsonUtilTest {

    @Test
    public void testGsonUtil_toJsonText_shouldBeEqual() throws Exception {
        //Arrange
        String senderId = "sender1";
        String text = "text123";
        MessageApp msg = new MessageTextApp(senderId, text);
        String targetJson = "{\"content\":\"text123\",\"senderId\":\"sender1\",\"type\":\"Text\"}";

        //Act
        String msgJson = GsonUtil.toJson(msg);

        //Assert
        assertEquals(msgJson, targetJson);
    }

    @Test
    public void testGsonUtil_toJsonGeo_shouldBeEqual() throws Exception {
        //Arrange
        String senderId = "sender1";
        GeoContentApp location = new GeoContentApp( new PointGeometryApp(23, 32), "example", "Regular");
        MessageApp msg = new MessageGeoApp(senderId,location);

        String targetJson = "{\"content\":{\"location\":{\"latitude\":23.0,\"longitude\":32.0,\"altitude\":0.0,\"hasAltitude\":false},\"text\":\"example\",\"locationType\":\"Regular\"},\"senderId\":\"sender1\",\"type\":\"Geo\"}";


        //Act
        String msgJson = GsonUtil.toJson(msg);

        //Assert
        assertEquals(msgJson, targetJson);
    }

    @Test
    public void testGsonUtil_toJsonUserLocation_shouldBeEqual() throws Exception {
        //Arrange
        String senderId = "sender1";
        MessageApp msg = new MessageUserLocationApp(senderId, new LocationSample(new PointGeometryApp(23, 32), 1462289579954L));
        String targetJson = "{\"content\":{\"location\":{\"latitude\":23.0,\"longitude\":32.0,\"altitude\":0.0,\"hasAltitude\":false},\"timeStamp\":1462289579954,\"hasSpeed\":false,\"speed\":0.0,\"hasBearing\":false,\"bearing\":0.0,\"hasAccuracy\":false,\"accuracy\":0.0},\"senderId\":\"sender1\",\"type\":\"UserLocation\"}";

        //Act
        String msgJson = GsonUtil.toJson(msg);

        //Assert
        assertEquals(msgJson, targetJson);
    }


    @Test
    public void testGsonUtil_fromJsonText_shouldBeEqual() throws Exception {
        //Arrange
        String senderId = "sender1";
        String text = "text123";
        MessageApp msg = new MessageTextApp(senderId, text);

        //Act
        String msgJson = GsonUtil.toJson(msg);
        MessageApp msgObj = GsonUtil.fromJson(msgJson, MessageApp.class);

        //Assert
        assertEquals(msgObj.getClass(), msg.getClass());
    }

    @Test
    public void testGsonUtil_fromJsonGeo_shouldBeEqual() throws Exception {
        //Arrange
        String senderId = "sender1";
        GeoContentApp location = new GeoContentApp( new PointGeometryApp(23, 32), "example", "Regular");
        MessageApp msg = new MessageGeoApp(senderId,location);

        //Act
        String msgJson = GsonUtil.toJson(msg);
        MessageApp msgObj = GsonUtil.fromJson(msgJson, MessageApp.class);

        //Assert
        assertEquals(msgObj.getClass(), msg.getClass());
    }

    @Test
    public void testGsonUtil_fromJsonUserLocation_shouldBeEqual() throws Exception {
        //Arrange
        String senderId = "sender1";
        MessageApp msg = new MessageUserLocationApp(senderId, new LocationSample(new PointGeometryApp(23, 32), 1462289579954L));

        //Act
        String msgJson = GsonUtil.toJson(msg);
        MessageApp msgObj = GsonUtil.fromJson(msgJson, MessageApp.class);

        //Assert
        assertEquals(msgObj.getClass(), msg.getClass());
    }

}
