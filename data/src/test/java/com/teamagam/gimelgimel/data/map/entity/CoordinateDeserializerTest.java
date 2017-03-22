package com.teamagam.gimelgimel.data.map.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CoordinateDeserializerTest {

    private Gson mGson;

    @Before
    public void setUp() throws Exception {
        mGson = new GsonBuilder()
                .registerTypeAdapter(Coordinate.class, new Coordinate.CoordinateDeserializer())
                .create();
    }

    @Test
    public void deserialize() throws Exception {
        //Arrange
        float lat = 10f;
        float lng = 12f;
        String coordinateJson = createCoordinateJson(lat, lng);

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
        int lat = 10;
        int lng = 12;
        Coordinate coordinate = new Coordinate(lat, lng);

        //Act
        JsonElement jsonElement = mGson.toJsonTree(coordinate, Coordinate.class);

        //Assert
        assertNotNull(jsonElement);
        assertEquals(jsonElement.toString(), createCoordinateJson(lat, lng));
    }

    private String createCoordinateJson(float lat, float lng) {
        return "[" + lng + "," + lat + "]";
    }
}