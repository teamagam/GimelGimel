package com.teamagam.gimelgimel.app.map.viewModel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.OnMapGestureListener;
import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawViewModel;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteAddDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteRemoveDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractor;
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
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AutoFactory
public class EditDynamicLayerViewModel extends BaseGeometryStyleViewModel {

  private final DisplayIconsInteractorFactory mDisplayIconsInteractorFactory;
  private final SendRemoteAddDynamicEntityRequestInteractorFactory
      mAddDynamicEntityRequestInteractorFactory;
  private FreeDrawViewModel mFreeDrawViewModel;
  private List<PointGeometry> mPoints;
  private DrawOnTapGestureListener mDrawOnTapGestureListener;
  private Consumer<Icon> mIconDisplayer;
  private DynamicLayerEntityDeleteListener mDeleteClickedEntityListener;
  private String mDynamicLayerId;
  private GGMapView mGGMapView;
  private MapDrawer mMapDrawer;
  private MapEntityFactory mEntityFactory;
  private Consumer<PointGeometry> mOnMapClick;
  private GeoEntity mCurrentEntity;
  private boolean mIsOnEditMode;
  private int mBorderStyleVisibility;
  private int mBorderColorVisibility;
  private int mFillColorVisibility;
  private int mIconPickerVisibility;
  private int mSymbologyPanelVisibility;
  private int mFreeDrawPanelVisibility;
  private boolean mIsFreeDrawMode;
  private ResourceObserver<Object> mOnStartFreeDrawingObserver;
  private Map<String, DynamicLayer> mIdToDynamicLayerMap;
  private Icon mSelectedIcon;
  private Navigator mNavigator;

