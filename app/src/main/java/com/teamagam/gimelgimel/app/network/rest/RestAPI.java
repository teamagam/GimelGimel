package com.teamagam.gimelgimel.app.network.rest;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageJsonAdapter;
import com.teamagam.gimelgimel.app.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class for exposing different remote APIs
 */
public class RestAPI {

    private static RestAPI sInstance = new RestAPI();

    public static RestAPI getInstance() {
        return sInstance;
    }

    private RestAPI() {
    }

    public GGMessagingAPI getMessagingAPI() {
        return GGMessagingLazyInitializer.GGMessagingAPI;
    }

    /**
     * This class is used for lazy initialization of GGMessaging.
     * Efficiently prevents synchronization problems with instantiation of a single object.
     */
    private static class GGMessagingLazyInitializer {
        public static final GGMessagingAPI GGMessagingAPI = initializeMessagingAPI();

        private static GGMessagingAPI initializeMessagingAPI() {
            //http logger for debugging
            final Logger logger = LoggerFactory.create("GGMessagingHttpClient");
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                    new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            logger.v(message);
                        }
                    });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(
                    interceptor).build();

            // The following code creates a new Gson instance that will convert all fields from lower
            // case with underscores to camel case and vice versa. It also registers a type adapter for
            // the Message class. This DateTypeAdapter will be used anytime Gson encounters a Date field.
            // The gson instance is passed as a parameter to GsonConverter, which is a wrapper
            // class for converting types.
            MessageJsonAdapter messageJsonAdapter = new MessageJsonAdapter();
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(Message.class, messageJsonAdapter)
                    .registerTypeAdapter(List.class,
                            new MessageListJsonAdapter(messageJsonAdapter))
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.MESSAGING_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit.create(GGMessagingAPI.class);
        }

        /**
         * Handles list of messages deserialization.
         * Will "tolerate" badly formatted message JSONs, by skipping them.
         */
        private static class MessageListJsonAdapter implements JsonDeserializer<List> {
            private static Logger sLogger = LoggerFactory.create();

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
                List<Message> messages = new ArrayList<>(array.size());

                for (JsonElement messageJson : array) {
                    addToMessagesIfParsable(context, messageJson, messages);
                }

                return messages;
            }

            private void addToMessagesIfParsable(JsonDeserializationContext context,
                                                 JsonElement messageJson,
                                                 List<Message> dstMessageList) {
                try {
                    JsonObject messageJsonObject = messageJson.getAsJsonObject();
                    Message m = mMessageJsonAdapter.deserialize(messageJsonObject, null, context);
                    dstMessageList.add(m);
                } catch (JsonParseException ex) {
                    sLogger.w("Parsing message failed", ex);
                }
            }
        }
    }
}
