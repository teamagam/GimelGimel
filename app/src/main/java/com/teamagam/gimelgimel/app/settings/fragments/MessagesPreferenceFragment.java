package com.teamagam.gimelgimel.app.settings.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import com.teamagam.gimelgimel.R;

/**
 * This fragment shows data and sync preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MessagesPreferenceFragment extends BasePreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_mesages);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.sync_messages_frequency_key)));
    }

}