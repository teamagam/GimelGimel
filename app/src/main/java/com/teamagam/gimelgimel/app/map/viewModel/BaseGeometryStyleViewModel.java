package com.teamagam.gimelgimel.app.map.viewModel;

import android.content.Context;
import android.graphics.Color;
import com.android.databinding.library.baseAdapters.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseGeometryStyleViewModel extends BaseMapViewModel {

  protected static final int ENTITY_REFRESH_PROPERTY_ID = -1;
  private static final String DEFAULT_BORDER_STYLE = "solid";

  protected DisplayIconsInteractorFactory mDisplayIconsInteractorFactory;
  protected Consumer<Integer> mPickColor;
  protected Consumer<String> mPickBorderStyle;
  protected String mBorderColor;
  protected String mBorderStyle;
  protected String mFillColor;
  protected List<Icon> mIcons;
  protected int mTypeIdx;
  protected boolean mIsBorderColorPicking;

  public BaseGeometryStyleViewModel(DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      DisplayIconsInteractorFactory displayIconsInteractorFactory,
      Context context,
      GGMapView ggMapView,
      Consumer<Integer> pickColor,
      Consumer<String> pickBorderStyle) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayDynamicLayersInteractorFactory, displayIntermediateRastersInteractorFactory,
        ggMapView);
    mDisplayIconsInteractorFactory = displayIconsInteractorFactory;
    mIcons = new ArrayList<>();
    mPickColor = pickColor;
    mIsBorderColorPicking = false;
    mPickBorderStyle = pickBorderStyle;
    mBorderColor = colorToString(context.getResources().getColor(R.color.default_border_color));
    mBorderStyle = DEFAULT_BORDER_STYLE;
    mFillColor = colorToString(context.getResources().getColor(R.color.default_fill_color));
  }

  public String[] getTypes() {
    return generateIconNames();
  }

  public int getTypeIdx() {
    return mTypeIdx;
  }

  public void setTypeIdx(int typeId) {
    mTypeIdx = typeId;
    notifyPropertyChanged(BR._all);
  }

  public int getBorderColor() {
    return Color.parseColor(mBorderColor);
  }

  public int getFillColor() {
    return Color.parseColor(mFillColor);
  }

  public void onBorderStyleClick() {
    try {
      mPickBorderStyle.accept(mBorderStyle);
    } catch (Exception ignored) {
      sLogger.w("Could not pick border style");
    }
  }

  public void onBorderColorClick() {
    try {
      mPickColor.accept(Color.parseColor(mBorderColor));
      mIsBorderColorPicking = true;
    } catch (Exception ignored) {
      sLogger.w("Could not pick border color");
    }
  }

  public void onFillColorClick() {
    try {
      mPickColor.accept(Color.parseColor(mFillColor));
      mIsBorderColorPicking = false;
    } catch (Exception ignored) {
      sLogger.w("Could not pick fill color");
    }
  }

  public void onColorSelected(boolean positiveClick, int color) {
    if (positiveClick) {
      if (mIsBorderColorPicking) {
        mBorderColor = colorToString(color);
      } else {
        mFillColor = colorToString(color);
      }
    }

    notifyPropertyChanged(BR._all);
  }

  public void onBorderStyleSelected(String borderStyle) {
    mBorderStyle = borderStyle;
    notifyPropertyChanged(BR._all);
  }

  protected void loadIcons() {
    mDisplayIconsInteractorFactory.create(icon -> mIcons.add(icon)).execute();
  }

  protected String colorToString(int color) {
    return "#" + Integer.toHexString(color).toUpperCase();
  }

  private String[] generateIconNames() {
    String[] names = new String[mIcons.size()];
    for (int i = 0; i < mIcons.size(); i++) {
      names[i] = mIcons.get(i).getDisplayName();
    }

    return names;
  }
}
