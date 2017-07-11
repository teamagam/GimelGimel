package com.teamagam.gimelgimel.app.map.esri.graphic;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Glide;

public class IconProviderImpl implements IconProvider {
  @Override
  public Drawable getIcon(String id) {
    try {
      return Glide.with(new Activity()).load("").into(-1, -1).get();
      //Bitmap bitmap = Glide.with(new Activity()).load("").asBitmap().into(-1, -1).get();
      //return new BitmapDrawable(Resources.getSystem(), Bitmap.createBitmap(null));
    } catch (Exception e) {
      return null;
    }
  }
}
