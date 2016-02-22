package com.teamagam.gimelgimel.app.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.view.fragments.CesiumFragment;
import com.teamagam.gimelgimel.app.view.fragments.FriendsFragment;

//todo: clean
//import com.commonsware.cwac.wakeful.WakefulIntentService;
//import com.teamagam.gimelgimel.app.control.services.GGService;

public class MainActivity extends BaseActivity<GGApplication>{

    // Represents the tag of the added fragments
    private final String TAG_FRAGMENT_FRIENDS = TAG + "TAG_FRAGMENT_GG_FRIENDS";
    private final String TAG_FRAGMENT_MAP_CESIUM = TAG + "TAG_FRAGMENT_GG_CESIUM";

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private FriendsFragment mFriendsFragment;
    private CesiumFragment mCesiumFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handling dynamic fragments section.
        // If this is the first time the Activity is created (and it's not a restart of it)
        if (savedInstanceState == null) {
            mFriendsFragment = new FriendsFragment();
            mCesiumFragment = new CesiumFragment();
        }
        // Else, it's a restart, just fetch the already existing fragments
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();

            mFriendsFragment = (FriendsFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_FRIENDS);
            mCesiumFragment = (CesiumFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_MAP_CESIUM);
        }

        // This is the title of the Activity, but it is expected that the inflated
        // fragments will come with their own title and than it will be overridden
        mTitle = getTitle();

        //todo: start both fragments.
        // Now do the actual swap of views
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mFriendsFragment, TAG_FRAGMENT_FRIENDS)
                .commit();
//        R.id.fragment_cesium_view

        //todo: where to start service? login activity?
//        WakefulIntentService.sendWakefulWork(this, GGService.actionGetTipsIntent(this));
    }

    public void restoreActionBar() {
//        mToolbar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // todo: clean
        if (true) {
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
        return R.layout.activity_main;
    }
}