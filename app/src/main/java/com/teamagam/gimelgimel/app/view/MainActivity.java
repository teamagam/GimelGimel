package com.teamagam.gimelgimel.app.view;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.receivers.GpsStatusBroadcastReceiver;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.injectors.components.DaggerMainActivityComponent;
import com.teamagam.gimelgimel.app.injectors.components.MainActivityComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ActivityModule;
import com.teamagam.gimelgimel.app.injectors.modules.MapModule;
import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.map.view.GGMap;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.network.receivers.ConnectivityStatusReceiver;
import com.teamagam.gimelgimel.app.network.receivers.NetworkChangeReceiver;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.app.notifications.view.MainActivityNotifications;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.GoToDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.TurnOnGpsDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments.MessagesDetailBaseGeoFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.BaseViewerFooterFragment;
import com.teamagam.gimelgimel.app.view.listeners.NavigationItemSelectedListener;
import com.teamagam.gimelgimel.app.view.settings.SettingsActivity;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<GGApplication>
        implements
        GoToDialogFragment.GoToDialogFragmentInterface,
        BaseViewerFooterFragment.MapManipulationInterface,
        ConnectivityStatusReceiver.NetworkAvailableListener,
        MessagesDetailBaseGeoFragment.GeoMessageInterface,
        GGMessageSender.MessageStatusListener {

    private static final Logger sLogger = LoggerFactory.create(MainActivity.class);


    @BindView(R.id.no_gps_signal_text_view)
    TextView mNoGpsTextView;

    @BindView(R.id.no_network_text_view)
    TextView mNoNetworkTextView;

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_activity_drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @BindView(R.id.activity_main_layout)
    SlidingUpPanelLayout mSlidingLayout;

    // Represents the tag of the added fragments
    private final String TAG_FRAGMENT_TURN_ON_GPS_DIALOG = TAG + "TURN_ON_GPS";

    //app fragments
    private ViewerFragment mViewerFragment;
    private MessagesContainerFragment mMessagesContainerFragment;

    //com.teamagam.gimelgimel.data.message.adapters
    private LocationFetcher mLocationFetcher;
    private ConnectivityStatusReceiver mConnectivityStatusReceiver;
    private GpsStatusAlertBroadcastReceiver mGpsStatusAlertBroadcastReceiver;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private GGMessageSender mGGMessageSender;

    // Listeners
    private SlidingPanelListener mPanelListener;
    private DrawerStateLoggerListener mDrawerStateLoggerListener;

    //injectors
    private MainActivityComponent mMainActivityComponent;

    MainActivityNotifications mMainMessagesNotifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeInjector();

        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbar.inflateMenu(R.menu.main);

        initialize(savedInstanceState);

        // creating the menu of the left side
        createLeftDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSlidingLayout.addPanelSlideListener(mPanelListener);
        mDrawerLayout.setDrawerListener(mDrawerStateLoggerListener);

        registerReceivers();
        registerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceivers();
        unregisterListeners();

        mDrawerLayout.setDrawerListener(null);
        mSlidingLayout.removePanelSlideListener(mPanelListener);
    }

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

