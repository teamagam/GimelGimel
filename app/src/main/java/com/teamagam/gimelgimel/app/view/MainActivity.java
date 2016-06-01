package com.teamagam.gimelgimel.app.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.control.sensors.GGLocation;
import com.teamagam.gimelgimel.app.model.ViewsModels.DrawerListItem;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.network.services.GGMessageLongPollingService;
import com.teamagam.gimelgimel.app.view.adapters.DrawerListAdapter;
import com.teamagam.gimelgimel.app.view.fragments.FriendsFragment;
import com.teamagam.gimelgimel.app.view.fragments.ViewerFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.GoToDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.ShowMessageDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.BaseViewerFooterFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.MapManipulationFooterFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.VectorManipulationFooterFragment;
import com.teamagam.gimelgimel.app.view.settings.SettingsActivity;
import com.teamagam.gimelgimel.app.view.viewer.GGMap;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<GGApplication>
        implements ViewerFragment.OnFragmentInteractionListener,
        ShowMessageDialogFragment.ShowMessageDialogFragmentInterface,
        GoToDialogFragment.GoToDialogFragmentInterface,
        BaseViewerFooterFragment.MapManipulationInterface {

    // Represents the tag of the added fragments
    private final String TAG_FRAGMENT_FRIENDS = TAG + "TAG_FRAGMENT_GG_FRIENDS";
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
    private FriendsFragment mFriendsFragment;
    private ViewerFragment mViewerFragment;

    //adapters
    private DrawerListAdapter mListAdapter;
    private MessageBroadcastReceiver mTextMessageReceiver;
    private MessageBroadcastReceiver mLatLongMessageReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handling dynamic fragments section.
        // If this is the first time the Activity is created (and it's not a restart of it)
        // Else, it's a restart, just fetch the already existing fragments
        if (savedInstanceState == null) {
            mFriendsFragment = new FriendsFragment();
            mViewerFragment = new ViewerFragment();
        }
        else {
            android.app.FragmentManager fragmentManager = getFragmentManager();

            mFriendsFragment = (FriendsFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_FRIENDS);
            mViewerFragment = (ViewerFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_MAP_CESIUM);
        }

        // creating the menu of the left side
        createLeftDrawer();

        // calculating current gps location
        CalculateCurrentLocation();

        // Don't add the fragment again, if it's already added
        if(!mViewerFragment.isAdded()) {

            //Set main content viewer fragment
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mViewerFragment, TAG_FRAGMENT_MAP_CESIUM)
                    .commit();
        }

        //todo: start both fragments.
        // Now do the actual swap of views
        if (mIsTwoPane) {
            //Set up two pane mode
        } else {
            //Set up one pane mode
        }

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

        //todo: where to start service? login activity?
        //WakefulIntentService.sendWakefulWork(this, GGService.actionGetTipsIntent(this));

    }

    private void CalculateCurrentLocation() {
        GGLocation gps = new GGLocation(this);
        mLocation = gps.getLocation();
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
                try {
                    getSupportActionBar().setTitle(mTitle);
                } catch (Exception e) {
                    //todo: handle exception
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
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

        GGMessageLongPollingService.startMessageLongPolling(this);
        // Register to receive messages.
        // We are registering an observer
        MessageBroadcastReceiver.registerReceiver(this, mTextMessageReceiver);
        MessageBroadcastReceiver.registerReceiver(this, mLatLongMessageReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageBroadcastReceiver.unregisterReceiver(this, mTextMessageReceiver);
        MessageBroadcastReceiver.unregisterReceiver(this, mLatLongMessageReceiver);

        GGMessageLongPollingService.stopMessagePolling(this);
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
            case R.id.action_location:
                //todo: change it the whatever activiy you like
                intent = new Intent(this, FriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_friends:
                intent = new Intent(this, FriendsActivity.class);
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
                getDrawable(R.drawable.ic_info_black_24dp),
                R.id.container_footer,
                new Fragment()
        ));
        list.add(new DrawerListItem(
                "Vector Manipulation",
                getDrawable(R.drawable.ic_info_black_24dp),
                R.id.container_footer,
                new VectorManipulationFooterFragment()
        ));
        list.add(new DrawerListItem(
                "Map Manipulation",
                getDrawable(R.drawable.ic_info_black_24dp),
                R.id.container_footer,
                new MapManipulationFooterFragment()
        ));
        return list;
    }

    @Override
    public GGMap getGGMap() {
        return mViewerFragment.getGGMap();
    }

    /**
     * Click listener for main activity's drawer component
     * Displays the fragment associated with item on click
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DrawerListItem drawerListItem = mListAdapter.getItem(position);

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