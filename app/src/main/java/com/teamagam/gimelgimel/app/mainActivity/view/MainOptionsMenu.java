package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.data.layers.LayersLocalCacheData;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import com.teamagam.gimelgimel.domain.utils.TextUtils;

@AutoFactory
public class MainOptionsMenu {
  private static final int CLEAR_CACHE_MENU_ITEM_ID = 739;
  private static final int CHANGE_USERNAME_MENU_ITEM_ID = 112;

  private final static AppLogger sLogger = AppLoggerFactory.create();

  private final MenuInflater mMenuInflater;
  private final PreferencesUtils mPreferencesUtils;
  private final LayersLocalCacheData mLayersLocalCacheData;
  private final Context mContext;

  public MainOptionsMenu(@Provided PreferencesUtils preferencesUtils,
      @Provided LayersLocalCacheData layersLocalCacheData,
      MenuInflater menuInflater,
      Context context) {
    mMenuInflater = menuInflater;
    mPreferencesUtils = preferencesUtils;
    mLayersLocalCacheData = layersLocalCacheData;
    mContext = context;
  }

  public void onCreate(Menu menu) {
    mMenuInflater.inflate(R.menu.main, menu);
    initMenuOptions(menu);
  }

  private void initMenuOptions(Menu menu) {
    if (BuildConfig.DEBUG) {
      initDeveloperOptions(menu);
    }
    initUtmOption(menu);
  }

  private void initDeveloperOptions(Menu menu) {
    menu.add(Menu.NONE, CLEAR_CACHE_MENU_ITEM_ID, Menu.NONE, R.string.menu_clear_vl_cache_title);
    menu.add(Menu.NONE, CHANGE_USERNAME_MENU_ITEM_ID, Menu.NONE,
        R.string.menu_change_username_title);
  }

  private void initUtmOption(Menu menu) {
    MenuItem useUtmItem = menu.findItem(R.id.menu_item_use_utm);
    useUtmItem.setChecked(mPreferencesUtils.shouldUseUtm());
  }

  public boolean onItemSelected(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.menu_item_use_utm:
        onUseUTMClicked(menuItem);
        return true;
      case CLEAR_CACHE_MENU_ITEM_ID:
        onClearCacheClicked();
        return true;
      case CHANGE_USERNAME_MENU_ITEM_ID:
        onChangeUsernameClicked();
        return true;
      default:
        return false;
    }
  }

  private void onUseUTMClicked(MenuItem menuItem) {
    sLogger.userInteraction("Change coordinate system menu option item clicked");

    mPreferencesUtils.toggleCoordinateSystemPrefs();
    menuItem.setChecked(mPreferencesUtils.shouldUseUtm());
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
    EditText usernameEditText = new EditText(mContext);
    new AlertDialog.Builder(mContext).setTitle(R.string.alert_dialog_change_username_title)
        .setView(usernameEditText)
        .setPositiveButton(R.string.alert_dialog_change_username_positive_button_text,
            new OnUsernameChangeButtonClickListener(usernameEditText))
        .show();
  }

  private class OnUsernameChangeButtonClickListener implements DialogInterface.OnClickListener {

    private final EditText mUsernameEditText;

    public OnUsernameChangeButtonClickListener(EditText usernameEditText) {
      mUsernameEditText = usernameEditText;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      String username = mUsernameEditText.getText().toString();
      if (TextUtils.isValidDisplayName(username)) {
        mPreferencesUtils.setUsername(username);
        dialog.dismiss();
      } else {
        Toast.makeText(mContext, "Invalid username", Toast.LENGTH_SHORT).show();
      }
    }
  }
}