//                mViewerFragment.clearSentLocationsLayer();
//                mViewerFragment.clearReceivedLocationsLayer();
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
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void goToLocation(PointGeometry pointGeometry) {
        mViewerFragment.goToLocation(pointGeometry);
    }

    @Override
    public void addMessageLocationPin(Message message) {
//        mViewerFragment.addMessageLocationPin(message);
    }

    @Override
    public GGMap getGGMap() {
        return mViewerFragment.getGGMap();
    }

    @Override
    public void onNetworkAvailableChange(boolean isNetworkAvailable) {
        sLogger.v("Network status: " + isNetworkAvailable);

        int visibility = isNetworkAvailable ? View.GONE : View.VISIBLE;
        mNoNetworkTextView.setVisibility(visibility);
        mNoNetworkTextView.bringToFront();
    }

    @Override
    public void onSuccessfulMessage(Message message) {
        View snackbarParent = mViewerFragment.getView();
        String text =
                String.format(
                        "The message of type %s, with the content: %s, has been sent",
                        message.getType(), message.getContent());

        Snackbar snackbar = createSnackbar(snackbarParent, text);

        snackbar.show();
    }

    @Override
    public void onFailureMessage(Message message) {
        View snackbarParent = mViewerFragment.getView();
        String text =
                String.format(
                        "Could not sent message of type %s, with the content: %s",
                        message.getType(), message.getContent());

        Snackbar snackbar = createSnackbar(snackbarParent, text);

        snackbar.show();
    }

    public void onGpsStopped() {
        sLogger.v("Gps status: stopped");

        setDisplayNoGpsView(true);
    }

    public void onGpsStarted() {
        sLogger.v("Gps status: started");

        setDisplayNoGpsView(false);
    }

    private void initialize(Bundle savedInstanceState) {
        initFragments(savedInstanceState);
        initBroadcastReceivers();
        initGpsStatus();
        initSlidingUpPanel();
        initDrawerListener();
        initMessageSenders();
        initMainNotifications();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainMessagesNotifications.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mMainMessagesNotifications.onStop();
    }

    private void initMainNotifications() {
        mMainMessagesNotifications = new MainActivityNotifications(this);
    }

    private void initializeInjector() {
        ((GGApplication)getApplication()).getApplicationComponent().inject(this);

        mMainActivityComponent = DaggerMainActivityComponent.builder()
                .applicationComponent(((GGApplication) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .messageModule(new MessageModule())
                .mapModule(new MapModule())
                .build();
    }

    private void initDrawerListener() {
        mDrawerStateLoggerListener = new DrawerStateLoggerListener();
    }

    private void initGpsStatus() {
        mLocationFetcher = LocationFetcher.getInstance(this);

        if (!mLocationFetcher.isGpsProviderEnabled()) {
            setDisplayNoGpsView(true);

            TurnOnGpsDialogFragment dialogFragment = new TurnOnGpsDialogFragment();
            dialogFragment.show(getFragmentManager(), TAG_FRAGMENT_TURN_ON_GPS_DIALOG);
        }
    }

    private void initFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getFragmentManager();
        //fragments inflated by xml
        mViewerFragment = (ViewerFragment) fragmentManager.findFragmentById(
                R.id.fragment_cesium_view);
        mMessagesContainerFragment =
                (MessagesContainerFragment) fragmentManager.findFragmentById(
                        R.id.fragment_messages_container);
    }

    private void initBroadcastReceivers() {
        mConnectivityStatusReceiver = new ConnectivityStatusReceiver(this);
        mGpsStatusAlertBroadcastReceiver = new GpsStatusAlertBroadcastReceiver();
        mNetworkChangeReceiver = new NetworkChangeReceiver();
    }

    private void initSlidingUpPanel() {
        mPanelListener = new SlidingPanelListener();
    }

    private void initMessageSenders() {
        mGGMessageSender = mApp.getMessageSender();
    }

    private void createLeftDrawer() {
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent();
    }

    private Snackbar createSnackbar(View parent, String text) {
        Snackbar snackbar = Snackbar.make(parent, text, Snackbar.LENGTH_LONG);

        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        return snackbar;
    }

    private void setupDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationItemSelectedListener(this, mDrawerLayout));
    }

    private void registerReceivers() {
        IntentFilter connectivityStatusFilter = new IntentFilter(
                ConnectivityStatusReceiver.INTENT_NAME);
        LocalBroadcastManager.getInstance(this).registerReceiver(mConnectivityStatusReceiver,
                connectivityStatusFilter);

        IntentFilter newGpsFilter = new IntentFilter(
                GpsStatusBroadcastReceiver.BROADCAST_NEW_GPS_STATUS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mGpsStatusAlertBroadcastReceiver,
                newGpsFilter);

        IntentFilter connectivityChangedIntentFilter = new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(mNetworkChangeReceiver, connectivityChangedIntentFilter, null,
                mApp.getSharedBackgroundHandler());
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mConnectivityStatusReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mGpsStatusAlertBroadcastReceiver);
        unregisterReceiver(mNetworkChangeReceiver);
    }

    private void registerListeners() {
        mGGMessageSender.addListener(this);
    }

    private void unregisterListeners() {
        mGGMessageSender.removeListener(this);
    }

    private void collapseSlidingPanel() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private boolean isSlidingPanelOpen() {
        return mSlidingLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED;
    }

    /**
     * Sets the visibility of the "no gps" alert textview
     *
     * @param displayState - true will make the view visible, false will be it gone
     */
    private void setDisplayNoGpsView(boolean displayState) {
        int visibility = displayState ? View.VISIBLE : View.GONE;
        mNoGpsTextView.setVisibility(visibility);
        mNoGpsTextView.bringToFront();
    }

    public MainActivityComponent getMainActivityComponent() {
        return mMainActivityComponent;
    }

    private class DrawerStateLoggerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            sLogger.userInteraction("Drawer opened");

            TextView navHeaderText = (TextView) drawerView.findViewById(R.id.nav_header_text);
            navHeaderText.setText(mApp.getPrefs().getString(R.string.user_name_text_key));
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            sLogger.userInteraction("Drawer closed");
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }

    private class GpsStatusAlertBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isGpsOn = intent.getBooleanExtra(GpsStatusBroadcastReceiver.GPS_STATUS_EXTRA,
                    true);

            if (isGpsOn) {
                MainActivity.this.onGpsStarted();
            } else {
                MainActivity.this.onGpsStopped();
            }
        }
    }

    private class SlidingPanelListener implements SlidingUpPanelLayout.PanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            int height = calculateHeight(slideOffset);
            mMessagesContainerFragment.onHeightChanged(height);
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                        SlidingUpPanelLayout.PanelState newState) {
            sLogger.userInteraction("Message fragment panel mode changed from "
                    + previousState + " to " + newState);
        }

        private int calculateHeight(final float slideOffset) {
            int layoutHeight = mSlidingLayout.getHeight();
            int panelHeight = mSlidingLayout.getPanelHeight();

            return (int) ((layoutHeight - panelHeight) * slideOffset);
        }
    }
}