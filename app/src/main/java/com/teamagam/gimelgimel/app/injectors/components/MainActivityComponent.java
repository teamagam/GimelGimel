package com.teamagam.gimelgimel.app.injectors.components;

import com.teamagam.gimelgimel.app.Alerts.view.BubbleAlertsSubcomponent;
import com.teamagam.gimelgimel.app.injectors.modules.ActivityModule;
import com.teamagam.gimelgimel.app.injectors.modules.MapModule;
import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityConnectivityAlerts;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityNotifications;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityPanel;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.message.view.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.message.view.SendMessagesFragment;
import com.teamagam.gimelgimel.app.sensor.view.SensorDetailFragment;
import com.teamagam.gimelgimel.app.sensor.view.SensorsMasterFragment;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects map specific Fragment ({@link ViewerFragment}).
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                MapModule.class,
                MessageModule.class
        }
)
public interface MainActivityComponent extends ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(ViewerFragment viewerFragment);

    void inject(SendGeographicMessageDialog sendGeoMessage);

    void inject(MainActivityNotifications mainMessagesNotifications);

    void inject(MainActivityConnectivityAlerts mainActivityConnectivityAlerts);

    void inject(MessagesContainerFragment fragment);

    void inject(SendMessagesFragment sendMessagesFragment);

    void inject(MainActivityPanel panel);

    void inject(SensorsMasterFragment fragment);

    void inject(SensorDetailFragment sensorDetailFragment);

    void inject(BubbleAlertsSubcomponent bubbleAlertsSubcomponent);
}

