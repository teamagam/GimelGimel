package com.teamagam.gimelgimel.app.settings.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.UsernameGenerator;
import com.teamagam.gimelgimel.domain.utils.TextUtils;
import io.reactivex.functions.Action;

public class SetUsernameAlertDialogBuilder {

  private static final InputFilter EMOJIS_FILTER = new EmojisInputFilter();
  private static final AppLogger sLogger =
      AppLoggerFactory.create(SetUsernameAlertDialogBuilder.class);

  private Context mContext;
  private EditText mInputEditText;
  private Action mOnFinishCallback;

  public SetUsernameAlertDialogBuilder(Context context) {
    mContext = context;
  }

  public AlertDialog create() {
    mInputEditText = createInputEditText();
    final AlertDialog dialog = getBasicAlertDialog(mInputEditText);
    mInputEditText.addTextChangedListener(new UsernameValidatorTextWatcher(dialog));
    return dialog;
  }

  public void setOnFinishCallback(Action onFinishCallback) {
    mOnFinishCallback = onFinishCallback;
  }

  @NonNull
  private EditText createInputEditText() {
    final EditText input = new EditText(mContext);
    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
    input.setFilters(new InputFilter[] { EMOJIS_FILTER });
    input.setText(generateUsername());
    input.setSelection(0, input.getText().length());
    return input;
  }

  private String generateUsername() {
    String[] animals = mContext.getResources().getStringArray(R.array.name_generator_animals);
    String[] adjectives = mContext.getResources().getStringArray(R.array.name_generator_adjectives);
    return new UsernameGenerator(animals, adjectives).generateUsername();
  }

  private AlertDialog getBasicAlertDialog(final EditText input) {
    return new AlertDialog.Builder(mContext).setTitle(R.string.dialog_set_username_title)
        .setMessage(R.string.dialog_set_username_message)
        .setView(input)
        .setPositiveButton(android.R.string.ok, (dialog1, which) -> onPositiveButtonClicked())
        .setCancelable(false)
        .create();
  }

  private void onPositiveButtonClicked() {
    setUsername(mInputEditText.getText().toString());

    if (mOnFinishCallback != null) {
      try {
        mOnFinishCallback.run();
      } catch (Exception e) {
        sLogger.e("UiRunner encountered a problem:\n" + e.toString());
      }
    }
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
    public void afterTextChanged(Editable username) {
      Button button = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
      button.setEnabled(TextUtils.isValidDisplayName(username.toString()));
    }
  }

  private static class EmojisInputFilter implements InputFilter {

    private static final CharSequence NO_FILTER = null;
    private static final String REPLACE_WITH_EMPTY = "";

    @Override
    public CharSequence filter(CharSequence source,
        int start,
        int end,
        Spanned dest,
        int dstart,
        int dend) {
      for (int index = start; index < end; index++) {
        if (isEmoji(getChar(source, index))) {
          return REPLACE_WITH_EMPTY;
        }
      }
      return NO_FILTER;
    }

    private boolean isEmoji(char c) {
      return Character.getType(c) == Character.SURROGATE;
    }

    private char getChar(CharSequence source, int index) {
      return source.charAt(index);
    }
  }
}
