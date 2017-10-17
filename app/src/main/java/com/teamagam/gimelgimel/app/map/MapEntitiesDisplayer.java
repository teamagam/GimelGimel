package com.teamagam.gimelgimel.app.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.utils.InteractorUtils;
import com.teamagam.gimelgimel.domain.base.interactors.Interactor;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.DynamicLayerPresentation;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.phase.visibility.DisplayPhaseLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterPresentation;
import java.util.ArrayList;
import java.util.List;

@AutoFactory
public class MapEntitiesDisplayer {

  private final DisplayMapEntitiesInteractorFactory mDisplayMapEntitiesInteractorFactory;
  private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
  private final DisplayDynamicLayersInteractorFactory mDisplayDynamicLayersInteractorFactory;
  private final DisplayIntermediateRastersInteractorFactory
      mDisplayIntermediateRastersInteractorFactory;
  private final DisplayPhaseLayersInteractorFactory mDisplayPhaseLayersInteractorFactory;
  private final GGMapView mGGMapView;
  private final List<Interactor> mInteractors;

  MapEntitiesDisplayer(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided DisplayPhaseLayersInteractorFactory displayPhaseLayersInteractorFactory,
      GGMapView ggMapView) {
    mDisplayMapEntitiesInteractorFactory = displayMapEntitiesInteractorFactory;
    mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
    mDisplayDynamicLayersInteractorFactory = displayDynamicLayersInteractorFactory;
    mDisplayIntermediateRastersInteractorFactory = displayIntermediateRastersInteractorFactory;
    mDisplayPhaseLayersInteractorFactory = displayPhaseLayersInteractorFactory;
    mGGMapView = ggMapView;
    mInteractors = new ArrayList<>();
  }

  public void start() {
    initializeInteractors();
    InteractorUtils.execute(mInteractors);
  }

  public void stop() {
    InteractorUtils.unsubscribe(mInteractors);
  }

  private void initializeInteractors() {
    mInteractors.add(mDisplayMapEntitiesInteractorFactory.create(mGGMapView::updateMapEntity));

    mInteractors.add(
        mDisplayVectorLayersInteractorFactory.create(new VectorLayersInteractorDisplayer()));

    DynamicLayersMapDisplayer dlDisplayer = new DynamicLayersMapDisplayer(mGGMapView);
    mInteractors.add(mDisplayDynamicLayersInteractorFactory.create(dlDisplayer));

    mInteractors.add(
        mDisplayIntermediateRastersInteractorFactory.create(new IntermediateRastersDisplayer()));

    mInteractors.add(mDisplayPhaseLayersInteractorFactory.create(plp -> {
      for (DynamicLayer dl : plp.getPhases()) {
        dlDisplayer.display(new DynamicLayerPresentation(dl, plp.isShown()));
      }
    }));
  }

  private class VectorLayersInteractorDisplayer implements DisplayVectorLayersInteractor.Displayer {
    @Override
    public void display(VectorLayerPresentation vlp) {
      if (vlp.isShown()) {
        mGGMapView.showVectorLayer(vlp);
      } else {
        mGGMapView.hideVectorLayer(vlp.getId());
      }
    }
  }

  private class IntermediateRastersDisplayer
      implements DisplayIntermediateRastersInteractor.Displayer {
    @Override
    public void display(IntermediateRasterPresentation intermediateRasterPresentation) {
      if (intermediateRasterPresentation.isShown()) {
        mGGMapView.setIntermediateRaster(intermediateRasterPresentation);
      } else {
        mGGMapView.removeIntermediateRaster();
      }
    }
  }
}
