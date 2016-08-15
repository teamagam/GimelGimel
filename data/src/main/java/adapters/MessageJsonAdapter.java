package adapters;

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

import httpModels.Message;
import httpModels.contents.GeoContent;
import httpModels.contents.ImageMetadata;
import httpModels.contents.LocationSample;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * A JsonAdapter to convert from Message Polymorphisms to JSON (Serializer)
 * and from JSON to Message (Deserializer)
 * <p/>
 * the specific class is determined by the type of the message.
 * <p/>
 * This adapter is based on gson and used by retrofit
 */
public class MessageJsonAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    protected static Map<String, Class> sClassMessageMap = new TreeMap<>();

    static {
        sClassMessageMap.put(Message.TEXT, String.class);
        sClassMessageMap.put(Message.GEO, GeoContent.class);
        sClassMessageMap.put(Message.USER_LOCATION, LocationSample.class);
        sClassMessageMap.put(Message.IMAGE, ImageMetadata.class);

    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String type = json.getAsJsonObject().get("type").getAsString();
        Class genericClass = sClassMessageMap.get(type);

        if (genericClass == null)
            throw new JsonParseException("Unknown message class: " + type);

        ParameterizedTypeImpl classToDeserialize =
                ParameterizedTypeImpl.make(Message.class, new Type[]{genericClass}, null);

        return context.deserialize(json, classToDeserialize.getRawType());
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