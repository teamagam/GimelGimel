package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;
import com.teamagam.gimelgimel.app.common.utils.Environment;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.app.icons.GlideIconProvider;
import com.teamagam.gimelgimel.app.icons.IconProvider;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawActionProvider;
import com.teamagam.gimelgimel.app.map.actions.measure.MeasureActionProvider;
import com.teamagam.gimelgimel.app.map.actions.send.geometry.SendGeometryActionProvider;
import com.teamagam.gimelgimel.app.map.actions.send.quadrilateral.SendQuadrilateralActionProvider;
import com.teamagam.gimelgimel.app.map.actions.timeplay.TimeplayActionProvider;
import com.teamagam.gimelgimel.data.layers.LayersLocalCacheData;
import com.teamagam.gimelgimel.data.notifications.cellular_network.CellularNetworkTypeDataRepository;
import com.teamagam.gimelgimel.data.rasters.IntermediateRastersLocalStorageData;
import com.teamagam.gimelgimel.data.timeplay.AggregationGeoSnapshoter;
import com.teamagam.gimelgimel.data.timeplay.AggregationTimespanCalculator;
import com.teamagam.gimelgimel.data.timeplay.dynamic_layers.DynamicLayersSnapshoter;
import com.teamagam.gimelgimel.data.timeplay.dynamic_layers.DynamicLayersTimespanCalculator;
import com.teamagam.gimelgimel.data.timeplay.messages.GeoMessagesSnapshoter;
import com.teamagam.gimelgimel.data.timeplay.messages.GeoMessagesTimespanCalculator;
import com.teamagam.gimelgimel.data.timeplay.users.UserGeoSnapshoter;
import com.teamagam.gimelgimel.data.timeplay.users.UsersGeoTimespanCalculator;
import com.teamagam.gimelgimel.domain.dynamicLayers.details.DynamicLayerToEntityMapper;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.notifications.cellular_network.CellularNetworkTypeRepository;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRastersLocalStorage;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import com.teamagam.gimelgimel.domain.timeplay.GeoTimespanCalculator;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import com.teamagam.gimelgimel.domain.utils.ApplicationStatus;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import javax.inject.Singleton;

@Module
public class UtilsModule {

  @Provides
  @Singleton
  LayersLocalCache provideLayersLocalCache(LayersLocalCacheData layersLocalCacheData) {
    return layersLocalCacheData;
  }

  @Provides
  @Singleton
  IntermediateRastersLocalStorage provideIntermediateRasterLocalStorage(
      IntermediateRastersLocalStorageData intermediateRastersLocalStorageData) {
    return intermediateRastersLocalStorageData;
  }

  @Provides
  @Singleton
  GlideLoader provideGlideFactory(Context context) {
    return new GlideLoader(context);
  }

  @Provides
  @Singleton
  CellularNetworkTypeRepository provideCellularNetworkTypeNotifier(CellularNetworkTypeDataRepository dataCellularNetworkTypeNotifier) {
    return dataCellularNetworkTypeNotifier;
  }

  @Provides
  @Singleton
  PreferencesUtils provideMessagesUtil(UserPreferencesRepository userPreferencesRepository) {
    return new PreferencesUtils(userPreferencesRepository);
  }

  @Provides
  @Singleton
  ApplicationStatus provideEnvironment(Context context) {
    return new Environment(context);
  }

  @Provides
  @Singleton
  IconProvider provideIconProvider(GlideIconProvider glideIconProvider) {
    return glideIconProvider;
  }

  @Provides
  @Singleton
  GeoSnapshoter provideGeoSnapshoter(GeoMessagesSnapshoter geoMessagesSnapshoter,
      UserGeoSnapshoter userGeoSnapshoter,
      DynamicLayersSnapshoter dynamicLayersSnapshoter) {
    return new AggregationGeoSnapshoter(geoMessagesSnapshoter, userGeoSnapshoter,
        dynamicLayersSnapshoter);
  }

  @Provides
  @Singleton
  GeoTimespanCalculator provideGeoTimespanCalculator(GeoMessagesTimespanCalculator geoMessagesTimespanCalculator,
      UsersGeoTimespanCalculator usersGeoTimespanCalculator,
      DynamicLayersTimespanCalculator dynamicLayersTimespanCalculator) {
    return new AggregationTimespanCalculator(usersGeoTimespanCalculator,
        geoMessagesTimespanCalculator, dynamicLayersTimespanCalculator);
  }

  @Provides
  @Singleton
  DynamicLayerToEntityMapper provideDynamicLayerToEntityMapper() {
    return new DynamicLayerToEntityMapper();
  }

  @Provides
  Iterable<ActionProvider> provideActionProviders() {
    return Arrays.asList(new SendQuadrilateralActionProvider(), new SendGeometryActionProvider(),
        new MeasureActionProvider(), new FreeDrawActionProvider(), new TimeplayActionProvider());
  }
}
