package com.teamagam.gimelgimel.app.map.viewModel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.SendQuadrilateralActionFragment;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

@AutoFactory
public class SendQuadrilateralActionViewModel extends BaseMapViewModel<SendQuadrilateralActionFragment> {

    protected SendQuadrilateralActionViewModel(
            @Provided DisplayMapEntitiesInteractorFactory mapEntitiesInteractorFactory,
            @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
            @Provided DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
            GGMapView ggMapView) {
        super(mapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
                displayIntermediateRastersInteractorFactory, ggMapView);
    }

    public void onPositiveButtonClick() {

    }

    public void onShowButtonClicked() {

    }
}