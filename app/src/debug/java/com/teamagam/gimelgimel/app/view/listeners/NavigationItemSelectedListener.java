package com.teamagam.gimelgimel.app.view.listeners;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.MapManipulationFooterFragment;
import com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments.VectorManipulationFooterFragment;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

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
        createFragmentByMenuItem(item);
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void createFragmentByMenuItem(MenuItem item) {
        // Currently we use the footer the show views from the Drawer
        // We should change this to more flexible code to support other views
        Fragment fragmentToDisplay = mActivity.getFragmentManager().findFragmentById(
                R.id.activity_main_container_footer);
        FragmentTransaction fragmentTransaction = mActivity.getFragmentManager().beginTransaction();

        switch (item.getItemId()) {
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

        if (fragmentToDisplay != null) {
            displayFragment(fragmentTransaction, fragmentToDisplay);
        }
    }

    private void removeFragment(FragmentTransaction fragmentTransaction,
                                Fragment fragmentToRemove) {
        if (fragmentToRemove != null) {
            fragmentTransaction.remove(fragmentToRemove);
            fragmentTransaction.commit();
        }
    }

    private void displayFragment(FragmentTransaction fragmentTransaction,
                                 @Nullable Fragment fragmentToDisplay) {
        fragmentTransaction.replace(R.id.activity_main_container_footer, fragmentToDisplay);
        fragmentTransaction.commit();
    }
}
