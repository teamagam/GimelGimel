package com.teamagam.gimelgimel.app.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.view.view.fragments.GGFavoriteFragment;
import com.teamagam.gimelgimel.app.view.view.fragments.GGFragment;

//import com.commonsware.cwac.wakeful.WakefulIntentService;
//import com.teamagam.gimelgimel.app.control.services.GGService;

public class MainActivity extends BaseActivity<GGApplication>
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    // Represents the tag of the added fragments
    private final String TAG_FRAGMENT_TIPS_FEATURED = TAG + "TAG_FRAGMENT_GG_FEATURED";
    private final String TAG_FRAGMENT_TIPS_FAVORITE = TAG + "TAG_FRAGMENT_GG_FAVORITE";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private GGFragment mTipsFeaturedFragment;
    private GGFragment mTipsFavoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handling dynamic fragments section.
        // If this is the first time the Activity is created (and it's not a restart of it)
        if (savedInstanceState == null) {
            mTipsFeaturedFragment = new GGFragment();
            mTipsFavoriteFragment = new GGFavoriteFragment();
        }
        // Else, it's a restart, just fetch the already existing fragments
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            //TODO: clean
//            mTipsFeaturedFragment = (GGFragment) fragmentManager.findFragmentByTag(
//                    TAG_FRAGMENT_TIPS_FEATURED);
//            mTipsFavoriteFragment = (GGFragment) fragmentManager.findFragmentByTag(
//                    TAG_FRAGMENT_TIPS_FAVORITE);
        }

        // This is the title of the Activity, but it is expected that the inflated
        // fragments will come with their own title and than it will be overridden
        mTitle = getTitle();

        // This fragment is instantiated in a static way, so just find it by ID and reference it
        //TODO: clean
//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        //TODO: clean
//        mNavigationDrawerFragment.setUp(
//                R.id.navigation_drawer,
//                (DrawerLayout) findViewById(R.id.drawer_layout),
//                mToolbar);

//        WakefulIntentService.sendWakefulWork(this, GGService.actionGetTipsIntent(this));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        BaseFragment fragmentToSetInContainer = null;
        String tagToSetInContainer = null;

        // TODO: Create a real array/enum of possible drawer items
        switch (position) {
            case 0:
                fragmentToSetInContainer = mTipsFeaturedFragment;
                tagToSetInContainer = TAG_FRAGMENT_TIPS_FEATURED;
                break;
            case 1:
                fragmentToSetInContainer = mTipsFavoriteFragment;
                tagToSetInContainer = TAG_FRAGMENT_TIPS_FAVORITE;
                break;
        }

        // Now do the actual swap of views
        //TODO: clean
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, fragmentToSetInContainer, tagToSetInContainer)
//                .commit();

        // Set the title of the activity according to the fragment
        int titleRes = fragmentToSetInContainer.getTitle();
        if (titleRes > 0) {
            mTitle = getString(titleRes);
        }
    }

    public void restoreActionBar() {
        mToolbar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getActivityLayout() {
        return -1;
        //TODO: clean
//        return R.layout.activity_main;
    }
}