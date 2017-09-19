package com.teamagam.gimelgimel.data.dynamicLayers.remote;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.response.entity.DynamicLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.contents.DynamicLayerData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.data.response.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.DynamicLayerRemoteSourceHandler;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class DataDynamicLayerRemoteSourceHandler implements DynamicLayerRemoteSourceHandler {

  private final DynamicLayersAPI mDynamicLayersAPI;
  private final GeoEntityDataMapper mGeoEntityDataMapper;
  private final UserPreferencesRepository mUserPreferencesRepository;
  private final GGMessagingAPI mGGMessagingAPI;

  @Inject
  public DataDynamicLayerRemoteSourceHandler(DynamicLayersAPI dynamicLayersAPI,
      GeoEntityDataMapper geoEntityDataMapper,
      UserPreferencesRepository userPreferencesRepository,
      GGMessagingAPI ggMessagingAPI) {
    mDynamicLayersAPI = dynamicLayersAPI;
    mGeoEntityDataMapper = geoEntityDataMapper;
    mUserPreferencesRepository = userPreferencesRepository;
    mGGMessagingAPI = ggMessagingAPI;
  }

  @Override
  public void createDynamicLayer(String name, String description) {
    DynamicLayerResponse dlResponse = new DynamicLayerResponse();
    dlResponse.setSenderId(mUserPreferencesRepository.getString(Constants.USERNAME_PREFERENCE_KEY));
    dlResponse.setContent(new DynamicLayerData(null, name, description, new GeoContentData[0]));
    doApiCall(mDynamicLayersAPI.createLayer(dlResponse),
        "Couldn't create new dynamic layer with name = " + name);
  }

  @Override
  public void addEntity(DynamicLayer dynamicLayer, DynamicEntity dynamicEntity) {
    doApiCall(mDynamicLayersAPI.addEntity(dynamicLayer.getId(),
        mGeoEntityDataMapper.transform(dynamicEntity)), "Couldn't add entity to server.");
  }

  @Override
  public void removeEntity(DynamicLayer dynamicLayer, DynamicEntity dynamicEntity) {
    doApiCall(mDynamicLayersAPI.removeEntity(dynamicLayer.getId(), dynamicEntity.getId()),
        "Couldn't remove entity from server.");
  }

  @Override
  public void updateDescription(DynamicLayer dynamicLayer) {
    DynamicLayerResponse dlResponse = new DynamicLayerResponse();
    dlResponse.setSenderId(mUserPreferencesRepository.getString(Constants.USERNAME_PREFERENCE_KEY));
    Function<DynamicEntity, GeoContentData> transformer = mGeoEntityDataMapper::transform;
    List<GeoContentData> geoContentDataList =
        Lists.transform(dynamicLayer.getEntities(), transformer);
    dlResponse.setContent(new DynamicLayerData(dynamicLayer.getId(), dynamicLayer.getName(),
        dynamicLayer.getDescription(), geoContentDataList.toArray(new GeoContentData[0])));
    mGGMessagingAPI.postMessage(dlResponse).subscribe(new ErrorLoggingObserver<>());
  }

  private void doApiCall(Call<Void> call, String errorMessage) {
    try {
      Response<Void> execute = call.execute();

      if (!execute.isSuccessful()) {
        throw new RuntimeException(errorMessage + " error-body " + execute.errorBody().toString());
      }
    } catch (IOException e) {
      throw new RuntimeException(errorMessage, e);
    }
  }
}
