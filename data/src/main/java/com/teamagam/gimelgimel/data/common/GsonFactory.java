package com.teamagam.gimelgimel.data.common;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.geogson.core.gson.GeometryAdapterFactory;
import com.teamagam.gimelgimel.data.response.adapters.ResponseJsonAdapter;
import com.teamagam.gimelgimel.data.response.adapters.ResponseListJsonAdapter;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import java.util.List;
import retrofit2.converter.gson.GsonConverterFactory;

public class GsonFactory {

  public static Gson getMessagingGson() {
    return Singleton.GSON_INSTANCE;
  }

  public static GsonConverterFactory getMessagingGsonConverterFactory() {
    return Singleton.GSON_CONVERTER_FACTORY;
  }

  private static class Singleton {
    private static final Gson GSON_INSTANCE = createGson();
    private static final GsonConverterFactory GSON_CONVERTER_FACTORY = createGsonConverterFactory();

    private static Gson createGson() {
      ResponseJsonAdapter responseJsonAdapter = new ResponseJsonAdapter();
      return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .registerTypeAdapterFactory(new GeometryAdapterFactory())
          .registerTypeAdapter(ServerResponse.class, responseJsonAdapter)
          .registerTypeAdapter(List.class, new ResponseListJsonAdapter(responseJsonAdapter))
          .create();
    }

    private static GsonConverterFactory createGsonConverterFactory() {
      return GsonConverterFactory.create(GSON_INSTANCE);
    }
  }
}
