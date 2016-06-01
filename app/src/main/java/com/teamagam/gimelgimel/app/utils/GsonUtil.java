package com.teamagam.gimelgimel.app.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageJsonAdapter;

/**
 * Created on 5/2/2016.
 * mainly used for {@link MessageJsonAdapter}
 */
public class GsonUtil {

    private static final Gson sGson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Message.class, new MessageJsonAdapter())
            .create();

    public static String toJson(Message msg) {
        return sGson.toJson(msg);
    }

    public static <T extends Message> T fromJson(String json, Class<T> clazz) {
        return sGson.fromJson(json, clazz);
    }

    public static JsonElement toJsonElement(Object content) {
        return sGson.toJsonTree(content);
    }
}
