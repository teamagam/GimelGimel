package com.teamagam.gimelgimel.app.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.ViewsModels.DrawerListItem;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.network.receivers.ConnectivityStatusReceiver;
import com.teamagam.gimelgimel.app.network.services.GGMessageLongPollingService;
import com.teamagam.gimelgimel.app.view.adapters.DrawerListAdapter;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<GGApplication>
        implements ViewerFragment.OnFragmentInteractionListener,
        ShowMessageDialogFragment.ShowMessageDialogFragmentInterface,
        GoToDialogFragment.GoToDialogFragmentInterface,
        BaseViewerFooterFragment.MapManipulationInterface,
        LocationFetcher.GpsStatusListener, ConnectivityStatusReceiver.NetworkAvailableListener {

    private static final Logger sLogger = LoggerFactory.create(MainActivity.class);

    // Represents the tag of the added fragments
    private final String TAG_FRAGMENT_TURN_ON_GPS_DIALOG = TAG + "TURN_ON_GPS";
    private final String TAG_FRAGMENT_MAP_CESIUM = TAG + "TAG_FRAGMENT_GG_CESIUM";

    //drawer parameters
    private ActionBarDrawerToggle mDrawerToggle;
    //layouts
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    //titles
    private CharSequence mDrawerTitle;

    // Used to store the last screen title.
    private CharSequence mTitle;

    //app fragments
    private ViewerFragment mViewerFragment;

    //adapters
    private DrawerListAdapter mListAdapter;
    private MessageBroadcastReceiver mTextMessageReceiver;
    private MessageBroadcastReceiver mLatLongMessageReceiver;
    private LocationFetcher mLocationFetcher;
    private MessageBroadcastReceiver mImageMessageReceiver;
    private ConnectivityStatusReceiver mConnectivityStatusReceiver;

    // Gps message
    private boolean mIsWaitingForGpsAlert;

    @BindView(R.id.no_gps_signal_text_view)
    TextView mNoGpsTextView;

    @BindView(R.id.no_network_text_view)
    TextView mNoNetworkTextView;
    private CountDownTimer mGpsCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        // Handling dynamic fragments section.
        // If this is the first time the Activity is created (and it's not a restart of it)
        // Else, it's a restart, just fetch the already existing fragments
        if (savedInstanceState == null) {
            mViewerFragment = new ViewerFragment();
        } else {
            android.app.FragmentManager fragmentManager = getFragmentManager();

            mViewerFragment = (ViewerFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_MAP_CESIUM);
        }

        // creating the menu of the left side
        createLeftDrawer();

        // Don't add the fragment again, if it's already added
        if (!mViewerFragment.isAdded()) {
            //Set main content viewer fragment
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mViewerFragment, TAG_FRAGMENT_MAP_CESIUM)
                    .commit();
        }

        int noGpsDelay = getResources().getInteger(R.integer.no_gps_message_delay);

        mIsWaitingForGpsAlert = false;
        mGpsCountDownTimer = new CountDownTimer(noGpsDelay, noGpsDelay) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDisplayNoGpsView(true);
                    }
                });
            }
        };

        initBroadcastReceivers();
        mLocationFetcher = LocationFetcher.getInstance(this);

        if (!mLocationFetcher.isGpsProviderEnabled()) {
            setDisplayNoGpsView(true);

            TurnOnGpsDialogFragment dialogFragment = new TurnOnGpsDialogFragment();
            dialogFragment.show(getFragmentManager(), TAG_FRAGMENT_TURN_ON_GPS_DIALOG);
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
    }

    private void createLeftDrawer() {

        //initialization
        mDrawerTitle = getTitle();
        mTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list_view);
        // nav drawer icons from resources
        List<DrawerListItem> drawerListItems = getDrawerListItems();

        mListAdapter = new DrawerListAdapter(this, R.layout.drawer_list_item, drawerListItems);

        mDrawerList.setAdapter(mListAdapter);
        mDrawerList.setItemChecked(0, true);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        try {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
        } catch (Exception e) {
            //todo: handle exception
        }
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                null,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                sLogger.userInteraction("Drawer closed");
                try {
                    getSupportActionBar().setTitle(mTitle);
                } catch (Exception e) {
                    //todo: handle exception
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                sLogger.userInteraction("Drawer opened");
                try {
                    getSupportActionBar().setTitle(mDrawerTitle);
                } catch (Exception e) {
                    //todo: handle exception
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
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
        mLocationFetcher.setGpsStatusListener(this);

        IntentFilter intentFilter = new IntentFilter(ConnectivityStatusReceiver.INTENT_NAME);

        LocalBroadcastManager.getInstance(this).registerReceiver(mConnectivityStatusReceiver,
                intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageBroadcastReceiver.unregisterReceiver(this, mTextMessageReceiver);
        MessageBroadcastReceiver.unregisterReceiver(this, mLatLongMessageReceiver);
        MessageBroadcastReceiver.unregisterReceiver(this, mImageMessageReceiver);

        GGMessageLongPollingService.stopMessagePollingAsync(this);

        mLocationFetcher.removeGpsStatusListener();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mConnectivityStatusReceiver);
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
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                sLogger.userInteraction("Settings menu option item clicked");
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            //todo: add this menu in the future.
//            case R.id.action_friends:
//                intent = new Intent(this, FriendsActivity.class);
//                startActivity(intent);
//                break;
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
    public void onFragmentInteraction(Uri uri) {
        //todo: complete interaction with fragment.

    }

    @Override
    public void goToLocation(PointGeometry pointGeometry) {
        mViewerFragment.goToLocation(pointGeometry);
    }

    @Override
    public void drawPin(PointGeometry pointGeometry) {
        mViewerFragment.drawPin(pointGeometry);
    }


    /**
     * @return list of {@link DrawerListItem}s for drawer to present
     */
    private List<DrawerListItem> getDrawerListItems() {
        List<DrawerListItem> list = new ArrayList<>();
        list.add(new DrawerListItem(
                "Home",
                getDrawable(android.R.drawable.ic_dialog_dialer),
                R.id.container_footer,
                new Fragment()
        ));
        list.add(new DrawerListItem(
                "Vector Manipulation",
                getDrawable(android.R.drawable.ic_menu_edit),
                R.id.container_footer,
                new VectorManipulationFooterFragment()
        ));
        list.add(new DrawerListItem(
                "Map Manipulation",
                getDrawable(android.R.drawable.ic_dialog_map),
                R.id.container_footer,
                new MapManipulationFooterFragment()
        ));
        return list;
    }

    @Override
    public GGMap getGGMap() {
        return mViewerFragment.getGGMap();
    }

    @Override
    public void onGpsStopped() {
        sLogger.v("Gps status: stopped");

        if(!mIsWaitingForGpsAlert) {
            mGpsCountDownTimer.start();
            mIsWaitingForGpsAlert = true;
        }
    }

    @Override
    public void onGpsStarted() {
        sLogger.v("Gps status: started");

        mGpsCountDownTimer.cancel();
        mIsWaitingForGpsAlert = false;

        setDisplayNoGpsView(false);
    }

    @Override
    public void onNetworkAvailableChange(boolean isNetworkAvailable) {
        sLogger.v("Network status: " + isNetworkAvailable);

        int visibility = isNetworkAvailable ? View.GONE : View.VISIBLE;
        mNoNetworkTextView.setVisibility(visibility);
        mNoNetworkTextView.bringToFront();
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

    /**
     * Click listener for main activity's drawer component
     * Displays the fragment associated with item on click
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DrawerListItem drawerListItem = mListAdapter.getItem(position);
            sLogger.userInteraction("Drawer item " + drawerListItem.getName() + " clicked");

            displayItemFragment(drawerListItem);

            mDrawerLayout.closeDrawer(mDrawerList);
        }

        private void displayItemFragment(DrawerListItem drawerListItem) {
            Fragment displayedFragment = getFragmentManager().findFragmentById(
                    drawerListItem.getContainerViewResourceId());
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            if (displayedFragment == null) {
                fragmentTransaction.add(
                        drawerListItem.getContainerViewResourceId(),
                        drawerListItem.getFragment()
                );
            } else {
                fragmentTransaction.replace(
                        drawerListItem.getContainerViewResourceId(),
                        drawerListItem.getFragment()
                );
            }
            fragmentTransaction.commit();
        }
    }
}