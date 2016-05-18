package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * A JsonAdapter to convert from Message Polymorphisms to JSON (Serializer)
 * and from JSON to Message (Deserializer)
 *
 * the specific class is determined by the type of the message.
 * <p/>
 * This adapter is based on gson and used by retrofit
 */
public class MessageJsonAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    protected static Map<String, Class> sClassMessageMap = new TreeMap<>();

    static {
        sClassMessageMap.put(Message.TEXT, MessageText.class);
        sClassMessageMap.put(Message.LAT_LONG, MessageLatLong.class);
        sClassMessageMap.put(Message.USER_LOCATION, MessageUserLocation.class);
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String type = json.getAsJsonObject().get("type").getAsString();
        Class c = sClassMessageMap.get(type);
        if (c == null)
            throw new RuntimeException("Unknown class: " + type);

        return context.deserialize(json, c);
    }

    @Override
    public JsonElement serialize(Message msg, Type typeOfSrc, JsonSerializationContext context) {
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
