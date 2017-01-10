package com.teamagam.gimelgimel.app.settings.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import com.teamagam.gimelgimel.R;

/**
 * This fragment shows notification preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GpsPreferenceFragment extends BasePreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_gps);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.gps_distance_key)));
    }
}