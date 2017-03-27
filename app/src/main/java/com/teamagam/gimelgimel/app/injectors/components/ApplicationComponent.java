package com.teamagam.gimelgimel.app.injectors.components;

import android.content.Context;
import android.location.LocationListener;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.app.injectors.modules.ApiModule;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.injectors.modules.RepositoryModule;
import com.teamagam.gimelgimel.app.injectors.modules.UtilsModule;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.map.esri.EsriGGMapView;
import com.teamagam.gimelgimel.app.map.view.SendQuadrilateralActionFragment;
import com.teamagam.gimelgimel.app.message.view.ImageFullscreenActivity;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.layers.LoadAllCachedLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.location.DisplayUsersLocationInteractor;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.SendSelfLocationsInteractor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import com.teamagam.gimelgimel.domain.messages.poller.StartFetchingMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.poller.StopFetchingMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.NewMessageIndicationRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.notifications.cellular_network.Update3GConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.rasters.LoadIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRastersRepository;
import com.teamagam.gimelgimel.domain.sensors.DisplaySensorsOnMapInteractor;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(
        modules = {
                ApplicationModule.class,
                RepositoryModule.class,
                ApiModule.class,
                UtilsModule.class,
                MessageModule.class
        })
public interface ApplicationComponent {
    void inject(GGApplication ggApplication);

    void inject(MainActivity mainActivity);

    void inject(ImageFullscreenActivity fullscreenActivity);

    void inject(EsriGGMapView esriGGMapView);

    void inject(SendQuadrilateralActionFragment sendQuadrilateralActionFragment);

    //Exposed to sub-graphs.
    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    MessagesRepository messagesRepository();

    EntityMessageMapper entityMessageMapper();

    UnreadMessagesCountRepository unreadCountMessagesRepository();

    LocationFetcher locationFetcher();

    LocationListener locationListener();

    LocationEventFetcher locationEventFetcher();

    LocationRepository locationRepository();

    UserPreferencesRepository userPreferencesRepository();

    GeoEntitiesRepository geoEntitiesRepository();

    DisplayedEntitiesRepository displayedEntitiesRepository();

    SingleDisplayedItemRepository<KmlEntityInfo> currentKmlEntityRepository();

    VectorLayersRepository vectorLayersRepository();

    VectorLayersVisibilityRepository vectorLayersVisibilityRepository();

    StartFetchingMessagesInteractor startFetchingMessagesInteractor();

    StopFetchingMessagesInteractor stopFetchingMessagesInteractor();

    DisplayUsersLocationInteractor displayUserLocationsInteractor();

    @Named("gps")
    ConnectivityStatusRepository gpsConnectivityStatusRepository();

    @Named("data")
    ConnectivityStatusRepository dataConnectivityStatusRepository();

    @Named("3g")
    ConnectivityStatusRepository threeGConnectivityStatusRepository();

    SendSelfLocationsInteractor sendMyLocationInteractor();

    DisplaySensorsOnMapInteractor displaySensorsOnMapInteractor();

    Update3GConnectivityStatusInteractor update3GConnectivityStatusInteractor();

    LoadAllCachedLayersInteractor loadAllCachedLayersInteractor();

    LoadIntermediateRastersInteractor loadIntermediateRastersInteractor();

    SensorsRepository sensorsRepository();

    SelectedSensorRepository selectedSensorRepository();

    LayersLocalCache layersLocalCache();

    GlideLoader glideFactory();

    AlertsRepository alertsRepository();

    InformedAlertsRepository informedAlertsRepository();

    SelectedEntityRepository selectedEntityRepository();

    IntermediateRastersRepository intermediateRastersRepository();

    IntermediateRasterVisibilityRepository intermediateRasterVisibilityRepository();

    MessageNotifications messageNotifications();

    NewMessageIndicationRepository newMessageIndicationRepository();
}