package com.teamagam.gimelgimel.app.view.listeners;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.MapManipulationFooterFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.VectorManipulationFooterFragment;

/**
 * Listens to an item click from the {@link NavigationView}.
 * Changes the footer container based on the clicked item.
 */
public class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private static Logger sLogger = LoggerFactory.create();

    Activity mActivity;
    DrawerLayout mDrawerLayout;

    /**
     * Initializes the listeners
     *
     * @param activity     The activity the contains the navigation view and the drawer layout
     * @param drawerLayout The drawer layout that contains the navigation view
     */
    public NavigationItemSelectedListener(Activity activity, DrawerLayout drawerLayout) {
        mActivity = activity;
        mDrawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        sLogger.userInteraction("Drawer item " + item + " clicked");
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }
}
