package com.teamagam.gimelgimel.app.map.viewModel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

@AutoFactory
public class BaseMapViewModel extends BaseViewModel {

    private final DisplayMapEntitiesInteractorFactory mMapEntitiesInteractorFactory;
    private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
    private final DisplayIntermediateRastersInteractorFactory mDisplayIntermediateRastersInteractorFactory;

    private final GGMapView mGGMapView;

    private DisplayMapEntitiesInteractor mMapEntitiesInteractor;
    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
    private DisplayIntermediateRastersInteractor mDisplayIntermediateRastersInteractor;

    protected BaseMapViewModel(
            @Provided DisplayMapEntitiesInteractorFactory mapEntitiesInteractorFactory,
            @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
            @Provided DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
            GGMapView ggMapView) {
        mMapEntitiesInteractorFactory = mapEntitiesInteractorFactory;
        mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
        mDisplayIntermediateRastersInteractorFactory = displayIntermediateRastersInteractorFactory;
        mGGMapView = ggMapView;
    }

    @Override
    public void start() {
        super.start();
        mGGMapView.setOnReadyListener(this::onMapReady);
    }

    @Override
    public void stop() {
        super.stop();
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