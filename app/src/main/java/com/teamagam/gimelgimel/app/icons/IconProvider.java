package com.teamagam.gimelgimel.app.icons;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public interface IconProvider {
  Drawable getIconDrawable(String id, int widthPx, int heightPx);

  void load(ImageView imageView, String iconId);
}
