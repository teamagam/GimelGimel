package com.teamagam.gimelgimel.app.common.databinding.adapters;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public class ImageSourceBindingAdapter {
  @BindingAdapter("android:src")
  public static void setImageUri(ImageView view, String imageUri) {
    if (imageUri == null) {
      view.setImageURI(null);
    } else {
      view.setImageURI(Uri.parse(imageUri));
    }
  }

  @BindingAdapter("android:src")
  public static void setImageUri(ImageView view, Uri imageUri) {
    view.setImageURI(imageUri);
  }

  @BindingAdapter("android:src")
  public static void setImageDrawable(ImageView view, Drawable drawable) {
    view.setImageDrawable(drawable);
  }

  @BindingAdapter("android:src")
  public static void setImageResource(ImageView imageView, int resource) {
    imageView.setImageResource(resource);
  }
}
