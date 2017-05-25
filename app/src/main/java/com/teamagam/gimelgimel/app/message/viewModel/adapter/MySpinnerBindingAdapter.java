package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.widget.Spinner;

@InverseBindingMethods({
    @InverseBindingMethod(type = Spinner.class, attribute = "android:selectedItemPosition"),
})
public class MySpinnerBindingAdapter {
  //
  //    @InverseBindingAdapter(attribute = "android:selectedItemPosition")
  //    public static int getPosition(Spinner spinner) {
  //        return spinner.getSelectedItemPosition();
  //    }
}
