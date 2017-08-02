package com.teamagam.gimelgimel.app.map.actions.freedraw;

import android.content.Context;
import android.graphics.Color;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.viewModel.BaseGeometryStyleViewModel;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;

@AutoFactory
public class FreeDrawViewModel extends BaseGeometryStyleViewModel {

  public static final double SPATIAL_TOLERANCE_DEG = 0.00001;

  private FreeDrawer mFreeDrawer;
  private GGMapView mGgMapView;
  private Consumer<Integer> mPickColor;
  private String mColor;
  private String mEraserActiveColor;
  private String mEraserInactiveColor;

  protected FreeDrawViewModel(@Provided Context context,
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      Consumer<Integer> pickColor,
      GGMapView ggMapView) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayDynamicLayersInteractorFactory, displayIntermediateRastersInteractorFactory, null,
        context, ggMapView, pickColor, null);
    mPickColor = pickColor;
    mColor = colorToString(context.getColor(R.color.colorAccent));
    mEraserActiveColor = colorToString(context.getColor(R.color.md_red_500));
    mEraserInactiveColor = colorToString(context.getColor(R.color.md_black_1000));
    mColor = colorToString(context.getColor(R.color.colorAccent));
    mGgMapView = ggMapView;
    mFreeDrawer = new FreeDrawer(mGgMapView, mColor, SPATIAL_TOLERANCE_DEG);
    mFreeDrawer.start();
  }

  public void onSwitchChanged(boolean isChecked) {
    sLogger.userInteraction("Free draw switch changed to " + isChecked);
    if (isChecked) {
      mGgMapView.setAllowPanning(true);
      mFreeDrawer.disable();
    } else {
      mGgMapView.setAllowPanning(false);
      mFreeDrawer.enable();
    }
  }

  public void onUndoClicked() {
    sLogger.userInteraction("undo");
    mFreeDrawer.undo();
  }

  public void onEraserClicked() {
    mFreeDrawer.switchMode();
    sLogger.userInteraction("Free drawing switch to "
        + (!mFreeDrawer.isInEraserMode() ? "eraser" : "drawing")
        + " mode");
    notifyPropertyChanged(BR._all);
  }

  public void onColorPickerClicked() {
    try {
      mPickColor.accept(Color.parseColor(mColor));
    } catch (Exception e) {
      sLogger.w("Cannot pick color");
    }
  }

  @Override
  public void start() {
    super.start();
    mFreeDrawer.enable();
  }

  @Override
  public void stop() {
    super.stop();
    mFreeDrawer.disable();
  }

  @Override
  public void onColorSelected(boolean positiveResult, int color) {
    if (positiveResult) {
      sLogger.userInteraction("Free drawing color changed to " + colorToString(color));
      mColor = colorToString(color);
      mFreeDrawer.setColor(mColor);
      notifyPropertyChanged(BR._all);
    }
  }

  public int getFreeDrawColor() {
    return Color.parseColor(mColor);
  }

  public int getEraserIconColor() {
    String color = mFreeDrawer.isInEraserMode() ? mEraserActiveColor : mEraserInactiveColor;
    return Color.parseColor(color);
  }
}
