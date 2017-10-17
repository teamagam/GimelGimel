package com.teamagam.gimelgimel.app.map.actions;

import android.content.Context;
import android.databinding.Bindable;
import android.graphics.Color;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.BaseMapViewModel;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapEntitiesDisplayerFactory;
import io.reactivex.functions.Consumer;

public abstract class BaseGeometryStyleViewModel extends BaseMapViewModel {

  private static final String DEFAULT_BORDER_STYLE = "solid";

  protected Consumer<Integer> mPickColor;
  protected Consumer<String> mPickBorderStyle;
  protected String mBorderColor;
  protected String mBorderStyle;
  protected String mFillColor;

  private boolean mIsBorderColorPicking;

  public BaseGeometryStyleViewModel(MapEntitiesDisplayerFactory mapEntitiesDisplayerFactory,
      Context context,
      GGMapView ggMapView,
      Consumer<Integer> pickColor,
      Consumer<String> pickBorderStyle) {
    super(mapEntitiesDisplayerFactory, ggMapView);
    mPickColor = pickColor;
    mIsBorderColorPicking = false;
    mPickBorderStyle = pickBorderStyle;
    mBorderColor = colorToString(context.getResources().getColor(R.color.default_border_color));
    mBorderStyle = DEFAULT_BORDER_STYLE;
    mFillColor = colorToString(context.getResources().getColor(R.color.default_fill_color));
  }

  @Bindable
  public int getBorderColor() {
    return Color.parseColor(mBorderColor);
  }

  @Bindable
  public int getFillColor() {
    return Color.parseColor(mFillColor);
  }

  public void onBorderStyleClick() {
    try {
      mPickBorderStyle.accept(mBorderStyle);
    } catch (Exception e) {
      sLogger.w("Could not pick border style");
    }
  }

  public void onBorderColorClick() {
    try {
      mPickColor.accept(Color.parseColor(mBorderColor));
      mIsBorderColorPicking = true;
    } catch (Exception e) {
      sLogger.w("Could not pick border color");
    }
  }

  public void onFillColorClick() {
    try {
      mPickColor.accept(Color.parseColor(mFillColor));
      mIsBorderColorPicking = false;
    } catch (Exception e) {
      sLogger.w("Could not pick fill color");
    }
  }

  public void onColorSelected(int color) {
    if (mIsBorderColorPicking) {
      mBorderColor = colorToString(color);
      notifyPropertyChanged(BR.borderColor);
    } else {
      mFillColor = colorToString(color);
      notifyPropertyChanged(BR.fillColor);
    }
  }

  public void onBorderStyleSelected(String borderStyle) {
    mBorderStyle = borderStyle;
  }

  protected String colorToString(int color) {
    return "#" + Integer.toHexString(color).toUpperCase();
  }
}
