package com.teamagam.gimelgimel.data.dynamicLayers.remote;

import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.response.entity.DynamicLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.contents.DynamicLayerData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.DynamicLayerRemoteSourceHandler;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class DataDynamicLayerRemoteSourceHandler implements DynamicLayerRemoteSourceHandler {

  private final DynamicLayersAPI mDynamicLayersAPI;
  private final GeoEntityDataMapper mGeoEntityDataMapper;
  private final UserPreferencesRepository mUserPreferencesRepository;

  @Inject
  public DataDynamicLayerRemoteSourceHandler(DynamicLayersAPI dynamicLayersAPI,
      GeoEntityDataMapper geoEntityDataMapper,
      UserPreferencesRepository userPreferencesRepository) {
    mDynamicLayersAPI = dynamicLayersAPI;
    mGeoEntityDataMapper = geoEntityDataMapper;
    mUserPreferencesRepository = userPreferencesRepository;
  }

  @Override
  public void createDynamicLayer(String name) {
    DynamicLayerResponse dlResponse = new DynamicLayerResponse();
    dlResponse.setSenderId(mUserPreferencesRepository.getString(Constants.USERNAME_PREFERENCE_KEY));
    dlResponse.setContent(new DynamicLayerData(null, name, new GeoContentData[0]));
    doApiCall(mDynamicLayersAPI.createLayer(dlResponse),
        "Couldn't create new dynamic layer with name = " + name);
  }

  @Override
  public void addEntity(DynamicLayer dynamicLayer, GeoEntity geoEntity) {
    doApiCall(mDynamicLayersAPI.addEntity(dynamicLayer.getId(),
        mGeoEntityDataMapper.transform(geoEntity)), "Couldn't add entity to server.");
  }

  @Override
  public void removeEntity(DynamicLayer dynamicLayer, GeoEntity geoEntity) {
    doApiCall(mDynamicLayersAPI.removeEntity(dynamicLayer.getId(), geoEntity.getId()),
        "Couldn't remove entity from server.");
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
