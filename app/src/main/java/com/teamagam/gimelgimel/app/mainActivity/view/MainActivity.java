package com.teamagam.gimelgimel.app.mainActivity.view;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.teamagam.gimelgimel.app.settings.SettingsActivity;
import com.teamagam.gimelgimel.app.settings.dialogs.SetUsernameAlertDialogBuilder;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity<GGApplication> {

    private static final AppLogger sLogger = AppLoggerFactory.create(MainActivity.class);

    @BindView(R.id.main_activity_drawer_layout)
    DrawerLayout mDrawerLayout;

    @Inject
    UserPreferencesRepository mUserPreferencesRepository;

    @Inject
    Navigator mNavigator;

    //app fragments
    private ViewerFragment mViewerFragment;
    //injectors
    private MainActivityComponent mMainActivityComponent;

    private MainActivityPanel mBottomPanel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                sLogger.userInteraction("Settings menu option item clicked");
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_clear_map:
                sLogger.userInteraction("Clear map menu option item clicked");

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeInjector();

        mMainActivityComponent.inject(this);

        super.onCreate(savedInstanceState);

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
        mMainActivityComponent = DaggerMainActivityComponent.builder()
                .applicationComponent(((GGApplication) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();

        mMainActivityComponent.inject(this);
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
            mNavigator.navigateToTurnOnGPSDialog();
        }
    }

    private void askForUsernameOnFirstTime() {
        if (!isUsernameSet()) {
            new SetUsernameAlertDialogBuilder(this).create().show();
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
        String defVal = getString(R.string.username_default);
        return mUserPreferencesRepository.getString(key).equals(defVal);
    }
}