package com.teamagam.gimelgimel.app.settings.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.UsernameGenerator;
import com.teamagam.gimelgimel.app.settings.fragments.GeneralPreferenceFragment;


public class SetUsernameAlertDialogBuilder {

    public static final InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));
                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };

    private Context mContext;

    public SetUsernameAlertDialogBuilder(Context context) {
        mContext = context;
    }

    public AlertDialog create() {
        final EditText input = getInputEditText();
        final AlertDialog dialog = getBasicAlertDialog(input);
        input.addTextChangedListener(new UsernameValidatorTextWatcher(dialog));
        return dialog;
    }

    @NonNull
    private EditText getInputEditText() {
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setFilters(new InputFilter[]{EMOJI_FILTER});
        input.setText(UsernameGenerator.generateUsername(mContext));
        input.setSelection(0, input.getText().length());
        return input;
    }

    private AlertDialog getBasicAlertDialog(final EditText input) {
        return new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_title_set_username)
                .setMessage(R.string.dialog_message_set_username)
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        setUsername(input.getText().toString());
                    }
                }).setCancelable(false).create();
    }

    private void setUsername(String username) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String key = mContext.getString(R.string.user_name_text_key);
        pref.edit().putString(key, username).apply();
    }


    private static class UsernameValidatorTextWatcher implements TextWatcher {
        private final AlertDialog mDialog;

        public UsernameValidatorTextWatcher(AlertDialog dialog) {
            mDialog = dialog;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!GeneralPreferenceFragment.isValidDisplayName(s)) {
                mDialog.getButton(
                        AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            } else {
                mDialog.getButton(
                        AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }
    }
}
