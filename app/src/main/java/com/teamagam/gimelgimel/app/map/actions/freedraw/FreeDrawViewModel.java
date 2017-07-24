package com.teamagam.gimelgimel.app.map.actions.freedraw;

import android.content.Context;
import android.graphics.Color;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.BaseMapViewModel;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;

@AutoFactory
public class FreeDrawViewModel extends BaseMapViewModel {

  public static final double SPATIAL_TOLERANCE_DEG = 0.1;
  private final FreeDrawer mFreeDrawer;
  private Consumer<Integer> mPickColor;
  private String mColor;

  protected FreeDrawViewModel(@Provided Context context,
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      Consumer<Integer> pickColor,
      GGMapView ggMapView) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);
    mPickColor = pickColor;
    mColor = colorToString(context.getColor(R.color.colorAccent));
    mFreeDrawer = new FreeDrawer(ggMapView, ggMapView.getMapDragEventObservable(), mColor,
        SPATIAL_TOLERANCE_DEG);
  }

  public void onUndoClicked() {
    mFreeDrawer.undo();
  }

  public void onEraserClicked() {
    mFreeDrawer.switchMode();
  }

  public void onColorPickerClicked() {
    try {
      mPickColor.accept(Color.parseColor(mColor));
    } catch (Exception e) {
      sLogger.w("Cannot pick color");
    }
  }

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

  private String colorToString(int color) {
    return "#" + Integer.toHexString(color).toUpperCase();
  }
}
