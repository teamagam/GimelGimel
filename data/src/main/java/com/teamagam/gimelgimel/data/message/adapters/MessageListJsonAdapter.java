package com.teamagam.gimelgimel.data.message.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles list of messages deserialization.
 * Will "tolerate" badly formatted message JSONs, by skipping them.
 */
public class MessageListJsonAdapter implements JsonDeserializer<List> {

    private static final Logger sLogger = LoggerFactory.create(
            MessageListJsonAdapter.class.getSimpleName());

    private MessageJsonAdapter mMessageJsonAdapter;

    public MessageListJsonAdapter(MessageJsonAdapter messageJsonAdapter) {
        mMessageJsonAdapter = messageJsonAdapter;
    }

    @Override
    public List deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        return deserializeMessagesArrayJson(context, array);
    }

    private List deserializeMessagesArrayJson(JsonDeserializationContext context,
                                              JsonArray array) {
        List<MessageData> messages = new ArrayList<>(array.size());

        for (JsonElement messageJson : array) {
            addToMessagesIfParsable(context, messageJson, messages);
        }

        return messages;
    }

    private void addToMessagesIfParsable(JsonDeserializationContext context,
                                         JsonElement messageJson,
                                         List<MessageData> dstMessageList) {
        try {
            JsonObject messageJsonObject = messageJson.getAsJsonObject();
            MessageData m = mMessageJsonAdapter.deserialize(messageJsonObject, null, context);
            dstMessageList.add(m);
        } catch (JsonParseException ex) {
            sLogger.w("The following message parsing failed:  " + messageJson.toString(), ex);
        }
    }
}
