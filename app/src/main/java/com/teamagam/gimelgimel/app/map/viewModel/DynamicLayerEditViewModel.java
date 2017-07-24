package com.teamagam.gimelgimel.app.map.viewModel;

import android.content.Context;
import android.databinding.Observable;
import android.graphics.Color;
import android.view.View;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

@AutoFactory
public class DynamicLayerEditViewModel extends BaseMapViewModel {

  private static final String DEFAULT_BORDER_STYLE = "solid";

  private final List<PointGeometry> mPoints;
  private final List<GeoEntity> mNewEntities;
  private final DisplayIconsInteractorFactory mDisplayIconsInteractorFactory;
  private GGMapView mGGMapView;
  private MapDrawer mMapDrawer;
  private MapEntityFactory mEntityFactory;
  private OnDrawGestureListener mGestureLisetner;
  private Consumer<PointGeometry> mOnMapClick;
  private Consumer<Integer> mPickColor;
  private Consumer<String> mPickBorderStyle;
  private GeoEntity mCurrentEntity;
  private String mBorderColor;
  private String mBorderStyle;
  private String mFillColor;
  private List<Icon> mIcons;
  private int mTypeIdx;
  private boolean mIsOnEditMode;
  private boolean mIsBorderColorPicking;
  private int mBorderStyleVisibility;
  private int mBorderColorVisibility;
  private int mFillColorVisibility;
  private int mIconPickerVisibility;

