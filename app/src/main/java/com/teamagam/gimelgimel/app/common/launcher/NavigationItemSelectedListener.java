package com.teamagam.gimelgimel.app.common.launcher;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;

/**
 * Listens to an item click from the {@link NavigationView}.
 * Changes the footer container based on the clicked item.
 */
public class NavigationItemSelectedListener
    implements NavigationView.OnNavigationItemSelectedListener {

  private static AppLogger sLogger = AppLoggerFactory.create();

  Activity mActivity;
  DrawerLayout mDrawerLayout;

  /**
   * Initializes the listeners
   *
   * @param activity The activity the contains the navigation view and the drawer layout
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
