package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

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
import com.teamagam.gimelgimel.app.map.actions.BaseGeometryStyleViewModel;
import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawViewModel;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteAddDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteRemoveDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractor;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;
import java.util.Collection;

@AutoFactory
public class EditDynamicLayerViewModel extends BaseGeometryStyleViewModel {

  private final DisplayIconsInteractorFactory mDisplayIconsInteractorFactory;
  private final SendRemoteAddDynamicEntityRequestInteractorFactory
      mAddDynamicEntityRequestInteractorFactory;
  private FreeDrawViewModel mFreeDrawViewModel;
  private Consumer<Icon> mIconDisplayer;
  private DynamicLayerEntityDeleteListener mDeleteClickedEntityListener;
  private String mDynamicLayerId;
  private GGMapView mGGMapView;
  private MapDrawer mMapDrawer;
  private MapEntityFactory mEntityFactory;
  private boolean mIsOnEditMode;
  private int mBorderStyleVisibility;
  private int mBorderColorVisibility;
  private int mFillColorVisibility;
  private int mIconPickerVisibility;
  private int mSymbologyPanelVisibility;
  private int mFreeDrawPanelVisibility;
  private int mDetailsPanelVisibility;
  private Icon mSelectedIcon;
  private Navigator mNavigator;
  private MapAction mCurrentMapAction;
  private SymbolFactory mSymbolFactory;
  private boolean mEditDescriptionBoxVisible;

