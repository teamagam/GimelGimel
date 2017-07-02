package com.teamagam.gimelgimel.data.response.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResponseListJsonAdapter implements JsonDeserializer<List> {

  private static final Logger sLogger =
      LoggerFactory.create(ResponseListJsonAdapter.class.getSimpleName());

  private ResponseJsonAdapter mResponseJsonAdapter;

  public ResponseListJsonAdapter(ResponseJsonAdapter responseJsonAdapter) {
    mResponseJsonAdapter = responseJsonAdapter;
  }

  @Override
  public List deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonArray array = json.getAsJsonArray();
    return deserializeMessagesArrayJson(context, array);
  }

  private List deserializeMessagesArrayJson(JsonDeserializationContext context, JsonArray array) {
    List<ServerResponse> messages = new ArrayList<>(array.size());

    for (JsonElement messageJson : array) {
      addToMessagesIfParsable(context, messageJson, messages);
    }

    return messages;
  }

  private void addToMessagesIfParsable(JsonDeserializationContext context,
      JsonElement messageJson,
      List<ServerResponse> dstMessageList) {
    try {
      JsonObject messageJsonObject = messageJson.getAsJsonObject();
      ServerResponse m = mResponseJsonAdapter.deserialize(messageJsonObject, null, context);
      dstMessageList.add(m);
    } catch (JsonParseException ex) {
      sLogger.w("The following message parsing failed:  " + messageJson.toString(), ex);
    }
  }
}
