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
  private final String mDynamicLayerId;

  private DisplayDynamicLayerDetailsInteractor mInteractor;
  private String mDescription;

  public DynamicLayerDetailsViewModel(
      @Provided DisplayDynamicLayerDetailsInteractorFactory interactorFactory,
      MasterListCallback masterListCallback,
      DynamicLayerDetailsFragment.OnDynamicEntityClickedListener onDynamicEntityClickedListener,
      String dynamicLayerId) {
    mInteractorFactory = interactorFactory;
    mMasterListCallback = masterListCallback;
    mOnDynamicEntityClickedListener = onDynamicEntityClickedListener;
    mDynamicLayerId = dynamicLayerId;
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
    return mDescription != null ? mDescription : "";
  }

  private void updateDynamicLayer(DynamicLayer dynamicLayer) {
    mMasterListCallback.clear();
    mMasterListCallback.addHeader(dynamicLayer.getName(),
        view -> onLayerListingClicked(dynamicLayer));
    int entitiesCounter = 1;
    for (DynamicEntity de : dynamicLayer.getEntities()) {
      mMasterListCallback.addDetails(getEntityTitle(de, entitiesCounter++),
          view -> onEntityListingClicked(de));
    }
  }

  private void onLayerListingClicked(DynamicLayer dynamicLayer) {
    updateDescription(dynamicLayer.getDescription());
  }

  private void updateDescription(String description) {
    mDescription = description;
    notifyChange();
  }

  private String getEntityTitle(DynamicEntity de, int entitiesCounter) {
    return "Entity #" + entitiesCounter;
  }

  private void onEntityListingClicked(DynamicEntity de) {
    updateDescription(de.getDescription());
    mOnDynamicEntityClickedListener.onDynamicEntityClicked(de);
  }

  interface MasterListCallback {

    void addHeader(String title, View.OnClickListener listener);

    void addDetails(String title, View.OnClickListener listener);

    void clear();
  }
}