  private DynamicLayer mDynamicLayer;
  private DynamicLayersRepository mDynamicLayersRepository;

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
      @Provided DynamicLayersRepository dynamicLayersRepository,
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
    mDynamicLayersRepository = dynamicLayersRepository;
    mDisplayIconsInteractorFactory = displayIconsInteractorFactory;
    mAddDynamicEntityRequestInteractorFactory = addDynamicEntityRequestInteractorFactory;
    mNavigator = navigator;
    mGGMapView = ggMapView;
    mIconDisplayer = iconDisplayer;
    mDynamicLayerId = dynamicLayerId;
    mIsOnEditMode = false;
    mEditDescriptionBoxVisible = false;
    mMapDrawer = new MapDrawer(mGGMapView);
    mEntityFactory = new MapEntityFactory();
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
    mCurrentMapAction = null;
  }

  @Override
  public void init() {
    super.init();
    initializeSelectedIcon();
    mDynamicLayer = mDynamicLayersRepository.getById(mDynamicLayerId);
    //notifyPropertyChanged();
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

  public int getDetailsPanelVisibility() {
    return mDetailsPanelVisibility;
  }

  public void onNewTabSelection(int newTabResource) {
    if (!mIsOnEditMode) {
      setupActionMode(newTabResource);
    }
  }

  @Bindable
  public boolean isEditDescriptionVisible() {
    return mEditDescriptionBoxVisible;
  }

  public void onEditDescriptionFabClicked() {
    mEditDescriptionBoxVisible = !mEditDescriptionBoxVisible;
    notifyPropertyChanged(BR.editDescriptionVisible);
  }

  public void onEditDescriptionTextChange(CharSequence text) {
  }

  @Bindable
  public String getDynamicLayerDescription() {
    return mDynamicLayer.getDescription();
  }

  @Override
  public void onBorderStyleSelected(String borderStyle) {
    super.onBorderStyleSelected(borderStyle);
    updateSymbol();
  }

  public void sendCurrentGeometry() {
    Collection<GeoEntity> toSend = mCurrentMapAction.getEntities();
    sendEntities(toSend);
    resetAction();
  }

  public void resetAction() {
    if (mCurrentMapAction != null) {
      mCurrentMapAction.stop();
      setIsOnEditMode(false);
      startCurrentAction();
    }
  }

  public void onSwitchChanged(boolean isChecked) {
    mFreeDrawViewModel.onSwitchChanged(isChecked);
  }

  public void onIconSelectionClicked() {
    mNavigator.openIconSelectionDialog(this::updateIcon);
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

  public void onColorSelected(int color) {
    super.onColorSelected(color);
    mFreeDrawViewModel.onColorSelected(color);
  }

  public int getFreeDrawColor() {
    return mFreeDrawViewModel.getFreeDrawColor();
  }

  public int getEraserIconColor() {
    return mFreeDrawViewModel.getEraserIconColor();
  }

  public void onDynamicEntityListingClicked(DynamicEntity dynamicEntity) {
    mGGMapView.lookAt(dynamicEntity.getGeoEntity().getGeometry());
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

  private void sendEntities(Collection<GeoEntity> toSend) {
    for (GeoEntity entity : toSend) {
      mAddDynamicEntityRequestInteractorFactory.create(mDynamicLayerId, entity).execute();
    }
  }

  private void setIsOnEditMode(boolean isOnEditMode) {
    mIsOnEditMode = isOnEditMode;
    notifyPropertyChanged(BR.onEditMode);
  }

  private void setupActionMode(int newTabResource) {
    setupCurrentAction(newTabResource);

    if (mCurrentMapAction == null) return;

    startCurrentAction();
    notifyPropertyChanged(BR._all);
  }

  private void startCurrentAction() {
    mCurrentMapAction.setupAction(mGGMapView, mSymbolFactory.create());
    mCurrentMapAction.setupSymbologyPanel(new SymbologyPanelVisibilitySetter());
    mCurrentMapAction.start();
  }

  private void updateIcon(Icon icon) {
    mSelectedIcon = icon;
    updateSymbol();
    try {
      mIconDisplayer.accept(icon);
    } catch (Exception e) {
    }
  }

  private void updateSymbol() {
    mCurrentMapAction.updateSymbol(mSymbolFactory.create());
  }

  private void setupCurrentAction(int newTabResource) {
    stopPrevious();
    mSymbolFactory = new StubSymbolFactory();
    if (newTabResource == R.id.tab_point) {
      mCurrentMapAction = new PointMapDrawer(mEntityFactory, mMapDrawer, getEditingStartListener());
      mSymbolFactory = new PointSymbolFactory();
    } else if (newTabResource == R.id.tab_polyline) {
      mCurrentMapAction =
          new PolylineMapDrawer(mEntityFactory, mMapDrawer, getEditingStartListener());
      mSymbolFactory = new PolylineSymbolFactory();
    } else if (newTabResource == R.id.tab_polygon) {
      mCurrentMapAction =
          new PolygonMapDrawer(mEntityFactory, mMapDrawer, getEditingStartListener());
      mSymbolFactory = new PolygonSymbolFactory();
    } else if (newTabResource == R.id.tab_free_draw) {
      mCurrentMapAction = new FreedrawMapDrawer(mFreeDrawViewModel, getEditingStartListener());
    } else if (newTabResource == R.id.tab_remove) {
      mCurrentMapAction = new DeleteAction(mDeleteClickedEntityListener);
    } else if (newTabResource == R.id.tab_details) {
      mCurrentMapAction = new DetailsAction();
    } else {
      sLogger.w("Unknown tab selected");
      mGGMapView.setOnMapGestureListener(null);
    }
  }

  private void stopPrevious() {
    if (mCurrentMapAction != null) {
      mCurrentMapAction.stop();
    }
  }

  private OnEditingStartListener getEditingStartListener() {
    return () -> setIsOnEditMode(true);
  }

  private interface SymbolFactory {
    Symbol create();
  }

  private class StubSymbolFactory implements SymbolFactory {

    @Override
    public Symbol create() {
      return null;
    }
  }

  private class PointSymbolFactory implements SymbolFactory {
    @Override
    public Symbol create() {
      return new PointSymbol.PointSymbolBuilder().setIconId(mSelectedIcon.getId()).build();
    }
  }

  private class PolygonSymbolFactory implements SymbolFactory {
    @Override
    public Symbol create() {
      return new PolygonSymbol.PolygonSymbolBuilder().setBorderStyle(mBorderStyle)
          .setBorderColor(mBorderColor)
          .setFillColor(mFillColor)
          .build();
    }
  }

  private class PolylineSymbolFactory implements SymbolFactory {

    @Override
    public Symbol create() {
      return new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(mBorderColor)
          .setBorderStyle(mBorderStyle)
          .build();
    }
  }

  class SymbologyPanelVisibilitySetter {

    void hideAll() {
      mSymbologyPanelVisibility = View.GONE;
      mFreeDrawPanelVisibility = View.GONE;
      mBorderColorVisibility = View.GONE;
      mBorderStyleVisibility = View.GONE;
      mFillColorVisibility = View.GONE;
      mIconPickerVisibility = View.GONE;
      mDetailsPanelVisibility = View.GONE;
    }

    void showFreeDrawPanel() {
      mFreeDrawPanelVisibility = View.VISIBLE;
    }

    void showBorder() {
      mSymbologyPanelVisibility = View.VISIBLE;
      mBorderColorVisibility = View.VISIBLE;
      mBorderStyleVisibility = View.VISIBLE;
    }

    void showFill() {
      mSymbologyPanelVisibility = View.VISIBLE;
      mFillColorVisibility = View.VISIBLE;
    }

    void showIcon() {
      mSymbologyPanelVisibility = View.VISIBLE;
      mIconPickerVisibility = View.VISIBLE;
    }

    void showDetailsPanel() {
      mDetailsPanelVisibility = View.VISIBLE;
    }
  }

  private class OnPropertyChanged extends OnPropertyChangedCallback {
    @Override
    public void onPropertyChanged(Observable sender, int propertyId) {
      if (stylingPropertyChanged(propertyId)) {
        updateSymbol();
      }
    }

    private boolean stylingPropertyChanged(int propertyId) {
      return propertyId == BR.borderColor || propertyId == BR.fillColor;
    }
  }
}