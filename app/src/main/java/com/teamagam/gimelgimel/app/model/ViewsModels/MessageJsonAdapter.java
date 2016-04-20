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
 * Created by Yoni on 4/19/2016.
 */
public class MessageJsonAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    private static Map<String, Class> sClassMessageMap = new TreeMap<>();

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
            return ((Message) context.deserialize(json, c));
    }

    @Override
    public JsonElement serialize(Message msg, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();

        retValue.addProperty("type", msg.getType());
        retValue.addProperty("_id", msg.getMessageId());
        retValue.addProperty("senderId", msg.getSenderId());

        retValue.add("createdAt", context.serialize(msg.getCreatedAt()));

        //should be visitor pattern?
        JsonElement contentElem = null;
        switch (msg.getType()){
            case Message.LAT_LONG:
                contentElem = context.serialize(((MessageLatLong)msg).getPoint());
                break;
            case Message.TEXT:
                contentElem = context.serialize(((MessageText)msg).getText());
                break;
            case Message.USER_LOCATION:
                contentElem = context.serialize(((MessageUserLocation)msg).getLocationSample());
                break;
            default:
        }
        retValue.add("content", contentElem);
        return retValue;
    }
}
