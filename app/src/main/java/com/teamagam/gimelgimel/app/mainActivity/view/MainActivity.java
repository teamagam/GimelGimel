package com.teamagam.gimelgimel.app.mainActivity.view;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.Alerts.view.AlertsSubcomponent;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.DaggerMainActivityComponent;
import com.teamagam.gimelgimel.app.injectors.components.MainActivityComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ActivityModule;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.settings.dialogs.SetUsernameAlertDialogBuilder;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<GGApplication> {

    private static final AppLogger sLogger = AppLoggerFactory.create(MainActivity.class);

    @BindView(R.id.main_activity_drawer_layout)
    DrawerLayout mDrawerLayout;

    @Inject
    UserPreferencesRepository mUserPreferencesRepository;
    //app fragments
    private ViewerFragment mViewerFragment;
    //injectors
    private MainActivityComponent mMainActivityComponent;

    private MainActivityPanel mBottomPanel;

    @Override
    public void onBackPressed() {
        sLogger.userInteraction("Back key pressed");

        if (mBottomPanel.isSlidingPanelOpen()) {
            mBottomPanel.collapseSlidingPanel();
        } else {

            // "Minimizes" application without forcing the activity to be destroyed
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ViewerFragment getViewerFragment() {
        return mViewerFragment;
    }

    public MainActivityComponent getMainActivityComponent() {
        return mMainActivityComponent;
    }

    public boolean isGpsProviderEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isSlidingPanelOpen() {
        return mBottomPanel.isSlidingPanelOpen();
    }

    public void setOnPanelOpenedListener(MainActivityPanel.OnPanelOpenListener listener) {
        mBottomPanel.setOnPanelOpenListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeInjector();

        mMainActivityComponent.inject(this);

        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        initialize();

        handleGpsEnabledState();

        askForUsernameOnFirstTime();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    private void initialize() {
        initViewer();
        initDrawer();
        initConnectivityAlertsModule();
        initBottomPanel();
        initMainNotifications();
        initBubbleAlerts();
    }

    private void initDrawer() {
        attachSubcomponent(new MainActivityDrawer(this));
    }

    private void initBottomPanel() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mBottomPanel = new MainActivityPanel(fragmentManager, this);
        attachSubcomponent(mBottomPanel);
    }

    private void initializeInjector() {
        getApplicationComponent().inject(this);

        mMainActivityComponent = DaggerMainActivityComponent.builder()
                .applicationComponent(((GGApplication) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    private void initViewer() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragments inflated by xml
        mViewerFragment = (ViewerFragment) fragmentManager.findFragmentById(
                R.id.viewer_layout);
    }

    private void initConnectivityAlertsModule() {
        MainActivityConnectivityAlerts mainActivityAlerts =
                new MainActivityConnectivityAlerts(this);
        attachSubcomponent(mainActivityAlerts);
    }

    private void initMainNotifications() {
        attachSubcomponent(new MainActivityNotifications(this));
    }

    private void initBubbleAlerts() {
        AlertsSubcomponent alertsSubcomponent = new AlertsSubcomponent(this);
        attachSubcomponent(alertsSubcomponent);
    }

    private void handleGpsEnabledState() {
        if (!isGpsProviderEnabled()) {
            Navigator.navigateToTurnOnGPSDialog(this);
        }
    }

    private void askForUsernameOnFirstTime() {
        if (!isUsernameSet()) {
            SetUsernameAlertDialogBuilder builder = new SetUsernameAlertDialogBuilder(this);
            builder.setOnFinishCallback(() -> {
                mApp.startSendingLocation();
            });

            builder.create().show();
        }
    }

    private boolean isUsernameSet() {
        String key = getString(R.string.user_name_text_key);
        if (mUserPreferencesRepository.contains(key)) {
            return !isUserNameSetToDefault();
        } else {
            return false;
        }
    }

    private boolean isUserNameSetToDefault() {
        String key = getString(R.string.user_name_text_key);
        String defVal = getString(R.string.pref_default_display_name);
        return mUserPreferencesRepository.getString(key).equals(defVal);
    }
}