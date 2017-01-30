package com.teamagam.gimelgimel.app.mainActivity.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.DaggerMainActivityComponent;
import com.teamagam.gimelgimel.app.injectors.components.MainActivityComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ActivityModule;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GoToDialogFragment;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.settings.SettingsActivity;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SetVectorLayerVisibilityInteractorFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<GGApplication>
        implements
        GoToDialogFragment.GoToDialogFragmentInterface {

    private static final AppLogger sLogger = AppLoggerFactory.create(MainActivity.class);

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_activity_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @Inject
    UserPreferencesRepository mUserPreferencesRepository;
    @Inject
    DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
    @Inject
    SetVectorLayerVisibilityInteractorFactory mSetVectorLayerVisibilityInteractorFactory;

    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;

    //app fragments
    private ViewerFragment mViewerFragment;
    // Listeners
    private DrawerStateLoggerListener mDrawerStateLoggerListener;
    //injectors
    private MainActivityComponent mMainActivityComponent;
    private MainActivityPanel mBottomPanel;

    private Map<String, Integer> mStringIdToIntIdMap;
    private Map<Integer, String> mIntIdToStringIdMap;
    private int mIdCounter;

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        // Stub for future use
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ||
                newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    public void goToLocation(PointGeometryApp pointGeometry) {
        mViewerFragment.lookAt(pointGeometry);
    }

    public MainActivityComponent getMainActivityComponent() {
        return mMainActivityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mStringIdToIntIdMap = new HashMap<>();
        mIntIdToStringIdMap = new HashMap<>();
        mIdCounter = 0;

        initializeInjector();

        mMainActivityComponent.inject(this);

        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbar.inflateMenu(R.menu.main);

        initialize();

        createLeftDrawer();

        handleGpsEnabledState();
    }

    private void handleGpsEnabledState() {
        if (!isGpsProviderEnabled()) {
            Navigator.navigateToTurnOnGPSDialog(this);
        }
    }

    public boolean isGpsProviderEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mDisplayVectorLayersInteractor.execute();
        mDrawerLayout.addDrawerListener(mDrawerStateLoggerListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDisplayVectorLayersInteractor.unsubscribe();
        mDrawerLayout.removeDrawerListener(mDrawerStateLoggerListener);
    }


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    private void initialize() {
        initViewer();
        initAlertsModule();
        initBottomPanel();
        initDrawerListener();
        initMainNotifications();
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

    private void initDrawerListener() {
        mDrawerStateLoggerListener = new DrawerStateLoggerListener();
    }

    private void initViewer() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragments inflated by xml
        mViewerFragment = (ViewerFragment) fragmentManager.findFragmentById(
                R.id.fragment_cesium_view);
    }

    private void initAlertsModule() {
        MainActivityAlerts mMainActivityAlerts = new MainActivityAlerts(this);
        attachSubcomponent(mMainActivityAlerts);
    }

    private void initMainNotifications() {
        MainActivityNotifications mMainMessagesNotifications = new MainActivityNotifications(this);
        attachSubcomponent(mMainMessagesNotifications);
    }

    private void createLeftDrawer() {
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent();
    }

    private void setupDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(
                new DrawerOnNavigationItemSelectedListener());
        mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(
                new DrawerVectorLayersDisplayer());
    }

    private class DrawerStateLoggerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            sLogger.userInteraction("Drawer opened");

            TextView navHeaderText = (TextView) drawerView.findViewById(R.id.nav_header_text);
            String username = mUserPreferencesRepository.getPreference(
                    getResources().getString(R.string.user_name_text_key));
            navHeaderText.setText(username);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            sLogger.userInteraction("Drawer closed");
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }

    private class DrawerVectorLayersDisplayer implements DisplayVectorLayersInteractor.Displayer {
        @Override
        public void displayShown(VectorLayerPresentation vectorLayerPresentation) {
            display(vectorLayerPresentation, true);
        }

        @Override
        public void displayHidden(VectorLayerPresentation vectorLayerPresentation) {
            display(vectorLayerPresentation, false);
        }

        private void display(VectorLayerPresentation vectorLayerPresentation, boolean isVisible) {
            if (null == mStringIdToIntIdMap.get(vectorLayerPresentation.getId())) {
                mStringIdToIntIdMap.put(vectorLayerPresentation.getId(), ++mIdCounter);
                mIntIdToStringIdMap.put(mIdCounter, vectorLayerPresentation.getId());
                mNavigationView.getMenu()
                        .add(R.id.drawer_menu_layers, mIdCounter, 0, vectorLayerPresentation.getName());
            }
            mNavigationView.getMenu()
                    .findItem(mStringIdToIntIdMap.get(vectorLayerPresentation.getId())).setChecked(isVisible);
        }
    }

    private class DrawerOnNavigationItemSelectedListener
            implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            sLogger.userInteraction("Drawer item " + item + " clicked");
            mSetVectorLayerVisibilityInteractorFactory.create(
                    mIntIdToStringIdMap.get(item.getItemId()), !item.isChecked()).execute();
            return true;
        }
    }
}