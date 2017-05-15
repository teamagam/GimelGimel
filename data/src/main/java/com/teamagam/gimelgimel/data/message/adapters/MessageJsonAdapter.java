package com.teamagam.gimelgimel.data.message.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.teamagam.gimelgimel.data.message.entity.UnknownMessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageAlertData;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageSensorData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.MessageVectorLayerData;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
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
        sClassMessageMap.put(MessageData.ALERT, MessageAlertData.class);
    }

    @Override
    public MessageData deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {

        try {
            String type = json.getAsJsonObject().get("type").getAsString();
            Class genericClass = sClassMessageMap.get(type);

            if (genericClass == null) {
                return deserializeToDummy(json.getAsJsonObject());
            }

            return context.deserialize(json, genericClass);
        } catch (IllegalStateException | JsonParseException ex) {
            return deserializeToDummy(json.getAsJsonObject());
        }
    }

    private MessageData deserializeToDummy(JsonObject jsonObject) {
        try {
            String date = (jsonObject.get("createdAt").getAsString());
            Date createdAt = ISO8601Utils.parse(date, new ParsePosition(0));
            return new UnknownMessageData(createdAt);
        } catch (ParseException e) {
            throw new JsonParseException(
                    "Couldn't parse date of message: " + jsonObject.getAsString(), e);
        }
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
