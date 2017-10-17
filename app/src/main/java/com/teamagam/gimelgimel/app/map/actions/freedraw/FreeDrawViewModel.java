package com.teamagam.gimelgimel.app.map.actions.freedraw;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.BaseGeometryStyleViewModel;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import java.util.Collection;

@AutoFactory
public class FreeDrawViewModel extends BaseGeometryStyleViewModel {

  private static final double SPATIAL_TOLERANCE_DEG = 0.00001;

  private FreeDrawer mFreeDrawer;
  private GGMapView mGgMapView;
  private Consumer<Integer> mPickColor;
  private String mColor;
  private String mEraserActiveColor;
  private String mEraserInactiveColor;

  protected FreeDrawViewModel(@Provided Context context,
      @Provided
          com.teamagam.gimelgimel.app.map.MapEntitiesDisplayerFactory mapEntitiesDisplayerFactory,
      Consumer<Integer> pickColor,
      GGMapView ggMapView) {
    super(mapEntitiesDisplayerFactory, context, ggMapView, pickColor, null);
    mPickColor = pickColor;
    mColor = getColorString(context, R.color.colorAccent);
    mEraserActiveColor = getColorString(context, R.color.md_red_500);
    mEraserInactiveColor = getColorString(context, R.color.md_black_1000);
    mColor = getColorString(context, R.color.colorAccent);
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
    mGgMapView.setAllowPanning(false);
    mFreeDrawer.enable();
  }

  @Override
  public void stop() {
    super.stop();
    mFreeDrawer.disable();
    mGgMapView.setAllowPanning(true);
  }

  @Override
  public void onColorSelected(int color) {
    sLogger.userInteraction("Free drawing color changed to " + colorToString(color));
    mColor = colorToString(color);
    mFreeDrawer.setColor(mColor);
    notifyPropertyChanged(BR._all);
  }

  public int getFreeDrawColor() {
    return Color.parseColor(mColor);
  }

  public int getEraserIconColor() {
    String color = mFreeDrawer.isInEraserMode() ? mEraserActiveColor : mEraserInactiveColor;
    return Color.parseColor(color);
  }

  public Collection<GeoEntity> getEntities() {
    return mFreeDrawer.getEntities();
  }

  public Observable<Object> getSignalOnStartDrawingObservable() {
    return mFreeDrawer.getSignalOnStartDrawingObservable();
  }

  public void clearFreeDrawer() {
    mFreeDrawer.clear();
  }

  private String getColorString(Context context, int colorResId) {
    return colorToString(getColor(context, colorResId));
  }

  private int getColor(Context context, int colorResId) {
    return ContextCompat.getColor(context, colorResId);
  }
}
