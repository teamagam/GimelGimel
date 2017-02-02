package com.teamagam.gimelgimel.app.settings.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.text.TextUtils;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.Constants;

/**
 * This fragment shows general preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreferenceFragment extends BasePreferenceFragment {
    public static boolean isValidDisplayName(CharSequence displayName) {
        return TextUtils.getTrimmedLength(displayName) > 0 &&
                displayName.length() <= Constants.DISPLAY_NAME_MAX_LENGTH;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_name_text_key)));
    }

    @Override
    protected boolean isValidValue(Preference preference, Object value) {
        switch (preference.getTitleRes()) {
            case R.string.pref_title_display_name:
                return isValidDisplayName(value.toString());
            default:
                throw new RuntimeException(
                        "Preference title not recognized: " + preference.toString());
        }
    }

}
