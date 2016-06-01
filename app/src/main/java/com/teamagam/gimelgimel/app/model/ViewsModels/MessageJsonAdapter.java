package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.teamagam.gimelgimel.app.utils.GsonUtil;

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
        sClassMessageMap.put(Message.IMAGE, MessageImage.class);

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

        JsonElement contentElem = context.serialize(msg.getContent());
        JsonObject retValue = createBasicJsonMessage(msg.getType(), msg.getSenderId(), contentElem);

        retValue.addProperty("_id", msg.getMessageId());
        retValue.add("createdAt", context.serialize(msg.getCreatedAt()));

        return retValue;
    }

    /**
     * used for Factoring Messages by type and content.
     * @param content - content of the corresponding message
     * @param type - type of the message {@link Message.MessageType}
     * @param senderId
     * @return - specific object of {@link Message<type>}
     */
    public static Message  createCustomMessage(Object content, @Message.MessageType String type, String senderId){

        JsonElement contentElem = GsonUtil.toJsonElement(content);
        JsonObject json = createBasicJsonMessage(type, senderId, contentElem);

        return GsonUtil.fromJson(json.toString(), sClassMessageMap.get(type));
    }

    private static JsonObject createBasicJsonMessage(@Message.MessageType String type, String senderId, JsonElement contentElem) {

        JsonObject json = new JsonObject();

        json.addProperty("senderId", senderId);
        json.addProperty("type", type);
        json.add("content", contentElem);

        return json;
    }

}
