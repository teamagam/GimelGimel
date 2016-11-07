package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.MessageUserLocationApp;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * A JsonAdapter to convert from MessageApp Polymorphisms to JSON (Serializer)
 * and from JSON to MessageApp (Deserializer)
 *
 * the specific class is determined by the type of the message.
 * <p/>
 * This adapter is based on gson and used by retrofit
 */
public class MessageJsonAdapter implements JsonSerializer<MessageApp>, JsonDeserializer<MessageApp> {

    protected static Map<String, Class> sClassMessageMap = new TreeMap<>();

    static {
        sClassMessageMap.put(MessageApp.TEXT, MessageTextApp.class);
        sClassMessageMap.put(MessageApp.GEO, MessageGeoApp.class);
        sClassMessageMap.put(MessageApp.USER_LOCATION, MessageUserLocationApp.class);
        sClassMessageMap.put(MessageApp.IMAGE, MessageImageApp.class);

    }

    @Override
    public MessageApp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String type = json.getAsJsonObject().get("type").getAsString();
        Class c = sClassMessageMap.get(type);
        if (c == null)
            throw new JsonParseException("Unknown message class: " + type);

        return context.deserialize(json, c);
    }

    @Override
    public JsonElement serialize(MessageApp msg, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();

        retValue.addProperty("type", msg.getType());
        retValue.addProperty("_id", msg.getMessageId());
        retValue.addProperty("senderId", msg.getSenderId());

        retValue.add("createdAt", context.serialize(msg.getCreatedAt()));

        JsonElement contentElem = context.serialize(msg.getContent());
        retValue.add("content", contentElem);
        return retValue;
    }
}
