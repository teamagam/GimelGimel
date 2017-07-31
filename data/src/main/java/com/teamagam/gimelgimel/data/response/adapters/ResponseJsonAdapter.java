package com.teamagam.gimelgimel.data.response.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.teamagam.gimelgimel.data.response.entity.AlertMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.DynamicLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.GeometryMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.TextMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.UnknownResponse;
import com.teamagam.gimelgimel.data.response.entity.UserLocationResponse;
import com.teamagam.gimelgimel.data.response.entity.VectorLayerResponse;
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
public class ResponseJsonAdapter
    implements JsonSerializer<ServerResponse>, JsonDeserializer<ServerResponse> {

  private static Map<String, Class> sClassMessageMap = new TreeMap<>();

  static {
    sClassMessageMap.put(ServerResponse.TEXT, TextMessageResponse.class);
    sClassMessageMap.put(ServerResponse.GEO, GeometryMessageResponse.class);
    sClassMessageMap.put(ServerResponse.USER_LOCATION, UserLocationResponse.class);
    sClassMessageMap.put(ServerResponse.IMAGE, ImageMessageResponse.class);
    sClassMessageMap.put(ServerResponse.VECTOR_LAYER, VectorLayerResponse.class);
    sClassMessageMap.put(ServerResponse.DYNAMIC_LAYER, DynamicLayerResponse.class);
    sClassMessageMap.put(ServerResponse.ALERT, AlertMessageResponse.class);
  }

  @Override
  public ServerResponse deserialize(JsonElement json,
      Type typeOfT,
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

  private ServerResponse deserializeToDummy(JsonObject jsonObject) {
    try {
      String date = (jsonObject.get("createdAt").getAsString());
      Date createdAt = ISO8601Utils.parse(date, new ParsePosition(0));
      return new UnknownResponse(createdAt);
    } catch (ParseException e) {
      throw new JsonParseException("Couldn't parse date of message: " + jsonObject.getAsString(),
          e);
    }
  }

  @Override
  public JsonElement serialize(ServerResponse msg,
      Type typeOfSrc,
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
