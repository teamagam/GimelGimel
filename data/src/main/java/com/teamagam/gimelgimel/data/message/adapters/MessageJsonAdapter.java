package com.teamagam.gimelgimel.data.message.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageSensorData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.MessageVectorLayerData;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * A JsonAdapter to convert from Message Polymorphisms to JSON (Serializer)
 * and from JSON to Message (Deserializer)
 * <p/>
 * the specific class is determined by the type of the message.
 * <p/>
 * This adapter is based on gson and used by retrofit
 */
public class MessageJsonAdapter implements JsonSerializer<MessageData>, JsonDeserializer<MessageData> {

    private static Map<String, Class> sClassMessageMap = new TreeMap<>();

    static {
        sClassMessageMap.put(MessageData.TEXT, MessageTextData.class);
        sClassMessageMap.put(MessageData.GEO, MessageGeoData.class);
        sClassMessageMap.put(MessageData.USER_LOCATION, MessageUserLocationData.class);
        sClassMessageMap.put(MessageData.IMAGE, MessageImageData.class);
        sClassMessageMap.put(MessageData.SENSOR, MessageSensorData.class);
        sClassMessageMap.put(MessageData.VECTOR_LAYER, MessageVectorLayerData.class);
    }

    @Override
    public MessageData deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {

        String type = json.getAsJsonObject().get("type").getAsString();
        Class genericClass = sClassMessageMap.get(type);

        if (genericClass == null) {
            throw new JsonParseException("Unknown message class: " + type);
        }

        return context.deserialize(json, genericClass);
    }

    @Override
    public JsonElement serialize(MessageData msg, Type typeOfSrc,
                                 JsonSerializationContext context) {
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
