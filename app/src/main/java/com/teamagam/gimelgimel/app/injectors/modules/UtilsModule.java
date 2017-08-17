package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;
import com.teamagam.gimelgimel.app.common.utils.Environment;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.app.icons.GlideIconProvider;
import com.teamagam.gimelgimel.app.icons.IconProvider;
import com.teamagam.gimelgimel.data.layers.LayersLocalCacheData;
import com.teamagam.gimelgimel.data.notifications.cellular_network.CellularNetworkTypeDataRepository;
import com.teamagam.gimelgimel.data.rasters.IntermediateRastersLocalStorageData;
import com.teamagam.gimelgimel.data.timeplay.DataGeoSnapshoter;
import com.teamagam.gimelgimel.data.timeplay.DataGeoTimespanCalculator;
import com.teamagam.gimelgimel.domain.icons.repository.IconsRepository;
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
  GeoSnapshoter provideGeoSnapshoter(DataGeoSnapshoter dataGeoSnapshoter) {
    return dataGeoSnapshoter;
  }

  @Provides
  @Singleton
  GeoTimespanCalculator provideGeoTimespanCalculator(DataGeoTimespanCalculator dataGeoTimespanCalculator) {
    return dataGeoTimespanCalculator;
  }
}
