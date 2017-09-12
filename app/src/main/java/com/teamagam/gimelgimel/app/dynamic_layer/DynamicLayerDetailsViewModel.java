package com.teamagam.gimelgimel.app.dynamic_layer;

import android.view.View;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayerDetailsInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayerDetailsInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;

@AutoFactory
public class DynamicLayerDetailsViewModel extends BaseViewModel {

  private final DisplayDynamicLayerDetailsInteractorFactory mInteractorFactory;
  private final MasterListCallback mMasterListCallback;
  private final DynamicLayerDetailsFragment.OnDynamicEntityClickedListener
      mOnDynamicEntityClickedListener;
  private final DetailsStringProvider mStringProvider;
  private final String mDynamicLayerId;
  private final String mDynamicEntityId;

  private DisplayDynamicLayerDetailsInteractor mInteractor;
  private String mDescription;
  private boolean mFirstDisplay;

  public DynamicLayerDetailsViewModel(
      @Provided DisplayDynamicLayerDetailsInteractorFactory interactorFactory,
      MasterListCallback masterListCallback,
      DynamicLayerDetailsFragment.OnDynamicEntityClickedListener onDynamicEntityClickedListener,
      DetailsStringProvider stringProvider, String dynamicLayerId, String dynamicEntityId) {
    mInteractorFactory = interactorFactory;
    mMasterListCallback = masterListCallback;
    mOnDynamicEntityClickedListener = onDynamicEntityClickedListener;
    mStringProvider = stringProvider;
    mDynamicLayerId = dynamicLayerId;
    mDynamicEntityId = dynamicEntityId;
    mFirstDisplay = true;
  }

  @Override
  public void start() {
    super.start();
    mInteractor = mInteractorFactory.create(this::updateDynamicLayer, mDynamicLayerId);
    execute(mInteractor);
  }

  @Override
  public void stop() {
    unsubscribe(mInteractor);
  }

  public String getDetails() {
    return mDescription != null ? mDescription : mStringProvider.getDefaultDetails();
  }

  private void updateDynamicLayer(DynamicLayer dynamicLayer) {
    mMasterListCallback.clear();
    displayLayer(dynamicLayer);
    int entityCount = 1;
    for (DynamicEntity de : dynamicLayer.getEntities()) {
      displayEntity(de, entityCount++);
    }
  }

  private void displayLayer(DynamicLayer dynamicLayer) {
    mMasterListCallback.addHeader(mStringProvider.getLayerDetailsHeader(dynamicLayer),
        view -> onLayerListingClicked(dynamicLayer));
  }

  private void displayEntity(DynamicEntity de, int entityCount) {
    String title = mStringProvider.getEntityDetailsHeader(de, entityCount);
    mMasterListCallback.addDetails(title, view -> onEntityListingClicked(de));
    if (de.getId().equals(mDynamicEntityId)) {
      selectOnFirstDisplay(title, de.getDescription());
    }
  }

  private void selectOnFirstDisplay(String title, String description) {
    if (mFirstDisplay) {
      mMasterListCallback.select(title);
      updateDescription(description);
      mFirstDisplay = false;
    }
  }

  private void onLayerListingClicked(DynamicLayer dynamicLayer) {
    updateDescription(dynamicLayer.getDescription());
  }

  private void updateDescription(String description) {
    mDescription = description;
    notifyChange();
  }

  private void onEntityListingClicked(DynamicEntity de) {
    updateDescription(de.getDescription());
    mOnDynamicEntityClickedListener.onDynamicEntityListingClicked(de);
  }

  interface MasterListCallback {

    void addHeader(String title, View.OnClickListener listener);

    void addDetails(String title, View.OnClickListener listener);

    void select(String title);

    void clear();
  }

  interface DetailsStringProvider {
    String getDefaultDetails();

    String getLayerDetailsHeader(DynamicLayer dynamicLayer);

    String getEntityDetailsHeader(DynamicEntity dynamicEntity, int entityCount);
  }
}
