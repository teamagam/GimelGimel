package com.teamagam.gimelgimel.app.injectors.components;

import android.content.Context;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.injectors.modules.ApiModule;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.app.injectors.modules.RepositoryModule;
import com.teamagam.gimelgimel.app.injectors.modules.UtilsModule;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.location.DisplayUsersLocationInteractor;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.SendSelfLocationsInteractor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.DisplayMyLocationOnMapInteractor;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.map.repository.ViewerCameraRepository;
import com.teamagam.gimelgimel.domain.messages.poller.StartFetchingMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.poller.StopFetchingMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesContainerStateRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
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
                UtilsModule.class
        })
public interface ApplicationComponent {
    void inject(GGApplication ggApplication);

    void inject(MainActivity mainActivity);

    //Exposed to sub-graphs.
    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    MessagesRepository messagesRepository();

    UnreadMessagesCountRepository unreadCountMessagesRepository();

    MessagesContainerStateRepository messagesContainerStateRepository();

    LocationFetcher locationFetcher();

    LocationEventFetcher locationEventFetcher();

    LocationRepository locationRepository();

    UserPreferencesRepository userPreferencesRepository();

    GeoEntitiesRepository geoEntitiesRepository();

    DisplayedEntitiesRepository displayedEntitiesRepository();

    VectorLayersRepository vectorLayersRepository();

    VectorLayersVisibilityRepository vectorLayersVisibilityRepository();

    ViewerCameraRepository viewerCameraRepository();

    StartFetchingMessagesInteractor startFetchingMessagesInteractor();

    StopFetchingMessagesInteractor stopFetchingMessagesInteractor();

    DisplayUsersLocationInteractor displayUserLocationsInteractor();

    @Named("gps")
    ConnectivityStatusRepository gpsConnectivityStatusRepository();

    @Named("data")
    ConnectivityStatusRepository dataConnectivityStatusRepository();

    DisplayMyLocationOnMapInteractor displayMyLocationOnMapInteractor();

    SendSelfLocationsInteractor sendMyLocationInteractor();

    DisplaySensorsOnMapInteractor displaySensorsOnMapInteractor();

    SensorsRepository sensorsRepository();

    SelectedSensorRepository selectedSensorRepository();
}
