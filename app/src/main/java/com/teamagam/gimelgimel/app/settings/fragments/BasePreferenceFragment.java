package com.teamagam.gimelgimel.app.settings.fragments;

import android.content.DialogInterface;
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

    private void setValidatorListener(Preference preference) {
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (isValidValue(preference, newValue)) {
                    updateSummary(preference, newValue);
                    return true;
                } else {
                    showValidationErrorMessage();
                    return false;
                }
            }
        });
    }

    protected boolean isValidValue(Preference preference, Object value) {
        return true;
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

    protected void showValidationErrorMessage() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error)
                .setMessage(R.string.error_settings_validation)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    private void setSummaryToCurrentValue(Preference preference) {
        preference.setSummary(PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

}
