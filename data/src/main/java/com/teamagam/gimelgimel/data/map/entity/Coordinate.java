package com.teamagam.gimelgimel.data.map.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

import java.lang.reflect.Type;

public class Coordinate implements GeometryData {

    private float mLat;
    private float mLng;

    public Coordinate(float lat, float lng) {
        mLat = lat;
        mLng = lng;
    }

    public float getLat() {
        return mLat;
    }

    public float getLng() {
        return mLng;
    }

    @Override
    public PointGeometry toModel() {
        return new PointGeometry(mLat, mLng);
    }

    public static class CoordinateDeserializer implements
            JsonSerializer<Coordinate>,
            JsonDeserializer<Coordinate> {

        @Override
        public Coordinate deserialize(JsonElement json, Type typeOfT,
                                      JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = json.getAsJsonArray();
            float lng = jsonArray.get(0).getAsFloat();
            float lat = jsonArray.get(1).getAsFloat();
            return new Coordinate(lat, lng);
        }

        @Override
        public JsonElement serialize(Coordinate src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            JsonArray jsonElements = new JsonArray();
            jsonElements.add(src.mLng);
            jsonElements.add(src.mLat);
            return jsonElements;
        }
    }
}