  protected EditDynamicLayerViewModel(@Provided Context context,
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided DisplayIconsInteractorFactory displayIconsInteractorFactory,
      @Provided
          SendRemoteAddDynamicEntityRequestInteractorFactory addDynamicEntityRequestInteractorFactory,
      @Provided
          SendRemoteRemoveDynamicEntityRequestInteractorFactory removeDynamicEntityRequestInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawViewModelFactory freeDrawViewModelFactory,
      Navigator navigator,
      GGMapView ggMapView,
      DynamicLayerEntityDeleteListener.DeleteEntityDialogDisplayer deleteEntityDialogDisplayer,
      Consumer<Integer> pickColor,
      Consumer<String> pickBorderStyle,
      Consumer<Icon> iconDisplayer,
      String dynamicLayerId) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayDynamicLayersInteractorFactory, displayIntermediateRastersInteractorFactory, context,
        ggMapView, pickColor, pickBorderStyle);
    mDisplayIconsInteractorFactory = displayIconsInteractorFactory;
    mAddDynamicEntityRequestInteractorFactory = addDynamicEntityRequestInteractorFactory;
    mRemoveDynamicEntityRequestInteractorFactory = removeDynamicEntityRequestInteractorFactory;
    mNavigator = navigator;
    mGGMapView = ggMapView;
    mDeleteEntityDialogDisplayer = deleteEntityDialogDisplayer;
    mIconDisplayer = iconDisplayer;
    mDynamicLayerId = dynamicLayerId;
    mIsOnEditMode = false;
    mPoints = new ArrayList<>();
    mMapDrawer = new MapDrawer(mGGMapView);
    mEntityFactory = new MapEntityFactory();
    mDrawOnTapGestureListener = new DrawOnTapGestureListener();
    mDeleteClickedEntityListener = new DynamicLayerEntityDeleteListener(deleteEntityDialogDisplayer,
        removeDynamicEntityRequestInteractorFactory, displayDynamicLayersInteractorFactory,
        dynamicLayerId);

    mGGMapView.setOnEntityClickedListener(null);
    mGGMapView.setOnMapGestureListener(null);
    addOnPropertyChangedCallback(new OnPropertyChanged());

    mFreeDrawViewModel = freeDrawViewModelFactory.create(pickColor, mGGMapView);
    mFreeDrawViewModel.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
      @Override
      public void onPropertyChanged(Observable sender, int propertyId) {
        notifyPropertyChanged(propertyId);
      }
    });
    mFreeDrawViewModel.init();
  }

  @Override
  public void init() {
    super.init();
    initializeSelectedIcon();
  }

  @Override
  public void start() {
    super.start();
    mDeleteClickedEntityListener.startDynamicLayerEntitiesSync();
  }

  @Override
  public void stop() {
    super.stop();
    mDeleteClickedEntityListener.stopDynamicLayerEntitiesSync();
  }

  @Bindable
  public boolean isOnEditMode() {
    return mIsOnEditMode;
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

  public int getSymbologyPanelVisibility() {
    return mSymbologyPanelVisibility;
  }

  public int getFreeDrawPanelVisibility() {
    return mFreeDrawPanelVisibility;
  }

  public void onNewTabSelection(int newTabResource) {
    if (!mIsOnEditMode) {
      setupActionMode(newTabResource);
    }
  }

  @Override
  public void onBorderStyleSelected(String borderStyle) {
    super.onBorderStyleSelected(borderStyle);
    refreshCurrentEntity();
  }

  public void sendCurrentGeometry() {
    setIsOnEditMode(false);
    Collection<GeoEntity> toSend = getToSendEntities();
    sendEntities(toSend);
    removeEntitiesFromMap(toSend);
    clearInnerEntitiesState();
  }

  public void onSwitchChanged(boolean isChecked) {
    mFreeDrawViewModel.onSwitchChanged(isChecked);
  }

  public void onIconSelectionClicked() {
    mNavigator.openIconSelectionDialog(icon -> {
      updateIcon(icon);
      refreshCurrentEntity();
    });
  }

  private void updateIcon(Icon icon) {
    mSelectedIcon = icon;
    try {
      mIconDisplayer.accept(icon);
    } catch (Exception e) {
    }
  }

  public void onUndoClicked() {
    mFreeDrawViewModel.onUndoClicked();
  }

  public void onEraserClicked() {
    mFreeDrawViewModel.onEraserClicked();
  }

  public void onFreeDrawColorPickerClicked() {
    mFreeDrawViewModel.onColorPickerClicked();
  }

  public void onColorSelected(boolean positiveResult, int color) {
    if (!mIsFreeDrawMode) {
      super.onColorSelected(positiveResult, color);
    } else {
      mFreeDrawViewModel.onColorSelected(positiveResult, color);
    }
  }

  public int getFreeDrawColor() {
    return mFreeDrawViewModel.getFreeDrawColor();
  }

  public int getEraserIconColor() {
    return mFreeDrawViewModel.getEraserIconColor();
  }

  private void initializeSelectedIcon() {
    mDisplayIconsInteractorFactory.create(new DisplayIconsInteractor.Displayer() {
      boolean mIsFirst = true;

      @Override
      public void display(Icon icon) {
        if (mIsFirst) {
          updateIcon(icon);
          mIsFirst = false;
        }
      }
    }).execute();
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

  private Collection<GeoEntity> getToSendEntities() {
    if (mIsFreeDrawMode) {
      return mFreeDrawViewModel.getEntities();
    } else if (mCurrentEntity != null) {
      return Collections.singleton(mCurrentEntity);
    }
    return Collections.emptyList();
  }

  private void sendEntities(Collection<GeoEntity> toSend) {
    for (GeoEntity entity : toSend) {
      mAddDynamicEntityRequestInteractorFactory.create(mDynamicLayerId, entity).execute();
    }
  }

  private void removeEntitiesFromMap(Collection<GeoEntity> toSend) {
    for (GeoEntity entity : toSend) {
      mGGMapView.updateMapEntity(GeoEntityNotification.createRemove(entity));
    }
  }

  private void clearInnerEntitiesState() {
    clearCurrentEntity();
    mFreeDrawViewModel.clearFreeDrawer();
  }

  private void clearCurrentEntity() {
    mPoints.clear();
    mCurrentEntity = null;
  }

  private void setIsOnEditMode(boolean isOnEditMode) {
    mIsOnEditMode = isOnEditMode;
    notifyPropertyChanged(BR.onEditMode);
  }

  private void setupActionMode(int newTabResource) {
    stopFreeDrawing();
    if (newTabResource == R.id.tab_point) {
      enableDraw();
      mOnMapClick = this::drawPoint;
      setupPointStyleVisibility();
    } else if (newTabResource == R.id.tab_polyline) {
      enableDraw();
      mOnMapClick = this::drawPolyline;
      setupPolylineStyleVisibility();
    } else if (newTabResource == R.id.tab_polygon) {
      enableDraw();
      mOnMapClick = this::drawPolygon;
      setupPolygonStyleVisibility();
    } else if (newTabResource == R.id.tab_free_draw) {
      setFreeDrawingListeners();
      mFreeDrawViewModel.start();
      mIsFreeDrawMode = true;
      setupFreeDrawStyleVisibility();
    } else if (newTabResource == R.id.tab_remove) {
      enableDelete();
      setupRemoveVisibility();
    } else {
      sLogger.w("Unknown tab selected");
      mGGMapView.setOnMapGestureListener(null);
    }

    notifyPropertyChanged(BR._all);
  }

  private void stopFreeDrawing() {
    mFreeDrawViewModel.stop();
    mIsFreeDrawMode = false;
    if (mOnStartFreeDrawingObserver != null) {
      mOnStartFreeDrawingObserver.dispose();
    }
  }

  private void enableDraw() {
    mGGMapView.setOnMapGestureListener(mDrawOnTapGestureListener);
    mGGMapView.setOnEntityClickedListener(null);
  }

  private void enableDelete() {
    mGGMapView.setOnMapGestureListener(null);
    mGGMapView.setOnEntityClickedListener(mDeleteClickedEntityListener);
  }

  private void setFreeDrawingListeners() {
    mGGMapView.setOnMapGestureListener(null);
    mGGMapView.setOnEntityClickedListener(null);
    mOnStartFreeDrawingObserver = mFreeDrawViewModel.getSignalOnStartDrawingObservable()
        .subscribeWith(new ErrorLoggingObserver<Object>() {
          @Override
          public void onNext(Object o) {
            setIsOnEditMode(true);
          }
        });
  }

  private void setupPointStyleVisibility() {
    mSymbologyPanelVisibility = View.VISIBLE;
    mFreeDrawPanelVisibility = View.GONE;
    mBorderColorVisibility = View.GONE;
    mBorderStyleVisibility = View.GONE;
    mFillColorVisibility = View.GONE;
    mIconPickerVisibility = View.VISIBLE;
  }

  private void setupPolylineStyleVisibility() {
    mSymbologyPanelVisibility = View.VISIBLE;
    mFreeDrawPanelVisibility = View.GONE;
    mBorderColorVisibility = View.VISIBLE;
    mBorderStyleVisibility = View.VISIBLE;
    mFillColorVisibility = View.GONE;
    mIconPickerVisibility = View.GONE;
  }

  private void setupPolygonStyleVisibility() {
    mSymbologyPanelVisibility = View.VISIBLE;
    mFreeDrawPanelVisibility = View.GONE;
    mBorderColorVisibility = View.VISIBLE;
    mBorderStyleVisibility = View.VISIBLE;
    mFillColorVisibility = View.VISIBLE;
    mIconPickerVisibility = View.GONE;
  }

  private void setupFreeDrawStyleVisibility() {
    mSymbologyPanelVisibility = View.GONE;
    mFreeDrawPanelVisibility = View.VISIBLE;
  }

  private void setupRemoveVisibility() {
    mSymbologyPanelVisibility = View.GONE;
    mFreeDrawPanelVisibility = View.GONE;
  }

  private void drawPoint(PointGeometry geometry) {
    PointSymbol symbol =
        new PointSymbol.PointSymbolBuilder().setIconId(mSelectedIcon.getId()).build();

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

  private class DrawOnTapGestureListener implements OnMapGestureListener {

    @Override
    public void onLongPress(PointGeometry pointGeometry) {
    }

    @Override
    public void onTap(PointGeometry pointGeometry) {
      try {
        setIsOnEditMode(true);
        mOnMapClick.accept(pointGeometry);
      } catch (Exception e) {
        sLogger.w("Could not process map click", e);
      }
    }
  }

  private class OnPropertyChanged extends OnPropertyChangedCallback {
    @Override
    public void onPropertyChanged(Observable sender, int propertyId) {
      if (stylingPropertyChanged(propertyId)) {
        refreshCurrentEntity();
      }
    }

    private boolean stylingPropertyChanged(int propertyId) {
      return propertyId == BR.borderColor || propertyId == BR.fillColor;
    }
  }
}
