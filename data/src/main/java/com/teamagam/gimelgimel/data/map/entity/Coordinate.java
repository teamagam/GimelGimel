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

    private float mLng;
    private float mLat;

    public Coordinate(float lng, float lat) {
        mLng = lng;
        mLat = lat;
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

    public static class CoordinateSerializer implements
            JsonSerializer<Coordinate>,
            JsonDeserializer<Coordinate> {

        @Override
        public Coordinate deserialize(JsonElement json, Type typeOfT,
                                      JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = json.getAsJsonArray();
            float lng = jsonArray.get(0).getAsFloat();
            float lat = jsonArray.get(1).getAsFloat();
            return new Coordinate(lng, lat);
        }

        @Override
        public JsonElement serialize(Coordinate src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(src.mLng);
            jsonArray.add(src.mLat);
            return jsonArray;
        }
    }
}
