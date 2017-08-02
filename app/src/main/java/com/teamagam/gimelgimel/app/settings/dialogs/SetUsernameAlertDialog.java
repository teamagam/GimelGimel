package com.teamagam.gimelgimel.app.settings.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.EditTextAlertDialogBuilder;
import com.teamagam.gimelgimel.app.common.utils.UsernameGenerator;
import com.teamagam.gimelgimel.domain.utils.TextUtils;
import rx.functions.Action1;

public class SetUsernameAlertDialog {

  private Context mContext;
  private boolean mIsCancelable;
  private Action1<String> mFinishCallback;
  private EditTextAlertDialogBuilder mEditTextAlertDialogBuilder;

  public SetUsernameAlertDialog(Context context) {
    mContext = context;
    mIsCancelable = false;
    mFinishCallback = this::setUsername;
    mEditTextAlertDialogBuilder = new EditTextAlertDialogBuilder(mContext);
  }

  public void setCancelable(boolean cancelable) {
    mIsCancelable = cancelable;
  }

  public void setFinishCallback(Action1<String> finishCallback) {
    mFinishCallback = s -> {
      setUsername(s);
      finishCallback.call(s);
    };
  }

  public void show() {
    mEditTextAlertDialogBuilder.setIsCancelable(mIsCancelable)
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
