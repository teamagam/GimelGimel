package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

public class BaseMapViewModel<V> extends BaseViewModel<V> {

    private final DisplayMapEntitiesInteractorFactory mMapEntitiesInteractorFactory;
    private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
    private final DisplayIntermediateRastersInteractorFactory mDisplayIntermediateRastersInteractorFactory;

    private final GGMapView mGGMapView;

    private DisplayMapEntitiesInteractor mMapEntitiesInteractor;
    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
    private DisplayIntermediateRastersInteractor mDisplayIntermediateRastersInteractor;

    protected BaseMapViewModel(
            DisplayMapEntitiesInteractorFactory mapEntitiesInteractorFactory,
            DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
            DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
            GGMapView ggMapView) {
        mMapEntitiesInteractorFactory = mapEntitiesInteractorFactory;
        mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
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
        unsubscribe(mMapEntitiesInteractor,
                mDisplayIntermediateRastersInteractor,
                mDisplayVectorLayersInteractor);
    }

    private void onMapReady() {
        initializeInteractors();
        execute(mMapEntitiesInteractor,
                mDisplayVectorLayersInteractor,
                mDisplayIntermediateRastersInteractor);
    }

    private void initializeInteractors() {
        mMapEntitiesInteractor = mMapEntitiesInteractorFactory.create(
                mGGMapView::updateMapEntity);

        mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(
                new VectorLayersInteractorDisplayer());

        mDisplayIntermediateRastersInteractor = mDisplayIntermediateRastersInteractorFactory.create(
                new IntermediateRastersDisplayer());
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

    private class IntermediateRastersDisplayer implements DisplayIntermediateRastersInteractor.Displayer {
        @Override
        public void display(
                DisplayIntermediateRastersInteractor.IntermediateRasterPresentation intermediateRasterPresentation) {
            if (intermediateRasterPresentation.isShown()) {
                mGGMapView.setIntermediateRaster(intermediateRasterPresentation);
            } else {
                mGGMapView.removeIntermediateRaster();
            }
        }
    }
}