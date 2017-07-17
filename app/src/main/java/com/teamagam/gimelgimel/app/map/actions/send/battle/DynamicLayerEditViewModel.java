package com.teamagam.gimelgimel.app.map.actions.send.battle;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.BaseMapViewModel;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

@AutoFactory
public class DynamicLayerEditViewModel extends BaseMapViewModel {

  protected DynamicLayerEditViewModel(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      GGMapView ggMapView) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);
  }

  public void onUndoClicked() {

  }

  public void onSendFabClicked() {

  }

  public String getDescription() {
    return "";
  }

  public void setDescription(String description) {

  }
}
