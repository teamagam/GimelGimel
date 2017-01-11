package com.teamagam.gimelgimel.app.mainActivity.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;
import com.teamagam.gimelgimel.app.common.launcher.NavigationItemSelectedListener;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.DaggerMainActivityComponent;
import com.teamagam.gimelgimel.app.injectors.components.MainActivityComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ActivityModule;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.AlertsViewModel;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GoToDialogFragment;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.settings.SettingsActivity;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

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
    @BindView(R.id.activity_main_layout)
    SlidingUpPanelLayout mSlidingLayout;


    @Inject
    UserPreferencesRepository mUserPreferencesRepository;
    //app fragments
    private ViewerFragment mViewerFragment;
    private MessagesContainerFragment mMessagesContainerFragment;

    // Listeners
    private SlidingPanelListener mPanelListener;
    private DrawerStateLoggerListener mDrawerStateLoggerListener;

    //injectors
    private MainActivityComponent mMainActivityComponent;

    private MainActivityNotifications mMainMessagesNotifications;
    private AlertsViewModel mAlertsViewModel;
    private MainActivityAlerts mMainActivityAlerts;
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

        if (isSlidingPanelOpen()) {
            collapseSlidingPanel();
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
        initializeInjector();

        mMainActivityComponent.inject(this);

        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbar.inflateMenu(R.menu.main);

        initialize();

        // creating the menu of the left side
        createLeftDrawer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainMessagesNotifications.onStart();
        mMainActivityAlerts.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSlidingLayout.addPanelSlideListener(mPanelListener);
        mDrawerLayout.setDrawerListener(mDrawerStateLoggerListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mDrawerLayout.setDrawerListener(null);
        mSlidingLayout.removePanelSlideListener(mPanelListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMainMessagesNotifications.onStop();
        mMainActivityAlerts.onStop();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    private void initialize() {
        initFragments();
        initAlertsModule();
        initSlidingUpPanel();
        initBottomPanel();
        initDrawerListener();
        initMainNotifications();
    }

    private void initBottomPanel() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mBottomPanel = new MainActivityPanel(fragmentManager, this);
        mBottomPanel.init();
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

    private void initFragments() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragments inflated by xml
        mViewerFragment = (ViewerFragment) fragmentManager.findFragmentById(
                R.id.fragment_cesium_view);
    }

    private void initSlidingUpPanel() {
        mPanelListener = new SlidingPanelListener();
    }

    private void initAlertsModule() {
        mMainActivityAlerts = new MainActivityAlerts(this);
    }

    private void initMainNotifications() {
        mMainMessagesNotifications = new MainActivityNotifications(this);
    }

    private void createLeftDrawer() {
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent();
    }

    private void setupDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationItemSelectedListener(this, mDrawerLayout));
    }

    private void collapseSlidingPanel() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }


    private boolean isSlidingPanelOpen() {
        return mSlidingLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED;
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

    private class SlidingPanelListener implements SlidingUpPanelLayout.PanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            int height = calculateHeight(slideOffset);
//            mMessagesContainerFragment.onHeightChanged(height);
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                        SlidingUpPanelLayout.PanelState newState) {
            sLogger.userInteraction("MessageApp fragment panel mode changed from "
                    + previousState + " to " + newState);
        }

        private int calculateHeight(final float slideOffset) {
            int layoutHeight = mSlidingLayout.getHeight();
            int panelHeight = mSlidingLayout.getPanelHeight();

            return (int) ((layoutHeight - panelHeight) * slideOffset);
        }
    }
}