package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import com.android.databinding.library.baseAdapters.BR;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.dynamic_layer.DynamicLayerDetailsFragment;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.BaseGeometryStyleViewModel;
import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawViewModel;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayerDetailsInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteAddDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteRemoveDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteUpdateDescriptionDynamicLayerRequestInteractorFactory;
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
  private SendRemoteUpdateDescriptionDynamicLayerRequestInteractorFactory
      mSendRemoteUpdateDescriptionDynamicLayerRequestInteractor;
  private SendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractorFactory
      mSendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractorFactory;
  private DisplayDynamicLayerDetailsInteractorFactory mDisplayDynamicLayerDetailsInteractorFactory;
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
  private String mEditedDescription;
  private Context mContext;
  private DynamicLayerDetailsFragment mDynamicLayerDetailsFragment;
  private DynamicEntity mEditedDynamicEntity;
  private boolean mIsEditDescriptionFabVisible;

  protected EditDynamicLayerViewModel(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided
          SendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractorFactory sendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractorFactory,
      @Provided DisplayIconsInteractorFactory displayIconsInteractorFactory,
      @Provided
          SendRemoteAddDynamicEntityRequestInteractorFactory addDynamicEntityRequestInteractorFactory,
      @Provided
          SendRemoteRemoveDynamicEntityRequestInteractorFactory removeDynamicEntityRequestInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawViewModelFactory freeDrawViewModelFactory,
      @Provided
          DisplayDynamicLayerDetailsInteractorFactory displayDynamicLayerDetailsInteractorFactory,
      @Provided
          SendRemoteUpdateDescriptionDynamicLayerRequestInteractorFactory sendRemoteUpdateDescriptionDynamicLayerRequestInteractorFactory,
      Context context,
      Navigator navigator,
      GGMapView ggMapView,
      DynamicLayerEntityDeleteListener.DeleteEntityDialogDisplayer deleteEntityDialogDisplayer,
      Consumer<Integer> pickColor,
      Consumer<String> pickBorderStyle,
      Consumer<Icon> iconDisplayer,
      String dynamicLayerId,
      DynamicLayerDetailsFragment dynamicLayerDetailsFragment) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayDynamicLayersInteractorFactory, displayIntermediateRastersInteractorFactory, context,
        ggMapView, pickColor, pickBorderStyle);
    mContext = context;
    mSendRemoteUpdateDescriptionDynamicLayerRequestInteractor =
        sendRemoteUpdateDescriptionDynamicLayerRequestInteractorFactory;
    mSendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractorFactory =
        sendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractorFactory;
    mDisplayDynamicLayerDetailsInteractorFactory = displayDynamicLayerDetailsInteractorFactory;
    mDisplayIconsInteractorFactory = displayIconsInteractorFactory;
    mAddDynamicEntityRequestInteractorFactory = addDynamicEntityRequestInteractorFactory;
    mNavigator = navigator;
    mGGMapView = ggMapView;
    mIconDisplayer = iconDisplayer;
    mDynamicLayerId = dynamicLayerId;
    mDynamicLayerDetailsFragment = dynamicLayerDetailsFragment;
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
    mIsEditDescriptionFabVisible = false;
  }

  @Override
  public void init() {
    super.init();
    initializeSelectedIcon();
    initializeDescriptionEditInteractor();
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

  public void setEditedEntityItemSelected(DynamicEntity dynamicLayerEntity) {
    mEditedDynamicEntity = dynamicLayerEntity;
    mIsEditDescriptionFabVisible = true;
    notifyPropertyChanged(BR.editDescriptionFabButtonVisible);
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

  public void onEditDescriptionFabClicked() {
    EditText input = new EditText(mContext);
    input.setText(mEditedDescription);
    if (mEditedDynamicEntity != null) {
      mEditedDescription = mEditedDynamicEntity.getDescription();
      createEditDescriptionAlertDialog(input, (dialogInterface, i) -> {
        mEditedDescription = input.getText().toString();
        sendEntityDescription();
        mDynamicLayerDetailsFragment.getViewModel().updateDescription(mEditedDescription);
      }, mContext.getString(R.string.edit_dynamic_layer_entity_alert_title), "");
    } else {
      createEditDescriptionAlertDialog(input, (dialogInterface, i) -> {
            mEditedDescription = input.getText().toString();
            sendDescription();
            mDynamicLayerDetailsFragment.getViewModel().updateDescription(mEditedDescription);
          }, mContext.getString(R.string.edit_layer_description_alert_title),
          mContext.getString(R.string.edit_layer_description_alert_message));
    }
  }

  @Bindable
  public boolean isEditDescriptionFabButtonVisible() {
    return mIsEditDescriptionFabVisible;
  }

  private void createEditDescriptionAlertDialog(EditText input,
      DialogInterface.OnClickListener clickListener,
      String title,
      String message) {
    new AlertDialog.Builder(mContext).setTitle(title)
        .setMessage(message)
        .setView(input)
        .setPositiveButton(R.string.save_label, clickListener)
        .setNegativeButton(R.string.picker_cancel, (dialog, whichButton) -> dialog.cancel())
        .create()
        .show();
  }

  @Override
  public void onBorderStyleSelected(String borderStyle) {
    super.onBorderStyleSelected(borderStyle);
    updateSymbol();
  }

  public void onSaveChangesFabClicked() {
    sendGeometryChanges();
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

  private void initializeDescriptionEditInteractor() {
    mDisplayDynamicLayerDetailsInteractorFactory.create(
        (dynamicLayer) -> mEditedDescription = dynamicLayer.getDescription(), mDynamicLayerId)
        .execute();
  }

  private void sendGeometryChanges() {
    Collection<GeoEntity> toSend = mCurrentMapAction.getEntities();
    sendEntities(toSend);
  }

  private void sendDescription() {
    mSendRemoteUpdateDescriptionDynamicLayerRequestInteractor.create(mEditedDescription,
        mDynamicLayerId).execute();
  }

  public void onDynamicEntityListingClicked(DynamicEntity dynamicEntity) {
    mGGMapView.lookAt(dynamicEntity.getGeoEntity().getGeometry());
  }

  private void sendEntityDescription() {
    mSendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractorFactory.create(
        mEditedDynamicEntity, mEditedDescription, mDynamicLayerId).execute();
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
    } catch (Exception ignored) {
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
      mEditedDynamicEntity = null;
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