package com.teamagam.gimelgimel.app.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.R;
import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity {

  private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
      (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
          setListPreferenceSummary((ListPreference) preference, stringValue);
        } else if (preference instanceof RingtonePreference) {
          setRingtonePreferenceSummary((RingtonePreference) preference, stringValue);
        } else {
          setSimplePreferenceSummary(preference, stringValue);
        }
        return true;
      };

  private static void setListPreferenceSummary(ListPreference listPreference, String stringValue) {
    int index = listPreference.findIndexOfValue(stringValue);
    listPreference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
  }

  private static void setRingtonePreferenceSummary(RingtonePreference preference,
      String stringValue) {
    if (TextUtils.isEmpty(stringValue)) {
      preference.setSummary(R.string.pref_notifications_ringtone_silent_summary);
    } else {
      Ringtone ringtone =
          RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));
      if (ringtone == null) {
        preference.setSummary(null);
      } else {
        String name = ringtone.getTitle(preference.getContext());
        preference.setSummary(name);
      }
    }
  }

  private static void setSimplePreferenceSummary(Preference preference, String stringValue) {
    preference.setSummary(stringValue);
  }

  private static boolean isXLargeTablet(Context context) {
    return (context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
  }

  private static void bindPreferenceSummaryToValue(Preference preference) {
    preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

    // Trigger the listener immediately with the preference's
    // current value.
    sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
        PreferenceManager.getDefaultSharedPreferences(preference.getContext())
            .getString(preference.getKey(), ""));
  }

  /** {@inheritDoc} */
  @Override
  public boolean onIsMultiPane() {
    return isXLargeTablet(this);
  }

  /** {@inheritDoc} */
  @Override
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public void onBuildHeaders(List<Header> target) {
    loadHeadersFromResource(R.xml.pref_headers, target);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();
  }

  /**
   * This method stops fragment injection in malicious applications.
   * Make sure to deny any unknown fragments here.
   */
  protected boolean isValidFragment(String fragmentName) {
    return PreferenceFragment.class.getName().equals(fragmentName)
        || GeneralPreferenceFragment.class.getName().equals(fragmentName)
        || NotificationPreferenceFragment.class.getName().equals(fragmentName);
  }

  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      // Show the Up button in the action bar.
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  /**
   * This fragment shows general preferences only. It is used when the
   * activity is showing a two-pane settings UI.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class GeneralPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_general);
      setHasOptionsMenu(true);
      findPreference(getString(R.string.pref_general_about_key)).setSummary(getAboutSummary());
    }

    private String getAboutSummary() {
      return getString(R.string.pref_general_summary_template, BuildConfig.VERSION_NAME);
    }
  }

  /**
   * This fragment shows notification preferences only. It is used when the
   * activity is showing a two-pane settings UI.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class NotificationPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_notification);
      setHasOptionsMenu(true);

      // Bind the summaries of EditText/List/Dialog/Ringtone preferences
      // to their values. When their values change, their summaries are
      // updated to reflect the new value, per the Android Design
      // guidelines.
      bindPreferenceSummaryToValue(
          findPreference(getString(R.string.pref_notifications_ringtone_key)));
    }
  }
}
