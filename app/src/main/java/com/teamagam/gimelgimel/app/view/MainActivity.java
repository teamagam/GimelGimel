package com.teamagam.gimelgimel.app.view;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.control.sensors.GGLocation;
import com.teamagam.gimelgimel.app.view.fragments.ViewerFragment;
import com.teamagam.gimelgimel.app.view.adapters.DrawerListAdapter;
import com.teamagam.gimelgimel.app.view.fragments.CesiumFragment;
import com.teamagam.gimelgimel.app.view.fragments.FriendsFragment;
import com.teamagam.gimelgimel.app.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<GGApplication>
        implements ViewerFragment.OnFragmentInteractionListener {

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
    private String[] mMenuTitles;

    // Used to store the last screen title.
    private CharSequence mTitle;

    private TypedArray mIcons;

    //app fragments
    private FriendsFragment mFriendsFragment;
    private ViewerFragment mViewerFragment;

    //adapters
    private DrawerListAdapter mListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handling dynamic fragments section.
        // If this is the first time the Activity is created (and it's not a restart of it)
        if (savedInstanceState == null) {
            mFriendsFragment = new FriendsFragment();
            mViewerFragment = new ViewerFragment();
        }
        // Else, it's a restart, just fetch the already existing fragments
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();

            mFriendsFragment = (FriendsFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_FRIENDS);
            mViewerFragment = (ViewerFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_MAP_CESIUM);
        }

        // creating the menu of the left side
        createLeftDrawer();

        // calculating current gps location
        CalculateCurrentLocation();

        //todo: start both fragments.
        // Now do the actual swap of views
        if (!mIsTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mViewerFragment, TAG_FRAGMENT_MAP_CESIUM)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mFriendsFragment, TAG_FRAGMENT_FRIENDS)
                    .commit();
        } else {
            //// TODO: why do we need else???
        }

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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mTitle = getTitle();
        mMenuTitles = getResources().getStringArray(R.array.menu_array);
        // nav drawer icons from resources
        List<DrawerListItem> drawerListItems;
        try {
            mIcons = getResources()
                    .obtainTypedArray(R.array.nav_drawer_icons);

            // get data from the table by the ListAdapter
            drawerListItems = new ArrayList<DrawerListItem>();
            for (int i = 0; i < mMenuTitles.length; i++) {
                //todo: change to the general case
                drawerListItems.add(new DrawerListItem(mMenuTitles[i], mIcons.getDrawable(i)));
                mListAdapter = new DrawerListAdapter(this, R.layout.drawer_list_item,
                        drawerListItems);
            }
        }
        catch (Exception e)
        {
            //TODO: handle exception
        }
        finally {
            mIcons.recycle();
        }
        // recycle the array

        mDrawerList.setAdapter(mListAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        try {
            (this).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            (this).getSupportActionBar().setHomeButtonEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
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

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments

        //todo: change to the fragment you want (isntead of menuFragment)
        Fragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(MenuFragment.ARG_MENU_NUMBER, position);
        fragment.setArguments(args);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //todo: complete interaction with fragment.

    }

    /**
     * Fragment that appears in the "content_frame", shows a menu
     */
    public static class MenuFragment extends Fragment {
        public static final String ARG_MENU_NUMBER = "menu_number";

        public MenuFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
            int i = getArguments().getInt(ARG_MENU_NUMBER);
            String title = getResources().getStringArray(R.array.menu_array)[i];
            getActivity().setTitle(title);
            return rootView;
        }
    }
}