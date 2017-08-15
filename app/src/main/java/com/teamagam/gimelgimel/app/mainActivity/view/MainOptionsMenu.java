package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.settings.dialogs.SetUsernameAlertDialog;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.layers.LayersLocalCacheData;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.IconsDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.OutGoingMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.VectorLayerDao;
import javax.inject.Inject;

@AutoFactory
public class MainOptionsMenu {
  private static final int CLEAR_CACHE_MENU_ITEM_ID = 739;
  private static final int CHANGE_USERNAME_MENU_ITEM_ID = 112;
  private static final int CHANGE_SERVER_MENU_ITEM_ID = 536;

  private final static AppLogger sLogger = AppLoggerFactory.create();

  private final LayersLocalCacheData mLayersLocalCacheData;
  private final Navigator mNavigator;
  private final DatabaseNuker mDatabaseNuker;
  private final MenuInflater mMenuInflater;
  private final Context mContext;
  private final SharedPreferences mSharedPreferences;

  public MainOptionsMenu(@Provided LayersLocalCacheData layersLocalCacheData,
      @Provided Navigator navigator,
      @Provided DatabaseNuker databaseNuker,
      MenuInflater menuInflater,
      Context context) {
    mNavigator = navigator;
    mDatabaseNuker = databaseNuker;
    mMenuInflater = menuInflater;
    mLayersLocalCacheData = layersLocalCacheData;
    mContext = context;
    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
  }

  public void onCreate(Menu menu) {
    mMenuInflater.inflate(R.menu.main, menu);
    initMenuOptions(menu);
  }

  private void initMenuOptions(Menu menu) {
    if (BuildConfig.DEBUG) {
      initDeveloperOptions(menu);
    }
  }

  private void initDeveloperOptions(Menu menu) {
    menu.add(Menu.NONE, CLEAR_CACHE_MENU_ITEM_ID, Menu.NONE, R.string.menu_clear_vl_cache_title);
    menu.add(Menu.NONE, CHANGE_USERNAME_MENU_ITEM_ID, Menu.NONE,
        R.string.menu_change_username_title);
    menu.add(Menu.NONE, CHANGE_SERVER_MENU_ITEM_ID, Menu.NONE, getChangeServerTitle());
  }

  private String getChangeServerTitle() {
    int currentServer = mSharedPreferences.getString(Constants.MESSAGING_SERVER_PREF_KEY,
        Constants.MESSAGING_SERVER_DEFAULT).contains("1") ? 1 : 2;

    return mContext.getString(R.string.change_server_menu_item_title, currentServer);
  }

  public boolean onItemSelected(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.menu_item_settings:
        onSettingsClicked();
        return true;
      case CLEAR_CACHE_MENU_ITEM_ID:
        onClearCacheClicked();
        return true;
      case CHANGE_USERNAME_MENU_ITEM_ID:
        onChangeUsernameClicked();
        return true;
      case CHANGE_SERVER_MENU_ITEM_ID:
        onChangeServerClicked();
        return true;
      default:
        return false;
    }
  }

  private void onSettingsClicked() {
    mNavigator.openSettingsActivity();
  }

  private void onClearCacheClicked() {
    sLogger.userInteraction("Clear VL cache clicked");
    boolean success = mLayersLocalCacheData.clearCache();
    Toast.makeText(mContext, getClearVLMessage(success), Toast.LENGTH_LONG).show();
  }

  private int getClearVLMessage(boolean success) {
    return success ? R.string.clear_cache_successful_message : R.string.clear_cache_failure_message;
  }

  private void onChangeUsernameClicked() {
    sLogger.userInteraction("Change username clicked");
    SetUsernameAlertDialog dialog = new SetUsernameAlertDialog(mContext);
    dialog.setCancelable(true);
    dialog.show();
  }

  private void onChangeServerClicked() {
    sLogger.userInteraction("Change server clicked");
    new AlertDialog.Builder(mContext).setItems(R.array.change_server_menu_list_items,
        new ServerListClickListener()).show();
  }

  public static class DatabaseNuker {

    private final IconsDao mIconsDao;
    private final UserLocationDao mUserLocationDao;
    private final MessagesDao mMessagesDao;
    private final VectorLayerDao mVectorLayerDao;
    private final OutGoingMessagesDao mOutGoingMessagesDao;
    private final DynamicLayerDao mDynamicLayerDao;

    @Inject
    public DatabaseNuker(IconsDao iconsDao,
        UserLocationDao userLocationDao,
        MessagesDao messagesDao,
        VectorLayerDao vectorLayerDao, OutGoingMessagesDao outGoingMessagesDao,
        DynamicLayerDao dynamicLayerDao) {
      mIconsDao = iconsDao;
      mUserLocationDao = userLocationDao;
      mMessagesDao = messagesDao;
      mVectorLayerDao = vectorLayerDao;
      mOutGoingMessagesDao = outGoingMessagesDao;
      mDynamicLayerDao = dynamicLayerDao;
    }

    public void nuke() {
      mIconsDao.nukeTable();
      mUserLocationDao.nukeTable();
      mMessagesDao.nukeTable();
      mVectorLayerDao.nukeTable();
      mOutGoingMessagesDao.nukeTable();
      mDynamicLayerDao.nukeTable();
    }
  }

  private class ServerListClickListener implements DialogInterface.OnClickListener {

    private static final int DEV_1_INDEX = 0;
    private static final int DEV_2_INDEX = 1;

    @Override
    public void onClick(DialogInterface dialog, int index) {
      if (index == DEV_1_INDEX) {
        setServerUrl(Constants.MESSAGING_SERVER_URL_1);
      } else if (index == DEV_2_INDEX) {
        setServerUrl(Constants.MESSAGING_SERVER_URL_2);
      } else {
        return;
      }
      nukeDatabase();
      resetSynchronizationTimestamp();

      killApplication();
    }

    private void nukeDatabase() {
      mDatabaseNuker.nuke();
    }

    private void resetSynchronizationTimestamp() {
      mSharedPreferences.edit()
          .putLong(mContext.getString(R.string.pref_latest_received_message_date_in_ms), 0)
          .commit();
    }

    private void setServerUrl(String url) {
      mSharedPreferences.edit().putString(Constants.MESSAGING_SERVER_PREF_KEY, url).commit();
    }

    private void killApplication() {
      System.exit(0);
    }
  }
}
