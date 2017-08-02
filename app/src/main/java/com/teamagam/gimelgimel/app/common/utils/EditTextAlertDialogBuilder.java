package com.teamagam.gimelgimel.app.common.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import io.reactivex.functions.Function;
import rx.functions.Action1;

public class EditTextAlertDialogBuilder {
  private static final InputFilter EMOJIS_FILTER = new EmojisInputFilter();
  private static final AppLogger sLogger =
      AppLoggerFactory.create(EditTextAlertDialogBuilder.class);

  private Context mContext;
  private boolean mIsCancelable;
  private EditText mInputEditText;
  private Action1<String> mOnFinishCallback;
  private int mTitleResId;
  private int mMessageResId;
  private String mInitialText;
  private Function<String, Boolean> mTextValidator;

  public EditTextAlertDialogBuilder(Context context) {
    mContext = context;
    mIsCancelable = false;
    mInitialText = "";
    mTextValidator = s -> true;
    mOnFinishCallback = s -> {
    };
  }

  public EditTextAlertDialogBuilder setIsCancelable(boolean isCancelable) {
    mIsCancelable = isCancelable;
    return this;
  }

  public EditTextAlertDialogBuilder setTitleResId(int titleResId) {
    mTitleResId = titleResId;
    return this;
  }

  public EditTextAlertDialogBuilder setMessageResId(int messageResId) {
    mMessageResId = messageResId;
    return this;
  }

  public EditTextAlertDialogBuilder setInitialText(String initialText) {
    mInitialText = initialText;
    return this;
  }

  public EditTextAlertDialogBuilder setTextValidator(Function<String, Boolean> textValidator) {
    mTextValidator = textValidator;
    return this;
  }

  public EditTextAlertDialogBuilder setOnFinishCallback(Action1<String> onFinishCallback) {
    mOnFinishCallback = onFinishCallback;
    return this;
  }

  public AlertDialog create() {
    mInputEditText = createInputEditText();
    final AlertDialog dialog = getBasicAlertDialog(mInputEditText);
    mInputEditText.addTextChangedListener(new InputValidatorTextWatcher(dialog));
    return dialog;
  }

  private EditText createInputEditText() {
    final EditText input = new EditText(mContext);
    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
    input.setFilters(new InputFilter[] { EMOJIS_FILTER });
    input.setText(mInitialText);
    input.setSelection(0, input.getText().length());
    setInitialEnabledState(input);
    return input;
  }

  private void setInitialEnabledState(EditText editText) {
    try {
      editText.setEnabled(mTextValidator.apply(mInitialText));
    } catch (Exception ignored) {

    }
  }

  private AlertDialog getBasicAlertDialog(final EditText input) {
    return new AlertDialog.Builder(mContext).setTitle(mTitleResId)
        .setMessage(mMessageResId)
        .setView(input)
        .setPositiveButton(android.R.string.ok, (dialog1, which) -> onPositiveButtonClicked())
        .setCancelable(mIsCancelable)
        .create();
  }

  private void onPositiveButtonClicked() {
    try {
      mOnFinishCallback.call(getInput());
    } catch (Exception e) {
      sLogger.e("Dialog's positive button was clicked but callback threw an exception:"
          + System.lineSeparator()
          + e.toString());
    }
  }

  private String getInput() {
    return mInputEditText.getText().toString();
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

  private class InputValidatorTextWatcher implements TextWatcher {

    private final AlertDialog mDialog;

    public InputValidatorTextWatcher(AlertDialog dialog) {
      mDialog = dialog;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable input) {
      Button button = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
      try {
        button.setEnabled(mTextValidator.apply(input.toString()));
      } catch (Exception ignored) {
      }
    }
  }
}
