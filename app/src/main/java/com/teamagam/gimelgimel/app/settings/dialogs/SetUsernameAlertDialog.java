package com.teamagam.gimelgimel.app.settings.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.EditTextAlertDialogBuilder;
import com.teamagam.gimelgimel.app.common.utils.UsernameGenerator;
import com.teamagam.gimelgimel.domain.utils.TextUtils;
import io.reactivex.functions.Consumer;

public class SetUsernameAlertDialog {

  private Context mContext;
  private boolean mIsCancelable;
  private Consumer<String> mFinishCallback;

  public SetUsernameAlertDialog(Context context) {
    mContext = context;
    mIsCancelable = false;
    mFinishCallback = this::setUsername;
  }

  public void setCancelable(boolean cancelable) {
    mIsCancelable = cancelable;
  }

  public void addFinishCallback(Consumer<String> finishCallback) {
    mFinishCallback = s -> {
      setUsername(s);
      try {
        finishCallback.accept(s);
      } catch (Exception ignored) {
      }
    };
  }

  public void show() {
    new EditTextAlertDialogBuilder(mContext).setIsCancelable(mIsCancelable)
        .setOnFinishCallback(mFinishCallback)
        .setInitialText(generateUsername())
        .setMessageResId(R.string.dialog_set_username_message)
        .setTitleResId(R.string.dialog_set_username_title)
        .setTextValidator(this::validateInput)
        .create()
        .show();
  }

  private void setUsername(String username) {
    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
    String key = mContext.getString(R.string.user_name_text_key);
    pref.edit().putString(key, username).apply();
  }

  private boolean validateInput(String input) {
    return TextUtils.isValidDisplayName(input);
  }

  private String generateUsername() {
    String[] animals = mContext.getResources().getStringArray(R.array.name_generator_animals);
    String[] adjectives = mContext.getResources().getStringArray(R.array.name_generator_adjectives);
    return new UsernameGenerator(animals, adjectives).generateUsername();
  }
}
