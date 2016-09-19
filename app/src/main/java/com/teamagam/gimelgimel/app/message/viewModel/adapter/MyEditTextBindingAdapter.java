package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.databinding.BindingAdapter;
import android.widget.EditText;

/**
 * custom binding adapter for editText view
 */
public class MyEditTextBindingAdapter {

    @BindingAdapter(value = {"errorEnabled", "errorText" }, requireAll = true)
    public static void bindError(final EditText editText, final boolean isErrorEnabled,
                                      final String msg) {
        if(isErrorEnabled) {
            editText.setError(msg);
        }
    }

}
