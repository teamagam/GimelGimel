package com.teamagam.gimelgimel.app.common.utils;

import android.content.Context;
import android.content.DialogInterface;
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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class EditTextAlertDialogBuilder {
  private static final InputFilter EMOJIS_FILTER = new EmojisInputFilter();
  private static final AppLogger sLogger =
      AppLoggerFactory.create(EditTextAlertDialogBuilder.class);

  private Context mContext;
  private boolean mIsCancelable;
  private EditText mInputEditText;
  private Consumer<String> mOnFinishCallback;
  private int mTitleResId;
  private int mMessageResId;
  private String mInitialText;
  private Predicate<String> mTextValidator;
  private AlertDialog mDialog;

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

  public EditTextAlertDialogBuilder setTextValidator(Predicate<String> textValidator) {
    mTextValidator = textValidator;
    return this;
  }

  public EditTextAlertDialogBuilder setOnFinishCallback(Consumer<String> onFinishCallback) {
    mOnFinishCallback = onFinishCallback;
    return this;
  }

  public AlertDialog create() {
    mInputEditText = createInputEditText();
    mDialog = getBasicAlertDialog(mInputEditText);
    mInputEditText.addTextChangedListener(new InputValidatorTextWatcher());
    updatePositiveButtonStateOnDisplay();
    return mDialog;
  }

  private EditText createInputEditText() {
    final EditText input = new EditText(mContext);
    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
    input.setFilters(new InputFilter[] { EMOJIS_FILTER });
    input.setText(mInitialText);
    input.setSelection(0, input.getText().length());
    return input;
  }

  private AlertDialog getBasicAlertDialog(final EditText input) {
    return new AlertDialog.Builder(mContext).setTitle(mTitleResId)
        .setMessage(mMessageResId)
        .setView(input)
        .setPositiveButton(android.R.string.ok, (dialog, which) -> onPositiveButtonClicked())
        .setCancelable(mIsCancelable)
        .create();
  }

  private void onPositiveButtonClicked() {
    try {
      mOnFinishCallback.accept(getInput());
    } catch (Exception e) {
      sLogger.e("Dialog's positive button was clicked but callback threw an exception:"
          + System.lineSeparator()
          + e.toString());
    }
  }

  private String getInput() {
    return mInputEditText.getText().toString();
  }

  private void updatePositiveButtonStateOnDisplay() {
    mDialog.setOnShowListener(dialogInterface -> updateButtonEnabledStatus(getInput()));
  }

  private void updateButtonEnabledStatus(String input) {
    try {
      Button positiveButton = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);
      positiveButton.setEnabled(mTextValidator.test(input));
    } catch (Exception ignored) {
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

  private class InputValidatorTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable input) {
      updateButtonEnabledStatus(input.toString());
    }
  }
}
