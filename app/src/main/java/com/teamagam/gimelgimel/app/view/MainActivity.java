package com.teamagam.gimelgimel.app.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
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
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.receivers.GpsStatusBroadcastReceiver;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.network.receivers.ConnectivityStatusReceiver;
import com.teamagam.gimelgimel.app.network.services.GGMessageLongPollingService;
import com.teamagam.gimelgimel.app.view.fragments.ViewerFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.GoToDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.ImageDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.ShowMessageDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.TurnOnGpsDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.BaseViewerFooterFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.MapManipulationFooterFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.VectorManipulationFooterFragment;
import com.teamagam.gimelgimel.app.view.settings.SettingsActivity;
import com.teamagam.gimelgimel.app.view.viewer.GGMap;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<GGApplication>
        implements ShowMessageDialogFragment.ShowMessageDialogFragmentInterface,
        GoToDialogFragment.GoToDialogFragmentInterface,
        BaseViewerFooterFragment.MapManipulationInterface,
        ConnectivityStatusReceiver.NetworkAvailableListener {

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

    // Represents the tag of the added fragments
    private final String TAG_FRAGMENT_TURN_ON_GPS_DIALOG = TAG + "TURN_ON_GPS";
    private final String TAG_FRAGMENT_MAP_CESIUM = TAG + "TAG_FRAGMENT_GG_CESIUM";

    //app fragments
    private ViewerFragment mViewerFragment;

    //adapters
    private MessageBroadcastReceiver mTextMessageReceiver;
    private MessageBroadcastReceiver mLatLongMessageReceiver;
    private LocationFetcher mLocationFetcher;
    private MessageBroadcastReceiver mImageMessageReceiver;
    private ConnectivityStatusReceiver mConnectivityStatusReceiver;
    private GpsStatusAlertBroadcastReceiver mGpsStatusAlertBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbar.inflateMenu(R.menu.main);

        initialize(savedInstanceState);

        // creating the menu of the left side
        createLeftDrawer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        GGMessageLongPollingService.startMessageLongPollingAsync(this);
        // Register to receive messages.
        // We are registering an observer
        MessageBroadcastReceiver.registerReceiver(this, mTextMessageReceiver);
        MessageBroadcastReceiver.registerReceiver(this, mLatLongMessageReceiver);
        MessageBroadcastReceiver.registerReceiver(this, mImageMessageReceiver);

        IntentFilter intentFilter = new IntentFilter(ConnectivityStatusReceiver.INTENT_NAME);

        LocalBroadcastManager.getInstance(this).registerReceiver(mConnectivityStatusReceiver,
                intentFilter);

        intentFilter = new IntentFilter(GpsStatusBroadcastReceiver.BROADCAST_NEW_GPS_STATUS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mGpsStatusAlertBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageBroadcastReceiver.unregisterReceiver(this, mTextMessageReceiver);
        MessageBroadcastReceiver.unregisterReceiver(this, mLatLongMessageReceiver);
        MessageBroadcastReceiver.unregisterReceiver(this, mImageMessageReceiver);

        GGMessageLongPollingService.stopMessagePollingAsync(this);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mConnectivityStatusReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGpsStatusAlertBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
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
    public void drawPin(PointGeometry pointGeometry) {
        mViewerFragment.drawPin(pointGeometry);
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
        // Handling dynamic fragments section.
        // If this is the first time the Activity is created (and it's not a restart of it)
        // Else, it's a restart, just fetch the already existing fragments
        if (savedInstanceState == null) {
            mViewerFragment = new ViewerFragment();
        } else {
            FragmentManager fragmentManager = getFragmentManager();

            mViewerFragment = (ViewerFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_MAP_CESIUM);
        }

        // Don't add the fragment again, if it's already added
        if (!mViewerFragment.isAdded()) {
            //Set main content viewer fragment
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mViewerFragment, TAG_FRAGMENT_MAP_CESIUM)
                    .commit();
        }
    }

    private void initBroadcastReceivers() {
        //create broadcast receiver
        MessageBroadcastReceiver.NewMessageHandler messageHandler = new MessageBroadcastReceiver.NewMessageHandler() {
            @Override
            public void onNewMessage(final Message msg) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowMessageDialogFragment.showNewMessages(getFragmentManager(), msg);
                    }
                });
            }
        };
        mTextMessageReceiver = new MessageBroadcastReceiver(messageHandler, Message.TEXT);
        mLatLongMessageReceiver = new MessageBroadcastReceiver(messageHandler, Message.LAT_LONG);
        mImageMessageReceiver = new MessageBroadcastReceiver(
                new MessageBroadcastReceiver.NewMessageHandler() {
                    @Override
                    public void onNewMessage(final Message msg) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageDialogFragment imageDF = new ImageDialogFragment();
                                imageDF.setImageMessage((MessageImage) msg);
                                imageDF.show(getFragmentManager(), "ImageDialogFragment");
                            }
                        });
                    }
                }, Message.IMAGE);

        mConnectivityStatusReceiver = new ConnectivityStatusReceiver(this);
        mGpsStatusAlertBroadcastReceiver = new GpsStatusAlertBroadcastReceiver();
    }

    private void createLeftDrawer() {

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent();
    }

    private void setupDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener());
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

    private class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            createFragmentByMenuItem(item);
            item.setChecked(true);
            mDrawerLayout.closeDrawers();
            return true;
        }

        private void createFragmentByMenuItem(MenuItem item) {
            // Currently we use the footer the show views from the Drawer
            // We should change this to more flexible code to support other views
            Fragment fragmentToDisplay = getFragmentManager().findFragmentById(R.id.container_footer);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            switch(item.getItemId()) {
                case R.id.nav_home:
                    removeFragment(fragmentTransaction, fragmentToDisplay);
                    fragmentToDisplay = null;
                    break;
                case R.id.nav_vector:
                    fragmentToDisplay = new VectorManipulationFooterFragment();
                    break;
                case R.id.nav_map:
                    fragmentToDisplay = new MapManipulationFooterFragment();
                    break;
            }

            if(fragmentToDisplay != null) {
                displayFragment(fragmentTransaction, fragmentToDisplay);
            }
        }

        private void removeFragment(FragmentTransaction fragmentTransaction, Fragment fragmentToRemove) {
            fragmentTransaction.remove(fragmentToRemove);
            fragmentTransaction.commit();
        }

        private void displayFragment(FragmentTransaction fragmentTransaction,
                                     @Nullable Fragment fragmentToDisplay) {
            if (fragmentToDisplay == null) {
                fragmentTransaction.add(R.id.container_footer, fragmentToDisplay);
            } else {
                fragmentTransaction.replace(R.id.container_footer, fragmentToDisplay);
            }
            fragmentTransaction.commit();
        }
    }

    private class GpsStatusAlertBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isGpsOn = intent.getBooleanExtra(GpsStatusBroadcastReceiver.GPS_STATUS_EXTRA, true);

            if (isGpsOn) {
                MainActivity.this.onGpsStarted();
            } else {
                MainActivity.this.onGpsStopped();
            }
        }
    }
}