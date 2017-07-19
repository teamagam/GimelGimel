package com.teamagam.gimelgimel.app.map;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterPresentation;
import io.reactivex.functions.Function;
import java.util.HashMap;
import java.util.Map;

public class BaseMapViewModel<V> extends BaseViewModel<V> {

  private final DisplayMapEntitiesInteractorFactory mMapEntitiesInteractorFactory;
  private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
  private final DisplayDynamicLayersInteractorFactory mDisplayDynamicLayersInteractorFactory;
  private final DisplayIntermediateRastersInteractorFactory
      mDisplayIntermediateRastersInteractorFactory;

  private final GGMapView mGGMapView;

  private DisplayMapEntitiesInteractor mDisplayMapEntitiesInteractor;
  private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
  private DisplayDynamicLayersInteractor mDisplayDynamicLayersInteractor;
  private DisplayIntermediateRastersInteractor mDisplayIntermediateRastersInteractor;

  protected BaseMapViewModel(DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      GGMapView ggMapView) {
    mMapEntitiesInteractorFactory = displayMapEntitiesInteractorFactory;
    mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
    mDisplayDynamicLayersInteractorFactory = displayDynamicLayersInteractorFactory;
    mDisplayIntermediateRastersInteractorFactory = displayIntermediateRastersInteractorFactory;
    mGGMapView = ggMapView;
  }

  @Override
  public void init() {
    super.init();
    mGGMapView.setOnReadyListener(this::onMapReady);
  }

  @Override
  public void destroy() {
    super.destroy();
    unsubscribe(mDisplayMapEntitiesInteractor, mDisplayIntermediateRastersInteractor,
        mDisplayVectorLayersInteractor, mDisplayDynamicLayersInteractor);
    mGGMapView.setOnReadyListener(null);
  }

  private void onMapReady() {
    initializeInteractors();
    execute(mDisplayMapEntitiesInteractor, mDisplayIntermediateRastersInteractor,
        mDisplayVectorLayersInteractor, mDisplayDynamicLayersInteractor);
  }

  private void initializeInteractors() {
    mDisplayMapEntitiesInteractor =
        mMapEntitiesInteractorFactory.create(mGGMapView::updateMapEntity);

    mDisplayVectorLayersInteractor =
        mDisplayVectorLayersInteractorFactory.create(new VectorLayersInteractorDisplayer());

    mDisplayDynamicLayersInteractor =
        mDisplayDynamicLayersInteractorFactory.create(new DynamicLayersInteractorDisplayer());

    mDisplayIntermediateRastersInteractor =
        mDisplayIntermediateRastersInteractorFactory.create(new IntermediateRastersDisplayer());
  }

  class VectorLayersInteractorDisplayer implements DisplayVectorLayersInteractor.Displayer {
    @Override
    public void display(VectorLayerPresentation vlp) {
      if (vlp.isShown()) {
        mGGMapView.showVectorLayer(vlp);
      } else {
        mGGMapView.hideVectorLayer(vlp.getId());
      }
    }
  }

  class DynamicLayersInteractorDisplayer implements DisplayDynamicLayersInteractor.Displayer {
    Map<String, DynamicLayer> mDisplayed = new HashMap<>();

    @Override
    public void display(DynamicLayer dl) {
      String id = dl.getId();
      if (mDisplayed.containsKey(id)) {
        updateEntitiesOnMap(id, GeoEntityNotification::createRemove);
      }
      mDisplayed.put(id, dl);
      updateEntitiesOnMap(id, GeoEntityNotification::createAdd);
    }

    private void updateEntitiesOnMap(String id,
        Function<GeoEntity, GeoEntityNotification> creator) {
      for (GeoEntity entity : mDisplayed.get(id).getEntities()) {
        try {
          mGGMapView.updateMapEntity(creator.apply(entity));
        } catch (Exception ignored) {
        }
      }
    }
  }

  class IntermediateRastersDisplayer implements DisplayIntermediateRastersInteractor.Displayer {
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