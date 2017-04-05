package com.teamagam.gimelgimel.app.settings.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.settings.SettingsActivity;

public abstract class BasePreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void bindPreferenceSummaryToValue(Preference preference) {
        setValidatorListener(preference);
        setSummaryToCurrentValue(preference);
    }

    protected boolean isValidValue(Preference preference, Object value) {
        return true;
    }

    protected void showValidationErrorMessage() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.validation_error_title)
                .setMessage(R.string.validation_error_message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel())
                .create().show();
    }

    private void setValidatorListener(Preference preference) {
        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            if (isValidValue(preference1, newValue)) {
                updateSummary(preference1, newValue);
                return true;
            } else {
                showValidationErrorMessage();
                return false;
            }
        });
    }

    private void updateSummary(Preference preference, Object value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value.toString());
            if (index >= 0) {
                preference.setSummary(listPreference.getEntries()[index]);
            }
        } else {
            preference.setSummary(value.toString());
        }
    }

    private void setSummaryToCurrentValue(Preference preference) {
        preference.setSummary(PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

}