  protected DynamicLayerEditViewModel(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided DisplayIconsInteractorFactory displayIconsInteractorFactory,
      Context context,
      GGMapView ggMapView,
      Consumer<Integer> pickColor,
      Consumer<String> pickBorderStyle) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);

    mGGMapView = ggMapView;
    mDisplayIconsInteractorFactory = displayIconsInteractorFactory;
    mPickColor = pickColor;
    mPickBorderStyle = pickBorderStyle;

    mIsOnEditMode = false;
    mIsBorderColorPicking = false;
    mIcons = new ArrayList<>();
    mPoints = new ArrayList<>();
    mNewEntities = new ArrayList<>();
    mMapDrawer = new MapDrawer(mGGMapView);
    mEntityFactory = new MapEntityFactory();
    mGestureLisetner = new OnDrawGestureListener();
    mBorderColor = colorToString(context.getResources().getColor(R.color.default_border_color));
    mBorderStyle = DEFAULT_BORDER_STYLE;
    mFillColor = colorToString(context.getResources().getColor(R.color.default_fill_color));

    mGGMapView.setOnMapGestureListener(null);

    addOnPropertyChangedCallback(new OnPropertyChanged());

    mDisplayIconsInteractorFactory.create(icon -> {
      mIcons.add(icon);
    }).execute();
  }

  public boolean isOnEditMode() {
    return mIsOnEditMode;
  }

  public String[] getTypes() {
    return generateIconNames();
  }

  public int getTypeIdx() {
    return mTypeIdx;
  }

  public void setTypeIdx(int typeId) {
    mTypeIdx = typeId;
    notifyPropertyChanged(0);
  }

  public int getBorderColor() {
    return Color.parseColor(mBorderColor);
  }

  public int getFillColor() {
    return Color.parseColor(mFillColor);
  }

  public int getBorderStyleVisibility() {
    return mBorderStyleVisibility;
  }

  public int getBorderColorVisibility() {
    return mBorderColorVisibility;
  }

  public int getFillColorVisibility() {
    return mFillColorVisibility;
  }

  public int getIconPickerVisibility() {
    return mIconPickerVisibility;
  }

  public void onNewTabSelection(int newTabResource) {
    if (!mIsOnEditMode) {
      setupDrawingMode(newTabResource);
    }
  }

  public void saveCurrentGeometry() {
    mIsOnEditMode = false;

    if (mCurrentEntity != null) {
      mNewEntities.add(mCurrentEntity);
      clearCurrentEntity();
    }
  }

  public void dismissCurrentGeometry() {
    mIsOnEditMode = false;
    mMapDrawer.erase(mCurrentEntity);
    clearCurrentEntity();
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

    notifyPropertyChanged(0);
  }

  public void onBorderStyleSelected(String borderStyle) {
    mBorderStyle = borderStyle;
    notifyPropertyChanged(0);
  }

  private void refreshCurrentEntity() {
    if (mCurrentEntity instanceof PolylineEntity) {
      refreshPolyline();
    } else if (mCurrentEntity instanceof PolygonEntity) {
      refreshPolygon();
    } else if (mCurrentEntity instanceof PointEntity) {
      drawPoint((PointGeometry) mCurrentEntity.getGeometry());
    }
  }

  private String[] generateIconNames() {
    String[] names = new String[mIcons.size()];
    for (int i = 0; i < mIcons.size(); i++) {
      names[i] = mIcons.get(i).getDisplayName();
    }

    return names;
  }

  private void clearCurrentEntity() {
    mPoints.clear();
    mCurrentEntity = null;
  }

  private void setupDrawingMode(int newTabResource) {
    mGGMapView.setOnMapGestureListener(mGestureLisetner);

    if (newTabResource == R.id.tab_point) {
      mOnMapClick = this::drawPoint;
      setupPointStyleVisibility();
    } else if (newTabResource == R.id.tab_polyline) {
      mOnMapClick = this::drawPolyline;
      setupPolylineStyleVisibility();
    } else if (newTabResource == R.id.tab_polygon) {
      mOnMapClick = this::drawPolygon;
      setupPolygonStyleVisibility();
    } else {
      sLogger.w("Unknown tab selected");
      mGGMapView.setOnMapGestureListener(null);
    }

    notifyPropertyChanged(0);
  }

  private void setupPointStyleVisibility() {
    mBorderColorVisibility = View.GONE;
    mBorderStyleVisibility = View.GONE;
    mFillColorVisibility = View.GONE;
    mIconPickerVisibility = View.VISIBLE;
  }

  private void setupPolygonStyleVisibility() {
    mBorderColorVisibility = View.VISIBLE;
    mBorderStyleVisibility = View.VISIBLE;
    mFillColorVisibility = View.VISIBLE;
    mIconPickerVisibility = View.GONE;
  }

  private void setupPolylineStyleVisibility() {
    mBorderColorVisibility = View.VISIBLE;
    mBorderStyleVisibility = View.VISIBLE;
    mFillColorVisibility = View.GONE;
    mIconPickerVisibility = View.GONE;
  }

  private void drawPoint(PointGeometry geometry) {
    PointSymbol symbol =
        new PointSymbol.PointSymbolBuilder().setIconId(mIcons.get(mTypeIdx).getId()).build();

    mMapDrawer.erase(mCurrentEntity);
    mCurrentEntity = mEntityFactory.createPoint(geometry, symbol);
    mMapDrawer.draw(mCurrentEntity);
  }

  private void drawPolyline(PointGeometry pointGeometry) {
    mPoints.add(pointGeometry);
    refreshPolyline();
  }

  private void refreshPolyline() {
    PolylineSymbol symbol = new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(mBorderColor)
        .setBorderStyle(mBorderStyle)
        .build();

    mMapDrawer.erase(mCurrentEntity);
    mCurrentEntity = mEntityFactory.createPolyline(mPoints, symbol);
    mMapDrawer.draw(mCurrentEntity);
  }

  private void drawPolygon(PointGeometry pointGeometry) {
    mPoints.add(pointGeometry);
    refreshPolygon();
  }

  private void refreshPolygon() {
    PolygonSymbol symbol = new PolygonSymbol.PolygonSymbolBuilder().setBorderColor(mBorderColor)
        .setBorderStyle(mBorderStyle)
        .setFillColor(mFillColor)
        .build();

    mMapDrawer.erase(mCurrentEntity);
    mCurrentEntity = mEntityFactory.createPolygon(mPoints, symbol);
    mMapDrawer.draw(mCurrentEntity);
  }

  private String colorToString(int color) {
    return "#" + Integer.toHexString(color).toUpperCase();
  }

  private class OnDrawGestureListener implements OnMapGestureListener {

    @Override
    public void onLongPress(PointGeometry pointGeometry) {
    }

    @Override
    public void onTap(PointGeometry pointGeometry) {
      try {
        mIsOnEditMode = true;
        mOnMapClick.accept(pointGeometry);
      } catch (Exception ignored) {
      }
    }
  }

  private class OnPropertyChanged extends OnPropertyChangedCallback {
    @Override
    public void onPropertyChanged(Observable sender, int propertyId) {
      refreshCurrentEntity();
    }
  }
}
