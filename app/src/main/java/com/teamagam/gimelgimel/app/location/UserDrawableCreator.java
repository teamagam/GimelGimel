package com.teamagam.gimelgimel.app.location;

import android.graphics.drawable.Drawable;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class UserDrawableCreator {

  private final int mWidth;
  private final int mHeight;
  private ColorGenerator mColorGenerator;

  public UserDrawableCreator(int width, int height) {
    mWidth = width;
    mHeight = height;
    mColorGenerator = ColorGenerator.MATERIAL;
  }

  public UserDrawableCreator() {
    this(-1, -1);
  }

  public Drawable getDrawable(String username, boolean isActive) {
    String firstLetter = username.substring(0, 1);
    int color = mColorGenerator.getColor(username);
    TextDrawable.IShapeBuilder builder = TextDrawable.builder();
    builder = config(isActive, builder);

    return builder.round().build(firstLetter, color);
  }

  private TextDrawable.IShapeBuilder config(boolean isActive, TextDrawable.IShapeBuilder builder) {
    TextDrawable.IConfigBuilder iConfigBuilder = builder.beginConfig();
    if (!isActive) {
      iConfigBuilder = iConfigBuilder.withBorder(5);
    }
    if (mWidth > 0) {
      iConfigBuilder.width(mWidth);
    }
    if (mHeight > 0) {
      iConfigBuilder.height(mHeight);
    }
    return iConfigBuilder.endConfig();
  }
}
