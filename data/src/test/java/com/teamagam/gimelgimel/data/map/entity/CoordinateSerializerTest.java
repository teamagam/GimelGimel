package com.teamagam.gimelgimel.data.map.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CoordinateSerializerTest {

    private Gson mGson;

    @Before
    public void setUp() throws Exception {
        mGson = new GsonBuilder()
                .registerTypeAdapter(Coordinate.class, new Coordinate.CoordinateSerializer())
                .create();
    }

    @Test
    public void deserialize() throws Exception {
        //Arrange
        float lng = 12f;
        float lat = 10f;
        String coordinateJson = createCoordinateJson(lng, lat);

        //Act
        Coordinate coordinate = mGson.fromJson(coordinateJson, Coordinate.class);

        //Assert
        assertNotNull(coordinate);
        assertEquals(coordinate.getLat(), 10f, 1E-6);
        assertEquals(coordinate.getLng(), 12f, 1E-6);
    }

    @Test
    public void serialize() throws Exception {
        //Arrange
        int lng = 12;
        int lat = 10;
        Coordinate coordinate = new Coordinate(lng, lat);

        //Act
        JsonElement jsonElement = mGson.toJsonTree(coordinate, Coordinate.class);

        //Assert
        assertNotNull(jsonElement);
        assertEquals(jsonElement.toString(), createCoordinateJson(lng, lat));
    }

    private String createCoordinateJson(float lng, float lat) {
        return "[" + lng + "," + lat + "]";
    }
}